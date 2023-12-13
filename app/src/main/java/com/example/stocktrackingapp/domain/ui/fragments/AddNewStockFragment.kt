package com.example.stocktrackingapp.domain.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

import com.example.stocktrackingapp.R
import com.example.stocktrackingapp.databinding.FragmentNewStockBinding
import com.example.stocktrackingapp.domain.ui.adapters.StockSelectedListener
import com.example.stocktrackingapp.domain.viewmodels.SearchStocksViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class AddNewStockFragment : DialogFragment() {

    private lateinit var stockSelectedListener: StockSelectedListener
    private lateinit var binding: FragmentNewStockBinding
    private lateinit var dialog: Dialog
    private lateinit var adapter: ArrayAdapter<String>

    private var isDialogOpen = false
    private val handler = Handler(Looper.getMainLooper())
    private val viewModel: SearchStocksViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogT = super.onCreateDialog(savedInstanceState)
        dialogT.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_searchable_spinner)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        adapter = ArrayAdapter<String>(
            requireContext(), R.layout.list_view_adapter, mutableListOf()
        )

        return dialogT
    }

    fun setTextChangeListener(listener: StockSelectedListener) {
        stockSelectedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectedCompany.setOnClickListener {
            if (!isDialogOpen) {
                dialog.show()

                val editText = dialog.findViewById<EditText>(R.id.edit_text)
                val listView = dialog.findViewById<ListView>(R.id.list_view)
                listView.adapter = adapter
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence?, p1: Int, p2: Int, p3: Int
                    ) {
                    }

                    override fun onTextChanged(
                        charSequence: CharSequence?, p1: Int, p2: Int, p3: Int
                    ) {
                        // Call ViewModel method to filter data based on search query
                        viewModel.searchStocksCompany(charSequence.toString())
                    }

                    override fun afterTextChanged(editable: Editable?) {}
                })
                listView.setOnItemClickListener { adapterView, _, i, _ ->
                    run {
                        binding.selectedCompany.text = adapterView.getItemAtPosition(i).toString()
                        dialog.dismiss()
                    }
                }
                isDialogOpen = true
                handler.postDelayed({
                    isDialogOpen = false
                }, 500)
                dialog.setOnDismissListener {
                    isDialogOpen = false
                }
            }
        }
        binding.addStock.setOnClickListener {
            stockSelectedListener.onStockSelected(binding.selectedCompany.text.toString())
            dismiss()
        }

        viewModel.flowState.observe(this, this::onSearchStockFlowState)
    }

    private fun onSearchStockFlowState(state: SearchStocksViewModel.SearchStockFlowState) {
        when (state) {
            is SearchStocksViewModel.SearchStockFlowState.OnError -> {
                Toasty.error(requireContext(), getString(state.error), Toast.LENGTH_SHORT, true)
                    .show()
            }

            SearchStocksViewModel.SearchStockFlowState.OnLoading -> {
                val progressBar = dialog.findViewById<ProgressBar>(R.id.progress_bar)
                progressBar.visibility = View.VISIBLE
            }

            is SearchStocksViewModel.SearchStockFlowState.OnSuccess -> {
                val progressBar = dialog.findViewById<ProgressBar>(R.id.progress_bar)
                progressBar.visibility = View.GONE
                val mutableStocks = state.stocksCompanies.toMutableList()

                adapter.clear()
                adapter.addAll(mutableStocks)
                adapter.notifyDataSetChanged()
            }
        }
    }
}
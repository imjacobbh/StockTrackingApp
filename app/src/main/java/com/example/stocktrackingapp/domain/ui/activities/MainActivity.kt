package com.example.stocktrackingapp.domain.ui.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stocktrackingapp.R
import com.example.stocktrackingapp.databinding.ActivityMainBinding
import com.example.stocktrackingapp.domain.model.StocksDomainModel
import com.example.stocktrackingapp.domain.ui.adapters.OnClickStockItem
import com.example.stocktrackingapp.domain.ui.adapters.StockAdapter
import com.example.stocktrackingapp.domain.ui.adapters.StockSelectedListener
import com.example.stocktrackingapp.domain.ui.fragments.AddNewStockFragment
import com.example.stocktrackingapp.domain.viewmodels.StocksViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), StockSelectedListener, OnClickStockItem {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: StocksViewModel by viewModels()
    private lateinit var adapter: StockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.button.setOnClickListener {
            val dialog = AddNewStockFragment()
            dialog.setTextChangeListener(this)
            dialog.show(supportFragmentManager, getString(R.string.addnewstock))
        }
        binding.listStocks.layoutManager = LinearLayoutManager(this)
        adapter = StockAdapter(this)
        binding.listStocks.adapter = adapter
        binding.listStocks.itemAnimator = DefaultItemAnimator()
        viewModel.flowState.observe(this, this::onStockFlowState)

        viewModel.startUpdating()
    }

    private fun onStockFlowState(state: StocksViewModel.StockFlowState) {
        when (state) {
            StocksViewModel.StockFlowState.OnLoading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is StocksViewModel.StockFlowState.OnError -> run {
                binding.progressBar.visibility = View.GONE
                val errors = state.error.distinct()
                val message = errors.joinToString("\n") { getString(it ?: R.string.empty) }
                Toasty.error(this, message, Toast.LENGTH_LONG).show()
            }

            is StocksViewModel.StockFlowState.OnSuccess -> {
                binding.progressBar.visibility = View.GONE
                adapter.setStocks(state.stocksCompanies)
            }

            is StocksViewModel.StockFlowState.OnStockRemoved -> {
                adapter.removeStock(state.stockRemoved)
                Toasty.success(this, getString(R.string.stock_removed)).show()
            }

            is StocksViewModel.StockFlowState.OnStockInserted -> {
                adapter.addStock(state.stock)
            }

            is StocksViewModel.StockFlowState.OnElementAlreadyAdded -> {
                Toasty.warning(this, state.error).show()
            }

            is StocksViewModel.StockFlowState.OnElementInvalid -> {
                Toasty.info(this, state.invalidStock).show()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onStockSelected(symbol: String) {
        viewModel.addStock(symbol)
    }

    override fun onLongClick(symbol: StocksDomainModel) {
        showAlertDialog(symbol)
    }

    private fun showAlertDialog(symbol: StocksDomainModel) {
        val builder = AlertDialog.Builder(
            this,
            R.style.AlertDialogTheme
        )
        builder.setMessage(getString(R.string.ask_to_eliminate))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                viewModel.removeStock(symbol)
                dialog.dismiss()
            }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPeriodicUpdates()
    }
}
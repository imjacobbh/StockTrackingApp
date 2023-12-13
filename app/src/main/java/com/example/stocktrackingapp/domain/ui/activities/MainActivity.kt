package com.example.stocktrackingapp.domain.ui.activities

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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
        viewModel.flowState.observe(this, this::onStockFlowState)

        viewModel.startUpdating()
    }

    private fun onStockFlowState(state: StocksViewModel.StockFlowState) {
        when (state) {
            StocksViewModel.StockFlowState.OnLoading -> {

            }

            is StocksViewModel.StockFlowState.OnError -> {

            }

            is StocksViewModel.StockFlowState.OnSuccess -> {
                adapter.setStocks(state.stocksCompanies)
            }

            is StocksViewModel.StockFlowState.OnStockRemoved -> {
                adapter.removeStock(state.stockRemoved)
            }

            is StocksViewModel.StockFlowState.OnStockInserted -> {
                adapter.addStock(state.stock)
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
        viewModel.removeStock(symbol)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopPeriodicUpdates()
    }
}
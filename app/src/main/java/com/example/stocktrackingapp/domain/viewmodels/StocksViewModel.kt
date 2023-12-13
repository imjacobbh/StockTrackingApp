package com.example.stocktrackingapp.domain.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocktrackingapp.domain.interactor.StocksInteractor
import com.example.stocktrackingapp.domain.model.StockResponseFromResult
import com.example.stocktrackingapp.domain.model.StocksDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val interactor: StocksInteractor
) : ViewModel() {


    private val updateInterval = 15000L // Update every 15 seconds
    private val handler = Handler(Looper.getMainLooper())

    fun startUpdating() {
        getUserStocks()
        startPeriodicUpdates()
    }

    fun stopPeriodicUpdates() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun startPeriodicUpdates() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                getUserStocks()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }


    val flowState by lazy { MutableLiveData<StockFlowState>() }
    private var stocksSymbols: ArrayList<String> = ArrayList()

    fun removeStock(stock: StocksDomainModel) {
        stopPeriodicUpdates()
        stocksSymbols.remove(stock.symbol)
        interactor.updateUserStocks(stocksSymbols)
        setFlowState(StockFlowState.OnStockRemoved(stock))
        startUpdating()
    }

    fun addStock(
        stockSymbol: String
    ) {
        stopPeriodicUpdates()
        stocksSymbols.add(stockSymbol)
        interactor.updateUserStocks(stocksSymbols)
        stopPeriodicUpdates()
        setFlowState(StockFlowState.OnStockInserted(StocksDomainModel(stockSymbol)))
        startUpdating()
    }

    fun getUserStocks() {
        viewModelScope.launch {
            setFlowState(StockFlowState.OnLoading)
            val responses = mutableListOf<StockResponseFromResult>()
            stocksSymbols = interactor.getUserStocksSymbols()
            for (symbol in stocksSymbols) {
                val result = interactor.getStocksCompany(symbol)
                val response = interactor.getStockResponseFromResult(result, symbol)
                responses.add(response)
            }
            setFlowState(StockFlowState.OnSuccess(responses.map { it.stock }))
            //  setFlowState(StockFlowState.OnError(responses.map { it.errorOutputStringRes }))

        }
    }

    private fun setFlowState(state: StockFlowState) {
        flowState.value = state
    }

    sealed class StockFlowState {
        data object OnLoading : StockFlowState()
        data class OnSuccess(val stocksCompanies: List<StocksDomainModel>) : StockFlowState()
        data class OnError(val error: List<Int?>) : StockFlowState()
        data class OnStockRemoved(val stockRemoved: StocksDomainModel) : StockFlowState()
        data class OnStockInserted(val stock: StocksDomainModel) : StockFlowState()
    }


}
package com.example.stocktrackingapp.domain.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocktrackingapp.R
import com.example.stocktrackingapp.domain.interactor.StocksInteractor
import com.example.stocktrackingapp.domain.model.StockResponseFromResult
import com.example.stocktrackingapp.domain.model.StocksDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class StocksViewModel @Inject constructor(
    private val interactor: StocksInteractor
) : ViewModel() {

    private var updateJob: Job? = null
    private val updateInterval = 60000L // Update every 60 seconds
    private val handler = Handler(Looper.getMainLooper())

    fun startUpdating() {
        getUserStocks()
        startPeriodicUpdates()
    }

    fun stopPeriodicUpdates() {
        updateJob?.cancel()
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
        startPeriodicUpdates()
    }

    fun addStock(
        stockSymbol: String
    ) {
        if (stocksSymbols.contains(stockSymbol)) {
            setFlowState(StockFlowState.OnElementAlreadyAdded(R.string.stock_already_added_to_your_list))
            return
        }
        if (stockSymbol.isEmpty() || stockSymbol == "Select") {
            setFlowState(StockFlowState.OnElementInvalid(R.string.invalid_stock))
            return
        }
        stopPeriodicUpdates()
        stocksSymbols.add(stockSymbol)
        interactor.updateUserStocks(stocksSymbols)
        stopPeriodicUpdates()
        setFlowState(StockFlowState.OnStockInserted(StocksDomainModel(stockSymbol)))
        startUpdating()
    }

    fun getUserStocks() {
        updateJob = viewModelScope.launch {
            setFlowState(StockFlowState.OnLoading)
            val responses = mutableListOf<StockResponseFromResult>()
            stocksSymbols = interactor.getUserStocksSymbols()
            for (symbol in stocksSymbols) {
                val result = interactor.getStocksCompany(symbol)
                val response = interactor.getStockResponseFromResult(result, symbol)
                responses.add(response)
            }
            setFlowState(StockFlowState.OnSuccess(responses.map { it.stock }))

            val errorResponses = responses.mapNotNull { it.errorOutputStringRes }
            if (errorResponses.isNotEmpty()) {
                setFlowState(StockFlowState.OnError(errorResponses))
            }

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
        data class OnElementAlreadyAdded(val error: Int) : StockFlowState()
        data class OnElementInvalid(val invalidStock: Int) : StockFlowState()
    }


}
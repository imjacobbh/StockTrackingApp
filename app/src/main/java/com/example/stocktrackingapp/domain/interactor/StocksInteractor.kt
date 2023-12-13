package com.example.stocktrackingapp.domain.interactor

import com.example.stocktrackingapp.R
import com.example.stocktrackingapp.data.repository.callback.StockRepository
import com.example.stocktrackingapp.data.repository.exception.NoInternetConnectionException
import com.example.stocktrackingapp.data.repository.model.ProcessedResult
import com.example.stocktrackingapp.data.source.local.models.StockEntity
import com.example.stocktrackingapp.data.source.local.sharedpref.SharedPreferencesManager
import com.example.stocktrackingapp.domain.model.StockResponseFromResult
import com.example.stocktrackingapp.domain.model.StocksDomainModel
import javax.inject.Inject

class StocksInteractor @Inject constructor(
    private val repository: StockRepository,
    private val sharedPreferencesManager: SharedPreferencesManager
) {
    fun getUserStocksSymbols(): ArrayList<String> {
        return sharedPreferencesManager.getStocks()
    }

    fun updateUserStocks(stocksSymbols: java.util.ArrayList<String>) {
        sharedPreferencesManager.saveStocks(stocksSymbols)
    }

    suspend fun getStocksCompany(symbol: String): ProcessedResult<StockEntity> {
        return repository.getStocksCompany(symbol)
    }

    fun getStockResponseFromResult(
        result: ProcessedResult<StockEntity>,
        symbol: String
    ): StockResponseFromResult {
        val stockDomain = result.entity?.let {
            StocksDomainModel(
                symbol = it.symbol,
                stockPrice = it.stockPrice,
                low = it.low,
                high = it.high
            )
        } ?: StocksDomainModel(symbol)
        val errorOutputs = wrapStockResultExceptions(result.exception)
        return StockResponseFromResult(errorOutputs, stockDomain)
    }

    private fun wrapStockResultExceptions(exception: Exception?): Int? = when (exception) {
        is NoInternetConnectionException -> R.string.network_error
        is Exception -> R.string.unexpected_error
        else -> null
    }

}
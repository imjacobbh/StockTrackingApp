package com.example.stocktrackingapp.domain.interactor

import com.example.stocktrackingapp.R
import com.example.stocktrackingapp.data.repository.callback.StockRepository
import com.example.stocktrackingapp.data.repository.exception.NoInternetConnectionException
import com.example.stocktrackingapp.data.repository.model.ProcessedResult
import com.example.stocktrackingapp.data.source.local.models.SearchedStockEntity
import com.example.stocktrackingapp.domain.model.SearchedStocksResponseFromResult
import javax.inject.Inject

class SearchStocksInteractor @Inject constructor(
    private val repository: StockRepository
) {

    suspend fun searchStocksCompany(keyword: String): ProcessedResult<List<SearchedStockEntity>> {
        return repository.searchCompanies(keyword)
    }

    fun getSearchedResponseFromResult(result: ProcessedResult<List<SearchedStockEntity>>): SearchedStocksResponseFromResult {
        val listStocksDomainModel = result.entity?.map {
            it.symbol
        } ?: emptyList()
        val errorOutputs = wrapSearchStocksResultExceptions(result.exception)
        return SearchedStocksResponseFromResult(errorOutputs, listStocksDomainModel)
    }

    private fun wrapSearchStocksResultExceptions(exception: Exception?): Int? = when (exception) {
        is NoInternetConnectionException -> R.string.network_error
        else -> null
    }

}

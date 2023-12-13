package com.example.stocktrackingapp.data.repository.helper

import com.example.stocktrackingapp.data.repository.mapper.StocksMapper
import com.example.stocktrackingapp.data.source.local.models.SearchedStockEntity
import com.example.stocktrackingapp.data.source.local.models.StockEntity
import com.example.stocktrackingapp.data.source.remote.ApiService
import com.example.stocktrackingapp.data.source.remote.models.GlobalQuoteResponseDTO
import com.example.stocktrackingapp.data.source.remote.models.SearchStockDTO
import retrofit2.Response
import javax.inject.Inject

class StocksRepositoryHelper @Inject constructor(
    private val retrofitService: ApiService,
    private val mapper: StocksMapper
) {

    suspend fun getStockCompany(symbol: String, apiKey: String): Response<GlobalQuoteResponseDTO> {
        return retrofitService.getStockQuote(GLOBAL_QUOTE, symbol, apiKey)
    }

    fun getStockEntityFromDto(result: GlobalQuoteResponseDTO): StockEntity {
        return mapper.getStockEntityFromDto(result)
    }

    fun getSearchedStockCompaniesFromDto(result: SearchStockDTO): List<SearchedStockEntity> {
        return mapper.getSearchedStocksFromDto(result)
    }

    suspend fun searchStocksCompany(keyword: String, apiKey: String): Response<SearchStockDTO> {
        return retrofitService.searchStocks(SYMBOL_SEARCH, keyword, apiKey)
    }

    companion object {
        const val GLOBAL_QUOTE = "GLOBAL_QUOTE"
        const val SYMBOL_SEARCH = "SYMBOL_SEARCH"
    }
}
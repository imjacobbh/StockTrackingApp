package com.example.stocktrackingapp.data.repository.callback

import com.example.stocktrackingapp.BuildConfig
import com.example.stocktrackingapp.data.repository.model.ProcessedResult
import com.example.stocktrackingapp.data.source.local.models.SearchedStockEntity
import com.example.stocktrackingapp.data.source.local.models.StockEntity

interface StockRepository {

    suspend fun getStocksCompany(
        symbol: String, apiKey: String = BuildConfig.API_KEY
    ): ProcessedResult<StockEntity>

    suspend fun searchCompanies(
        keyword: String, apiKey: String = BuildConfig.API_KEY
    ): ProcessedResult<List<SearchedStockEntity>>

}
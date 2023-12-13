package com.example.stocktrackingapp.data.source.remote

import com.example.stocktrackingapp.data.source.remote.models.GlobalQuoteResponseDTO
import com.example.stocktrackingapp.data.source.remote.models.SearchStockDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("query")
    suspend fun getStockQuote(
        @Query("function") function: String,
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): Response<GlobalQuoteResponseDTO>

    @GET("query")
    suspend fun searchStocks(
        @Query("function") function: String,
        @Query("keywords") keyword: String,
        @Query("apikey") apiKey: String
    ): Response<SearchStockDTO>
}
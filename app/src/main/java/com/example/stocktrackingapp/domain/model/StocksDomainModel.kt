package com.example.stocktrackingapp.domain.model

data class StocksDomainModel(
    val symbol: String,
    val stockPrice: String = "",
    val low: String = "",
    val high: String = ""
)

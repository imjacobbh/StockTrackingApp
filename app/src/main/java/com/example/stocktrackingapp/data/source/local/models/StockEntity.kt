package com.example.stocktrackingapp.data.source.local.models

data class StockEntity(
    val symbol: String,
    val stockPrice: String,
    val low: String,
    val high: String
)

package com.example.stocktrackingapp.data.source.remote.models

import com.google.gson.annotations.SerializedName

data class SearchStockDTO(
    @SerializedName("bestMatches") val bestMatches: List<StockSearchedDTO>
)

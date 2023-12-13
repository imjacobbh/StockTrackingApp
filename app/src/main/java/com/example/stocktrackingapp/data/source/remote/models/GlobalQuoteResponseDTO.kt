package com.example.stocktrackingapp.data.source.remote.models

import com.google.gson.annotations.SerializedName

data class GlobalQuoteResponseDTO(
    @SerializedName("Global Quote") val globalQuote: StockDTO
)

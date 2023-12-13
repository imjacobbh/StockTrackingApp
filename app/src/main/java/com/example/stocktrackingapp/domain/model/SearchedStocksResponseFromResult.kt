package com.example.stocktrackingapp.domain.model

import androidx.annotation.StringRes

data class SearchedStocksResponseFromResult(
    @StringRes
    val errorOutputStringRes: Int?,
    val companies: List<String>
)


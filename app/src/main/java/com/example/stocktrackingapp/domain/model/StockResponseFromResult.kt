package com.example.stocktrackingapp.domain.model

import androidx.annotation.StringRes

data class StockResponseFromResult(
    @StringRes
    val errorOutputStringRes: Int?,
    val stock: StocksDomainModel
)
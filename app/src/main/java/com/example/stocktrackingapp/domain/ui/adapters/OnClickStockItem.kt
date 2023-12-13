package com.example.stocktrackingapp.domain.ui.adapters

import com.example.stocktrackingapp.domain.model.StocksDomainModel

interface OnClickStockItem {
    fun onLongClick(symbol: StocksDomainModel)
}
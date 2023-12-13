package com.example.stocktrackingapp.data.repository.mapper

import com.example.stocktrackingapp.data.source.local.models.SearchedStockEntity
import com.example.stocktrackingapp.data.source.local.models.StockEntity
import com.example.stocktrackingapp.data.source.remote.models.GlobalQuoteResponseDTO
import com.example.stocktrackingapp.data.source.remote.models.SearchStockDTO

class StocksMapper {

    fun getStockEntityFromDto(result: GlobalQuoteResponseDTO): StockEntity {
        return with(result.globalQuote) {
            StockEntity(
                symbol = symbol,
                low = low,
                stockPrice = price,
                high = high
            )
        }
    }

    fun getSearchedStocksFromDto(result: SearchStockDTO): List<SearchedStockEntity> {
        return result.bestMatches.map {
            SearchedStockEntity(
                name = it.name,
                symbol = it.symbol
            )
        }
    }
}
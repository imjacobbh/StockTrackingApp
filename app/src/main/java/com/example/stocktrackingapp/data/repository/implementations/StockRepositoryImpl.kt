package com.example.stocktrackingapp.data.repository.implementations

import com.example.stocktrackingapp.data.repository.CacheManager
import com.example.stocktrackingapp.data.repository.callback.StockRepository
import com.example.stocktrackingapp.data.repository.helper.StocksRepositoryHelper
import com.example.stocktrackingapp.data.repository.model.ProcessedResult
import com.example.stocktrackingapp.data.source.local.models.SearchedStockEntity
import com.example.stocktrackingapp.data.source.local.models.StockEntity
import com.example.stocktrackingapp.data.source.remote.models.GlobalQuoteResponseDTO
import com.example.stocktrackingapp.data.source.remote.models.SearchStockDTO
import com.example.stocktrackingapp.data.source.utils.HttpConnectionUtil
import com.example.stocktrackingapp.data.source.utils.NetworkStatusUtil
import retrofit2.Response
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val networkUtil: NetworkStatusUtil,
    private val httpConnectionUtil: HttpConnectionUtil,
    private val helper: StocksRepositoryHelper
) : StockRepository {

    override suspend fun getStocksCompany(
        symbol: String,
        apiKey: String
    ): ProcessedResult<StockEntity> {
        val cacheManager =
            object :
                CacheManager<GlobalQuoteResponseDTO, StockEntity>(networkUtil, httpConnectionUtil) {
                override suspend fun getDto(): Response<GlobalQuoteResponseDTO> {
                    return helper.getStockCompany(symbol, apiKey)
                }

                override fun isValidDTO(result: GlobalQuoteResponseDTO): Boolean {
                    return result.globalQuote != null
                }

                override suspend fun getEntity(result: GlobalQuoteResponseDTO): StockEntity {
                    return helper.getStockEntityFromDto(result)
                }

            }
        return cacheManager.retrieveResult()
    }

    override suspend fun searchCompanies(
        keyword: String,
        apiKey: String
    ): ProcessedResult<List<SearchedStockEntity>> {
        val cacheManager =
            object :
                CacheManager<SearchStockDTO, List<SearchedStockEntity>>(
                    networkUtil,
                    httpConnectionUtil
                ) {
                override suspend fun getDto(): Response<SearchStockDTO> {
                    return helper.searchStocksCompany(keyword, apiKey)
                }

                override fun isValidDTO(result: SearchStockDTO): Boolean {
                    return result.bestMatches != null
                }

                override suspend fun getEntity(result: SearchStockDTO): List<SearchedStockEntity> {
                    return helper.getSearchedStockCompaniesFromDto(result)
                }

            }
        return cacheManager.retrieveResult()
    }
}
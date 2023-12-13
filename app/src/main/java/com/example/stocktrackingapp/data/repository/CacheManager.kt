package com.example.stocktrackingapp.data.repository

import android.util.Log
import com.example.stocktrackingapp.data.repository.exception.InvalidDtoException
import com.example.stocktrackingapp.data.repository.exception.NoInternetConnectionException
import com.example.stocktrackingapp.data.repository.model.ProcessedResult
import com.example.stocktrackingapp.data.source.utils.HttpConnectionUtil
import com.example.stocktrackingapp.data.source.utils.NetworkStatusUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


internal abstract class CacheManager<DTO, Entity>(
    private val networkUtil: NetworkStatusUtil,
    private val httpConnectionUtil: HttpConnectionUtil
) {

    private fun verifyExceptionResultFromDto(result: Response<*>) =
        httpConnectionUtil.verifyExceptionResultFromDto(result)


    suspend fun retrieveResult(): ProcessedResult<Entity> {
        return callApi()
    }

    open suspend fun callApi(): ProcessedResult<Entity> = withContext(Dispatchers.IO) {
        if (isNetworkNotConnected()) {
            Log.e("CacheManagerException", "NoInternetConnectionException")
            getCacheDataOnException(NoInternetConnectionException())
        } else {
            fetchDto()?.let { response ->
                onApiCallSuccess(response)
            } ?: run {
                Log.e("CacheManagerException", "InvalidDtoException: null DTO")
                getCacheDataOnException(InvalidDtoException())
            }
        }
    }

    private suspend fun fetchDto(): Response<DTO>? {
        return try {
            getDto()
        } catch (t: Throwable) {
            Log.e("CacheManager", "OnError getting API entity: ${t.localizedMessage}")
            null
        }
    }

    private suspend fun onApiCallSuccess(response: Response<DTO>): ProcessedResult<Entity> =
        withContext(Dispatchers.IO) {
            val exception = verifyExceptionResultFromDto(response)
            if (exception == null) {
                getEntityWithValidResponse(response)
            } else {
                Log.e("CacheManager", "OnError $exception")
                getCacheDataOnException(exception)
            }
        }

    private suspend fun getEntityFromDto(body: DTO): ProcessedResult<Entity> =
        withContext(Dispatchers.IO) {
            val entity = getEntity(body)
            ProcessedResult(entity = entity)
        }

    @Throws(Throwable::class)
    abstract suspend fun getDto(): Response<DTO>

    //used to map to local entity
    @Throws(Throwable::class)
    abstract suspend fun getEntity(result: DTO): Entity?

    abstract fun isValidDTO(result: DTO): Boolean

    private fun isNetworkConnected() = networkUtil.isNetworkAvailable()

    private fun isNetworkNotConnected() = !isNetworkConnected()

    private fun getCacheDataOnException(e: Exception): ProcessedResult<Entity> =
        ProcessedResult(exception = e)

    private fun isResponseValid(response: Response<DTO>): Boolean {
        val body = response.body()
        Log.d("CACHE", body.toString())
        return (body != null && isValidDTO(body))
    }

    private suspend fun getEntityIfNotNullOrReturnException(dto: DTO?): ProcessedResult<Entity> {
        return if (dto != null) {
            getEntityFromDto(dto)
        } else {
            getCacheDataOnException(InvalidDtoException())
        }
    }

    private suspend fun getEntityWithValidResponse(response: Response<DTO>): ProcessedResult<Entity> =
        if (isResponseValid(response)) {
            getEntityIfNotNullOrReturnException(response.body())
        } else {
            Log.e("CacheManager", "OnError InvalidDtoException")
            getCacheDataOnException(InvalidDtoException())
        }
}
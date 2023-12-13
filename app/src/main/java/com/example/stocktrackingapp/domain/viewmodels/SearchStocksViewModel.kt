package com.example.stocktrackingapp.domain.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stocktrackingapp.data.repository.model.ProcessedResult
import com.example.stocktrackingapp.data.source.local.models.SearchedStockEntity
import com.example.stocktrackingapp.domain.interactor.SearchStocksInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchStocksViewModel @Inject constructor(
    private val interactor: SearchStocksInteractor
) : ViewModel() {

    val flowState by lazy { MutableLiveData<SearchStockFlowState>() }
    private var searchJob: Job? = null

    fun searchStocksCompany(keyword: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            setFlowState(SearchStockFlowState.OnLoading)
            val result = interactor.searchStocksCompany(keyword)
            setStateWithSearchResult(result)
        }
    }

    private fun setStateWithSearchResult(result: ProcessedResult<List<SearchedStockEntity>>) {
        val response = interactor.getSearchedResponseFromResult(result)
        setFlowState(SearchStockFlowState.OnSuccess(response.companies))
        response.errorOutputStringRes?.let {
            setFlowState(SearchStockFlowState.OnError(it))
        }

    }

    private fun setFlowState(state: SearchStockFlowState) {
        flowState.value = state
    }

    sealed class SearchStockFlowState {
        data object OnLoading : SearchStockFlowState()
        data class OnSuccess(val stocksCompanies: List<String>) : SearchStockFlowState()
        data class OnError(val error: Int) : SearchStockFlowState()
    }
}
package com.example.stocktrackingapp.data.repository.model

import java.lang.Exception

data class ProcessedResult<Result>(
    var exception: Exception? = null,
    var entity: Result? = null
)
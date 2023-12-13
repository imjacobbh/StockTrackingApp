package com.example.stocktrackingapp.data.source.utils

import com.example.stocktrackingapp.data.repository.exception.BadRequestException
import com.example.stocktrackingapp.data.repository.exception.ConflictException
import com.example.stocktrackingapp.data.repository.exception.ForbiddenException
import com.example.stocktrackingapp.data.repository.exception.NotFoundException
import com.example.stocktrackingapp.data.repository.exception.UnAuthorizedException
import com.example.stocktrackingapp.data.repository.exception.UnProcessableEntityException
import retrofit2.Response
import java.lang.Exception

class HttpConnectionUtil {
    fun <DTO> verifyExceptionResultFromDto(dto: DTO): Exception? {
        val mDto = dto as Response<*>
        return when (mDto.code()) {
            in 200..299 -> null
            400 -> BadRequestException()
            401 -> UnAuthorizedException()
            403 -> ForbiddenException()
            404 -> NotFoundException()
            409 -> ConflictException()
            422 -> UnProcessableEntityException()
            else -> Exception()
        }
    }
}
package com.szymanski.sharelibrary.core.exception

import org.json.JSONObject
import retrofit2.HttpException


class ErrorWrapperImpl(
) : ErrorWrapper {

    override fun wrap(throwable: Throwable): Throwable {
        return when (throwable) {
            is HttpException -> wrapServerError(throwable)
            else -> throwable
        }
    }

    private fun wrapServerError(httpException: HttpException): Throwable {
        return with(httpException) {
            val errorMessage =
                JSONObject((response()!!.errorBody())!!.string()).get("message").toString()
            when (code()) {
                500 -> ServerException.Internal(errorMessage)
                302 -> ServerException.Found(errorMessage)
                404 -> ServerException.NotFound(errorMessage)
                403 -> ServerException.Forbidden(errorMessage)
                else -> ServerException.Unknown(errorMessage)
            }
        }
    }
}

suspend fun <T> callOrThrow(
    errorWrapper: ErrorWrapper,
    apiCall: suspend () -> T,
): T {

    return runCatching {
        apiCall()
    }
        .getOrElse {
            throw errorWrapper.wrap(it)
        }
}

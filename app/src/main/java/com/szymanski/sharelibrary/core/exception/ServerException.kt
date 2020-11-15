package com.szymanski.sharelibrary.core.exception

sealed class ServerException(message: String?) : Throwable(message) {

    class Internal(message: String?) : ServerException(message)
    class BadRequest(message: String?) : ServerException(message)
    class Unknown(message: String?) : ServerException(message)
    class Found(message: String?) : ServerException(message)
    class NotFound(message: String?) : ServerException(message)
    class Forbidden(message: String?) : ServerException(message)

}

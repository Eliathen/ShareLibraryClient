package com.szymanski.sharelibrary.core.exception

import android.content.Context
import androidx.annotation.StringRes
import com.szymanski.sharelibrary.R
import java.net.ConnectException

class ErrorMapperImpl(private val context: Context) : ErrorMapper {

    override fun map(throwable: Throwable): String {
        return when (throwable) {
            is ServerException -> mapServerException(throwable)
            is ConnectException -> getMessage(R.string.error_connect)
            else -> getMessage(R.string.error_unknown)
        }
    }

    private fun mapServerException(serverException: ServerException): String {
        return serverException.message.toString()
    }

    private fun getMessage(@StringRes stringRes: Int) = context.getString(stringRes)
}
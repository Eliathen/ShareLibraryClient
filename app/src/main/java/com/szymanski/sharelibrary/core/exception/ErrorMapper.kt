package com.szymanski.sharelibrary.core.exception

interface ErrorMapper {

    fun map(throwable: Throwable): String
}
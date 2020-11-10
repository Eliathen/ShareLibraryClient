package com.szymanski.sharelibrary.core.exceptions

interface ErrorMapper {

    fun map(throwable: Throwable): String
}
package com.szymanski.sharelibrary.core.exception

interface ErrorWrapper {

    fun wrap(throwable: Throwable): Throwable

}
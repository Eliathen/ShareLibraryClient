package com.szymanski.sharelibrary.features.book.domain.model

import java.io.Serializable

data class Book(

    val title: String?,

    val authors: List<Author>?,

    val image: ByteArray?,
) : Serializable {

}
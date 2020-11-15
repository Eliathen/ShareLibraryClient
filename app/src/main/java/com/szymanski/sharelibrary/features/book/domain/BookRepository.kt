package com.szymanski.sharelibrary.features.book.domain

import com.szymanski.sharelibrary.features.book.domain.model.Book

interface BookRepository {

    suspend fun saveBook(book: Book)
}
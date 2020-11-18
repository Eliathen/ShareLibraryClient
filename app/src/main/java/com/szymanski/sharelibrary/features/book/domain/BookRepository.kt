package com.szymanski.sharelibrary.features.book.domain

import com.szymanski.sharelibrary.features.book.domain.model.Book

interface BookRepository {

    suspend fun saveBook(book: Book, userId: Long)
    suspend fun getUsersBook(userId: Long): List<Book>
    suspend fun searchBooks(query: String): List<Book>
}
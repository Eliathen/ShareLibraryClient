package com.szymanski.sharelibrary.features.book.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.model.Author
import com.szymanski.sharelibrary.features.book.domain.model.Book
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


class BookRepositoryImpl(
    private val api: Api,
    private val errorWrapper: ErrorWrapper,
    private val userStorage: UserStorage,
) : BookRepository {
    override suspend fun saveBook(book: Book, userId: Long) {
        val image = MultipartBody.Part.createFormData(
            "image", "myPic", book.cover!!
                .toRequestBody("image/*".toMediaTypeOrNull(),
                    0, book.cover.size)
        )
        val map = createMapOfRequestBodyFromAuthorList(book.authors!!.toList())
        callOrThrow(errorWrapper) {
            api.saveBook(book.title!!, image, map, userId)
        }
    }

    private fun createMapOfRequestBodyFromAuthorList(authors: List<Author>): Map<String, RequestBody> {
        val map = hashMapOf<String, RequestBody>()
        val mT = ("text/plain".toMediaTypeOrNull())
        for (index in authors.indices) {
            val name = authors[index].name.let { it!!.toRequestBody(mT) }
            val surname = authors[index].surname.let { it!!.toRequestBody(mT) }
            map["authors[$index].name"] = name
            map["authors[$index].surname"] = surname
        }
        return map
    }

    override suspend fun getUsersBook(userId: Long): List<Book> {
        return callOrThrow(errorWrapper) {
            api.getUsersBook(userId).map { it.toBook() }.toList()
        }
    }

    override suspend fun searchBooks(query: String): List<Book> {
        return callOrThrow(errorWrapper) {
            api.searchBooks(query).map { it.toBook() }.toList()
        }
    }
}
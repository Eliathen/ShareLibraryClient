package com.szymanski.sharelibrary.features.book.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.response.BookResponse
import com.szymanski.sharelibrary.core.api.model.response.LanguageResponse
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class BookRepositoryImplTest {


//    @Test
//    fun `WHEN request save book THEN call save book method from API `() {
//        //given
//        val book = Book.mock()
//        val userId = 1L
//        val image = mockk<MultipartBody.Part>(relaxed = true)
//        val api = mockk<Api> {
//            coEvery { saveBook(any(),
//                any(),
//                any(),
//                any(), any(), any(), any()) } returns Unit
//        }
//        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
//        val repository = BookRepositoryImpl(api, errorWrapper)
//        //when
//        runBlocking {
//            repository.saveBook(book, userId)
//        }
//        //then
//        coVerify {
//            api.saveBook(
//                book.title!!,
//                image,
//                mapOf<String, RequestBody>(),
//                mapOf<String, RequestBody>(),
//                userId,
//                mapOf<String, RequestBody>(),
//                book.condition?.ordinal!!
//                )
//        }
//    }


    @Test
    fun `WHEN request get users book THEN call get users book method from API `() {
        val userId = 1L
        val api = mockk<Api> {
            coEvery { getUsersBook(userId) } returns listOf(BookResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = BookRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getUsersBook(userId)
        }
        //then
        coVerify {
            api.getUsersBook(userId)
        }
    }

    @Test
    fun `WHEN request search books THEN call search books method from API `() {
        val query = "query"
        val api = mockk<Api> {
            coEvery { searchBooks(query) } returns listOf(BookResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = BookRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.searchBooks(query)
        }
        //then
        coVerify {
            api.searchBooks(query)
        }
    }

    @Test
    fun `WHEN request get get languages THEN call get languages method from API `() {
        val api = mockk<Api> {
            coEvery { getLanguageList() } returns listOf(LanguageResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = BookRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getLanguages()
        }
        //then
        coVerify {
            api.getLanguageList()
        }
    }
}
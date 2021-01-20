package com.szymanski.sharelibrary.features.book.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.response.CategoryResponse
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class CategoryRepositoryImplTest {


    @Test
    fun `WHEN category request get categories THEN fetch categories from API `() {
        //given
        val api = mockk<Api> {
            coEvery { getCategories() } returns listOf(CategoryResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = CategoryRepositoryImpl(errorWrapper, api)
        //when
        runBlocking {
            repository.getAll()
        }
        //then
        coVerify {
            api.getCategories()
        }
    }
}
package com.szymanski.sharelibrary.features.book.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.callOrThrow
import com.szymanski.sharelibrary.features.book.domain.CategoryRepository
import com.szymanski.sharelibrary.features.book.domain.model.Category

class CategoryRepositoryImpl(
    private val errorWrapper: ErrorWrapper,
    private val api: Api,
) : CategoryRepository {

    override suspend fun getAll(): List<Category> {
        return callOrThrow(errorWrapper) { api.getCategories().map { it.toCategory() } }
    }
}
package com.szymanski.sharelibrary.features.book.domain

import com.szymanski.sharelibrary.features.book.domain.model.Category

interface CategoryRepository {

    suspend fun getAll(): List<Category>

}

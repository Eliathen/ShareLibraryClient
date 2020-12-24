package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.book.domain.CategoryRepository
import com.szymanski.sharelibrary.features.book.domain.model.Category

class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository,
) : BaseUseCase<List<Category>, Unit>() {

    override suspend fun action(params: Unit): List<Category> {
        return categoryRepository.getAll()
    }
}
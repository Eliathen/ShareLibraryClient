package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.features.book.domain.CategoryRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetCategoriesUseCaseTest {

    @Test
    fun `when use case is invoked than execute getAll method from repository`() {
        //given
        val repository = mockk<CategoryRepository>(relaxed = true)
        val useCase = GetCategoriesUseCase(repository)
        //when
        useCase(
            params = Unit,
            scope = GlobalScope
        )
        //then
        coVerify { repository.getAll() }
    }
}
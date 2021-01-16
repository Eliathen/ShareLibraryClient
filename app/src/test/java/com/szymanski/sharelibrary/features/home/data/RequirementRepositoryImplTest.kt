package com.szymanski.sharelibrary.features.home.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.CreateRequirementRequest
import com.szymanski.sharelibrary.core.api.model.response.RequirementResponse
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class RequirementRepositoryImplTest {


    @Test
    fun `WHEN request create requirement THEN fetch created requirement from API `() {
        //given
        val api = mockk<Api> {
            coEvery { createRequirement(CreateRequirementRequest.mock()) } returns RequirementResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = RequirementRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.createRequirement(1L, 1L)
        }
        //then
        coVerify {
            api.createRequirement(CreateRequirementRequest.mock())
        }
    }

    @Test
    fun `WHEN request get requirements by exchange id THEN fetch requirements from API `() {
        //given
        val exchangeId = 1L
        val api = mockk<Api> {
            coEvery { getRequirementByExchangeId(exchangeId) } returns listOf(RequirementResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = RequirementRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getRequirementsByExchangeId(exchangeId)
        }
        //then
        coVerify {
            api.getRequirementByExchangeId(exchangeId)
        }
    }

    @Test
    fun `WHEN request get user requirements THEN fetch user requirement from API `() {
        //given
        val userId = 1L
        val api = mockk<Api> {
            coEvery { getUserRequirements(userId) } returns listOf(RequirementResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = RequirementRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getUserRequirements(userId)
        }
        //then
        coVerify {
            api.getUserRequirements(userId)
        }
    }
}
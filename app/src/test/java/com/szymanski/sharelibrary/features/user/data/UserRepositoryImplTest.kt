package com.szymanski.sharelibrary.features.user.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.request.*
import com.szymanski.sharelibrary.core.api.model.response.LoginResponse
import com.szymanski.sharelibrary.core.api.model.response.RegisterResponse
import com.szymanski.sharelibrary.core.api.model.response.UserResponse
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class UserRepositoryImplTest {

    @Test
    fun `WHEN request register user THEN fetch created user from API `() {
        //given
        val api = mockk<Api> {
            coEvery { registerUser(RegisterRequest.mock()) } returns RegisterResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = UserRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.registerUser(User.mock())
        }
        //then
        coVerify {
            api.registerUser(RegisterRequest.mock())
        }
    }

    @Test
    fun `WHEN request login user THEN fetch login from API `() {
        //given
        val api = mockk<Api> {
            coEvery { login(LoginRequest.mock()) } returns LoginResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = UserRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.login(Login.mock())
        }
        //then
        coVerify {
            api.login(LoginRequest.mock())
        }
    }

    @Test
    fun `WHEN request get user by id THEN fetch user from API `() {
        //given
        val userId = 1L
        val api = mockk<Api> {
            coEvery { getUser(userId) } returns UserResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = UserRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getUser(userId)
        }
        //then
        coVerify {
            api.getUser(userId)
        }
    }

    @Test
    fun `WHEN request edit THEN fetch user from API `() {
        //given
        val userId = 1L
        val api = mockk<Api> {
            coEvery { editUser(userId, UserRequest.mock()) } returns UserResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = UserRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.editUser(User.mock())
        }
        //then
        coVerify {
            api.editUser(userId, UserRequest.mock())
        }
    }

    @Test
    fun `WHEN request assign book to user THEN fetch user from API `() {
        //given
        val userId = 1L
        val request = AssignBookRequest.mock()
        val api = mockk<Api> {
            coEvery { assignBook(request) } returns UserResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = UserRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.assignBook(request)
        }
        //then
        coVerify {
            api.assignBook(request)
        }
    }

    @Test
    fun `WHEN request withdraw book from user THEN fetch user from API `() {
        //given
        val withdrawRequest = WithdrawBookRequest.mock()
        val api = mockk<Api> {
            coEvery { withdrawBook(withdrawRequest) } returns UserResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = UserRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.withdrawBook(withdrawRequest)
        }
        //then
        coVerify {
            api.withdrawBook(withdrawRequest)
        }
    }

    @Test
    fun `WHEN request get user by books where at user is THEN fetch users from API `() {
        //given
        val userId = 1L
        val api = mockk<Api> {
            coEvery { getUsersByBooksWhereAtUserIdIs(userId) } returns listOf(UserResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = UserRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getUsersByBooksWhereAtUserIdIs(userId)
        }
        //then
        coVerify {
            api.getUsersByBooksWhereAtUserIdIs(userId)
        }
    }
}
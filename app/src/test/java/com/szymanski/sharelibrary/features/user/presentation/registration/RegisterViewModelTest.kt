package com.szymanski.sharelibrary.features.user.presentation.registration

import androidx.lifecycle.Observer
import com.szymanski.sharelibrary.core.base.UiState
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorageImpl
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.LoginUserUseCase
import com.szymanski.sharelibrary.features.user.domain.usecase.RegisterUserUseCase
import com.szymanski.sharelibrary.features.user.navigation.UserNavigationImpl
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable
import com.szymanski.sharelibrary.mock.mock
import com.szymanski.sharelibrary.utils.ViewModelTest
import com.szymanski.sharelibrary.utils.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.Test

internal class RegisterViewModelTest : ViewModelTest() {

    @Test
    fun `WHEN registerUserUseCase is success THEN set idle state`() {
        //given
        val user = User.mock()
        val registerUseCase = mockk<RegisterUserUseCase> {
            every {
                this@mockk(any(), any(), any(), any())
            } answers {
                lastArg<(Result<User>) -> User>()(Result.success(user))
            }
        }
        val loginUserUseCase = mockk<LoginUserUseCase>(relaxed = true)
        val userNavigation = mockk<UserNavigationImpl>(relaxed = true)
        val userStorage = mockk<UserStorageImpl>(relaxed = true)
        val errorMapper = mockk<ErrorMapper>(relaxed = true)
        val viewModel = RegisterViewModel(registerUseCase,
            loginUserUseCase, userNavigation, userStorage, errorMapper)
        //when
        viewModel.registerUser(UserDisplayable(user))
        //then
        viewModel.uiState.getOrAwaitValue() shouldBe UiState.Idle
    }

    @Test
    fun `WHEN registerUserUseCase is failure THEN set idle state AND set error message in live data`() {
        //given
        val user = User.mock()
        val throwable = Throwable("")
        val registerUseCase = mockk<RegisterUserUseCase> {
            every {
                this@mockk(any(), any(), any(), any())
            } answers {
                lastArg<(Result<User>) -> User>()(Result.failure(throwable))
            }
        }
        val loginUserUseCase = mockk<LoginUserUseCase>(relaxed = true)
        val userNavigation = mockk<UserNavigationImpl>(relaxed = true)
        val userStorage = mockk<UserStorageImpl>(relaxed = true)
        val errorMapper = mockk<ErrorMapper>(relaxed = true)
        val viewModel = RegisterViewModel(registerUseCase,
            loginUserUseCase, userNavigation, userStorage, errorMapper)
        val observer = mockk<Observer<String>>(relaxed = true)
        //when
        viewModel.message.observeForever(observer)
        viewModel.registerUser(UserDisplayable(user))
        //then
        viewModel.uiState.getOrAwaitValue() shouldBe UiState.Idle
        verify { observer.onChanged(throwable.message) }
    }

}
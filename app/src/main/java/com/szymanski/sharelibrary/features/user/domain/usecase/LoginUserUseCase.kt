package com.szymanski.sharelibrary.features.user.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.Login

class LoginUserUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<Login, Login>() {

    override suspend fun action(params: Login): Login {
        return userRepository.login(params)
    }
}
package com.szymanski.sharelibrary.features.users.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.users.domain.UserRepository
import com.szymanski.sharelibrary.features.users.domain.model.Login
import com.szymanski.sharelibrary.features.users.domain.model.Token

class LoginUserUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<Token, Login>() {

    override suspend fun action(params: Login): Token {
        return userRepository.login(params).token!!
    }
}
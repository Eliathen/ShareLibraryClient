package com.szymanski.sharelibrary.features.users.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.users.domain.UserRepository
import com.szymanski.sharelibrary.features.users.domain.model.User

class RegisterUserUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<Unit, User>() {

    override suspend fun action(params: User) {
        userRepository.registerUser(params)
    }
}
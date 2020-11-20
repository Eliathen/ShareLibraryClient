package com.szymanski.sharelibrary.features.user.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.User

class GetUserUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<User, Long>() {
    override suspend fun action(params: Long): User {
        return userRepository.getUser(params)
    }
}
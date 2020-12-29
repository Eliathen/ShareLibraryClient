package com.szymanski.sharelibrary.features.user.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.User

class EditUserUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<User, User>() {
    override suspend fun action(params: User): User {
        return userRepository.editUser(params)
    }
}
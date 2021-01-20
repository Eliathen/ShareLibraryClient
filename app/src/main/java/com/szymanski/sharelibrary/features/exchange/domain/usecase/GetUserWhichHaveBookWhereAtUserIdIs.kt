package com.szymanski.sharelibrary.features.exchange.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.User

class GetUserWhichHaveBookWhereAtUserIdIs(
    private val userRepository: UserRepository,
) : BaseUseCase<List<User>, Long>() {
    override suspend fun action(params: Long): List<User> {
        return userRepository.getUsersByBooksWhereAtUserIdIs(params)
    }
}
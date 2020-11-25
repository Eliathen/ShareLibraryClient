package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.api.model.request.WithdrawBookRequest
import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.User

class WithdrawBookUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<User, WithdrawBookRequest>() {
    override suspend fun action(params: WithdrawBookRequest): User {
        return userRepository.withdrawBook(params)
    }

}

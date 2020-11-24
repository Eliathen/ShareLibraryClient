package com.szymanski.sharelibrary.features.book.domain.usecase

import android.util.Log
import com.szymanski.sharelibrary.core.api.model.request.WithdrawBookRequest
import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.User

class WithdrawBookUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<User, WithdrawBookRequest>() {
    private val TAG = "WithdrawBookUseCase"
    override suspend fun action(params: WithdrawBookRequest): User {
        Log.d(TAG, "action: inUseCase ")
        return userRepository.withdrawBook(params)
    }

}

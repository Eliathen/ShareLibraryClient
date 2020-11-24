package com.szymanski.sharelibrary.features.book.domain.usecase

import com.szymanski.sharelibrary.core.api.model.request.AssignBookRequest
import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.user.domain.UserRepository
import com.szymanski.sharelibrary.features.user.domain.model.User

class AssignBookToUserUseCase(
    private val userRepository: UserRepository,
) : BaseUseCase<User, AssignBookRequest>() {

    override suspend fun action(params: AssignBookRequest): User {
        return userRepository.assignBook(params)
    }

}

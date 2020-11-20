package com.szymanski.sharelibrary.core.api.model

import com.szymanski.sharelibrary.features.user.domain.model.User

class UserResponse(
    //TODO add user properties
) {
    fun toUser() = User(
        null, null, null, null, null, null, null
    )
}

package com.szymanski.sharelibrary.features.user.registration.presentation.model

import com.szymanski.sharelibrary.features.user.domain.model.User

data class UserDisplayable(
    val id: Long?,
    val email: String?,
    val username: String?,
    val password: CharArray?,
    val name: String?,
    val surname: String?,
    val address: AddressDisplayable?,
) {

    constructor(user: User) : this(
        id = user.id,
        email = user.email,
        username = user.username,
        password = user.password,
        name = user.name,
        surname = user.surname,
        address = user.address?.let { AddressDisplayable(it) }
    )

    fun toUser() = User(
        id = this.id,
        email = this.email,
        username = this.username,
        password = this.password,
        name = this.name,
        surname = this.surname,
        address = this.address?.toAddress()
    )


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserDisplayable

        if (email != other.email) return false
        if (username != other.username) return false
        if (!password.contentEquals(other.password)) return false
        if (name != other.name) return false
        if (surname != other.surname) return false
        if (address != other.address) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + surname.hashCode()
        result = 31 * result + address.hashCode()
        return result
    }

}

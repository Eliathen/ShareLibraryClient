package com.szymanski.sharelibrary.features.users.domain.model

data class User(
    val id: Long?,
    val email: String?,
    val username: String?,
    val password: CharArray?,
    val name: String?,
    val surname: String?,
    val address: Address?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

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

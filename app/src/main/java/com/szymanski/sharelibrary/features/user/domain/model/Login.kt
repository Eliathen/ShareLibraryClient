package com.szymanski.sharelibrary.features.user.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Login(
    val id: Long?,
    val userNameOrEmail: String?,
    val password: CharArray?,
    val token: Token?,
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Login

        if (id != other.id) return false
        if (userNameOrEmail != other.userNameOrEmail) return false
        if (password != null) {
            if (other.password == null) return false
            if (!password.contentEquals(other.password)) return false
        } else if (other.password != null) return false
        if (token != other.token) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (userNameOrEmail?.hashCode() ?: 0)
        result = 31 * result + (password?.contentHashCode() ?: 0)
        result = 31 * result + (token?.hashCode() ?: 0)
        return result
    }
}
package com.szymanski.sharelibrary.features.user.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Login(
    val id: Long?,
    val userNameOrEmail: String?,
    val password: CharArray?,
    val token: Token?,
) : Parcelable
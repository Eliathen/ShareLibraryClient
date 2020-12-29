package com.szymanski.sharelibrary.features.user.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Token(
    val accessToken: String?,
    val tokenType: String?,
) : Parcelable {
    companion object
}
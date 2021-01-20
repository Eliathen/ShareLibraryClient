package com.szymanski.sharelibrary.features.book.presentation.model

import android.os.Parcelable
import com.szymanski.sharelibrary.features.book.domain.model.Language
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LanguageDisplayable(
    val name: String?,
    val id: Int?,
) : Parcelable {
    fun toLanguage() = Language(
        name = this.name,
        id = this.id
    )

    constructor(language: Language) : this(
        name = language.name,
        id = language.id
    )

    companion object
}
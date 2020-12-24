package com.szymanski.sharelibrary.features.book.presentation.model

import android.os.Parcelable
import com.szymanski.sharelibrary.features.book.domain.model.Category
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryDisplayable(
    val id: Int,
    val name: String,
) : Parcelable {
    constructor(category: Category) : this(
        id = category.id,
        name = category.name
    )

    fun toCategory() = Category(
        id = this.id,
        name = this.name
    )
}
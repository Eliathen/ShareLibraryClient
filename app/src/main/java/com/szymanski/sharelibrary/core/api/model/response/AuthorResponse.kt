package com.szymanski.sharelibrary.core.api.model.response

import com.google.gson.annotations.SerializedName
import com.szymanski.sharelibrary.features.book.domain.model.Author

class AuthorResponse(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("surname") val surname: String?,
) {
    fun toAuthor() = Author(
        id = this.id,
        name = this.name,
        surname = this.surname
    )
}


package com.szymanski.sharelibrary.core.helpers

import com.szymanski.sharelibrary.features.book.presentation.model.AuthorDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.CategoryDisplayable


fun convertAuthorDisplayableListToString(list: List<AuthorDisplayable>): String {
    var endString = ""
    list.forEach { author ->
        endString += "${author.name} ${author.surname}, "
    }
    endString = endString.trim()
    return endString.substring(0, endString.length - 1)
}

fun convertCategoriesDisplayableListToString(list: List<CategoryDisplayable>): String {
    var endString = ""
    list.forEach { category ->
        endString += "${category.name}, "
    }
    endString = endString.trim()
    return endString.substring(0, endString.length - 1)
}

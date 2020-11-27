package com.szymanski.sharelibrary.features.book.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable

class BookDetailsViewModel(private val bookNavigator: BookNavigator) : BaseViewModel() {

    private val _book: MutableLiveData<BookDisplayable> by lazy {
        MutableLiveData()
    }

    val book: LiveData<BookDisplayable> by lazy {
        _book
    }

    fun setBook(bookDisplayable: BookDisplayable) {
        _book.value = bookDisplayable
    }

}
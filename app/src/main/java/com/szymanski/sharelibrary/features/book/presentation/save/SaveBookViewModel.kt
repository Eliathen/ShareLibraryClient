package com.szymanski.sharelibrary.features.book.presentation.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.model.Category
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCategoriesUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.SaveBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.book.presentation.model.CategoryDisplayable

class SaveBookViewModel(
    errorMapper: ErrorMapper,
    private val bookNavigator: BookNavigator,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val saveBookUseCase: SaveBookUseCase,
) : BaseViewModel(errorMapper) {

    private val _book: MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }

    val book: LiveData<BookDisplayable> by lazy {
        _book.map {
            BookDisplayable(it)
        }
    }
    private val _categories: MutableLiveData<List<Category>> by lazy {
        MutableLiveData<List<Category>>().also {
            getCategories()
        }
    }

    val chosenCategories: MutableList<Boolean> by lazy {
        createList(_categories.value!!)
    }

    private fun createList(value: List<Category>): MutableList<Boolean> {
        val list = mutableListOf<Boolean>()
        for (category in value) {
            list.add(false)
        }
        return list
    }

    private fun getCategories() {
        getCategoriesUseCase(
            scope = viewModelScope,
            params = Unit
        ) { result ->
            result.onSuccess {
                _categories.value = it
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    val categories: LiveData<List<CategoryDisplayable>> by lazy {
        _categories.map {
            it.map { category -> CategoryDisplayable(category) }
        }
    }


    fun saveBook(book: BookDisplayable) {
        setPendingState()
        val saveBook = book.toBook()
        saveBook.categories = createListOfCategories()
        saveBookUseCase(
            scope = viewModelScope,
            params = saveBook
        ) {
            setIdleState()
            it.onSuccess {
                bookNavigator.openBooksScreen()
            }
            it.onFailure { throwable ->
                handleFailure(throwable)
            }
        }
    }

    private fun createListOfCategories(): List<Category>? {
        val chosen = mutableListOf<Category>()
        chosenCategories.forEachIndexed { index, b ->
            if (b) {
                _categories.value?.get(index)?.let { chosen.add(it) }
            }
        }
        return chosen
    }

}
package com.szymanski.sharelibrary.features.home.presentation.requirements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.home.domain.model.Requirement
import com.szymanski.sharelibrary.features.home.domain.usecase.GetUserRequirements
import com.szymanski.sharelibrary.features.home.presentation.model.RequirementDisplayable

class RequirementsViewModel(
    private val getUserRequirements: GetUserRequirements,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val getCoverUseCase: GetCoverUseCase,
    private val userStorage: UserStorage,
) : BaseViewModel() {

    private val TAG = "RequirementsViewModel"

    private val _requirements: MutableLiveData<List<Requirement>> by lazy {
        MutableLiveData<List<Requirement>>().also {
            getRequirements()
        }
    }
    val requirement: LiveData<List<RequirementDisplayable>> by lazy {
        _requirements.map {
            it.map { requirement ->
                RequirementDisplayable(requirement)
            }
        }
    }
    private val _otherUserBooks: MutableLiveData<List<Book>> by lazy {
        MutableLiveData<List<Book>>()
    }
    val otherUserBooks: LiveData<List<BookDisplayable>> by lazy {
        _otherUserBooks.map {
            it.map { book ->
                BookDisplayable(book)
            }
        }
    }
    private val _bookDetails: MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }
    val bookDetails: LiveData<BookDisplayable> by lazy {
        _bookDetails.map {
            BookDisplayable(it)
        }
    }


    private fun getRequirements() {
        setPendingState()
        getUserRequirements(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            setIdleState()
            result.onSuccess {
                _requirements.value = it
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun getUserBooks(userId: Long) {
        setPendingState()
        getUsersBookUseCase(
            scope = viewModelScope,
            params = userId
        ) { result ->
            setIdleState()
            result.onSuccess {
                _otherUserBooks.value =
                    it.filter { book -> book.status == BookStatus.AT_OWNER || book.status == BookStatus.SHARED }
            }
            result.onFailure { handleFailure(it) }
        }
    }

    fun refreshRequirements() {
        getRequirements()
    }

    fun downloadImage(bookDisplayable: BookDisplayable) {
        bookDisplayable.id?.let {
            getCoverUseCase(viewModelScope, params = it) { result ->
                result.onSuccess { cover ->
                    val book =
                        _otherUserBooks.value?.first { book -> book.id == bookDisplayable.id }
                    book?.cover = cover
                    _bookDetails.value = book
                }
                result.onFailure { it ->
                    handleFailure(it)

                }
            }
        }
    }
}
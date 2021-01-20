package com.szymanski.sharelibrary.features.book.presentation.otheruserbook

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesUseCase
import com.szymanski.sharelibrary.features.home.domain.model.Requirement
import com.szymanski.sharelibrary.features.home.domain.usecase.CreateRequirementUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetRequirementByExchangeIdUseCase
import com.szymanski.sharelibrary.features.home.presentation.model.RequirementDisplayable

class OtherUserBooksViewModel(
    errorMapper: ErrorMapper,
    userStorage: UserStorage,
    private val getCoverUseCase: GetCoverUseCase,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val getRequirementByExchangeIdUseCase: GetRequirementByExchangeIdUseCase,
    private val getExchangesUseCase: GetExchangesUseCase,
    private val createRequirementUseCase: CreateRequirementUseCase,
) : BaseViewModel(errorMapper) {


    val userId: Long = userStorage.getUserId()

    private var exchangeId: Long? = null

    private var otherUserId: Long? = null

    private val _books by lazy {
        MutableLiveData<List<Book>>()
    }

    val books by lazy {
        _books.map {
            it.map { book ->
                BookDisplayable(book)
            }
        }
    }
    private val _requirements by lazy {
        MutableLiveData<List<Requirement>>()
    }

    val requirements by lazy {
        _requirements.map {
            it.map { requirement ->
                RequirementDisplayable(requirement)
            }
        }
    }

    fun getUsersBook(otherUserId: Long) {
        this.otherUserId = otherUserId
        setPendingState()
        getUsersBookUseCase(scope = viewModelScope, params = this.otherUserId!!) { result ->
            setIdleState()
            result.onSuccess { books ->
                _books.value = books
                downloadCovers(books)
            }

            result.onFailure { throwable ->
                handleFailure(throwable)
            }
        }
    }

    private fun downloadCovers(books: List<Book>) {
        books.forEach { book ->
            book.id?.let {
                getCoverUseCase(viewModelScope, params = it) { result ->
                    result.onSuccess { cover ->
                        val booksWithCovers = mutableSetOf<Book>()
                        book.cover = cover
                        booksWithCovers.add(book)
                        _books.value?.let { it1 -> booksWithCovers.addAll(it1) }
                        _books.value = booksWithCovers.sortedBy { book -> book.id }.toList()
                    }
                    result.onFailure { it ->
                        handleFailure(it)

                    }
                }
            }
        }
    }

    //TODO check if instead GetExchangesUseCase should be GetUserExchanges
    fun getBookRequirements(bookDisplayable: BookDisplayable) {
        setPendingState()
        getExchangesUseCase(
            scope = viewModelScope,
            params = otherUserId!!
        ) { result ->
            setIdleState()
            result.onSuccess { exchanges ->
                val exchange: Exchange?
                try {
                    exchange = exchanges.first { it.book.id == bookDisplayable.id }
                } catch (exception: NoSuchElementException) {
                    _requirements.value = emptyList()
                    return@onSuccess
                }
                exchangeId = exchange.id
                exchange.id?.let { getRequirementsByExchangeId(it) }
            }
            result.onFailure {
                handleFailure(it)
            }

        }
    }

    private fun getRequirementsByExchangeId(exchangeId: Long) {
        setPendingState()
        getRequirementByExchangeIdUseCase(
            scope = viewModelScope,
            params = exchangeId
        ) { result ->
            setIdleState()
            result.onSuccess { requirementsList ->
                requirementsList.let {
                    _requirements.value = it
                }
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun requirementBook() {
        createRequirementUseCase(
            scope = viewModelScope,
            params = Pair(exchangeId!!, userId)
        ) { result ->
            result.onSuccess {
                val new = setOf(it)
                new.plus(_requirements)
                _requirements.value = new.toList()
                otherUserId?.let { id -> getUsersBook(id) }
                showMessage("Your request has been made successfully")
            }
            result.onFailure { handleFailure(it) }
        }
    }

}
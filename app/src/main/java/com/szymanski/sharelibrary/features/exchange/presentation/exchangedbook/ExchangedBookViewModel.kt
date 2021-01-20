package com.szymanski.sharelibrary.features.exchange.presentation.exchangedbook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetRoomBySenderIdAndRecipientIdUseCase
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetUserWhichHaveBookWhereAtUserIdIs
import com.szymanski.sharelibrary.features.home.navigation.HomeNavigation
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class ExchangedBookViewModel(
    errorMapper: ErrorMapper,
    private val getUserWhichHaveBookWhereAtUserIdIs: GetUserWhichHaveBookWhereAtUserIdIs,
    private val getRoomBySenderIdAndRecipientIdUseCase: GetRoomBySenderIdAndRecipientIdUseCase,
    private val homeNavigation: HomeNavigation,
    private val userStorage: UserStorage,
    private val getCoverUseCase: GetCoverUseCase,
) : BaseViewModel(errorMapper) {

    private val _users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers()
        }
    }
    val users: LiveData<List<UserDisplayable>> by lazy {
        _users.map { list ->
            list.map {
                UserDisplayable(it)
            }
        }
    }

    private val _books: MutableLiveData<List<Book>> by lazy {
        MutableLiveData(getAllBooks(_users.value)).also {
            loadUsers()
        }
    }

    val books: LiveData<List<BookDisplayable>> by lazy {
        _books.map { list ->
            list.map {
                BookDisplayable(it)
            }
        }
    }

    private fun getAllBooks(value: List<User>?): List<Book> {
        val result = mutableSetOf<Book>()
        value?.forEach { user ->
            user.books?.let { result.addAll(it) }
        }
        return result.toList()
    }

    private val _book: MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }
    val book: LiveData<BookDisplayable> by lazy {
        _book.map {
            BookDisplayable(it)
        }
    }

    private fun loadUsers() {
        setPendingState()
        getUserWhichHaveBookWhereAtUserIdIs(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            setIdleState()
            result.onSuccess {
                _users.value = it
                _books.value = getAllBooks(it)
            }
            result.onFailure { handleFailure(it) }
        }
    }

    fun downloadCover(bookDisplayable: BookDisplayable) {
        setPendingState()
        getCoverUseCase(
            scope = viewModelScope,
            params = bookDisplayable.id!!
        ) { result ->
            setIdleState()
            result.onSuccess {
                bookDisplayable.cover = it
                _book.value = bookDisplayable.toBook()
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun openChatRoom(otherUser: UserDisplayable) {
        getRoomBySenderIdAndRecipientIdUseCase(
            params = Pair(userStorage.getUserId(), otherUser.id!!),
            scope = viewModelScope
        ) { result ->
            result.onSuccess {
                homeNavigation.openChatRoomIfExists(RoomDisplayable(it))
            }
            result.onFailure {
                homeNavigation.openChatRoomIfNotExists(otherUser)
            }

        }
    }

    fun getUserByBook(book: BookDisplayable): UserDisplayable? {
        val user = _users.value?.first { user ->
            user.books?.any { b -> b.id == book.id }!!
        }!!
        return UserDisplayable(user)
    }
}
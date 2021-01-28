package com.szymanski.sharelibrary.features.home.presentation.requirements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import com.szymanski.sharelibrary.core.api.model.request.ExecuteExchangeRequest
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.presentation.model.BookDisplayable
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetRoomBySenderIdAndRecipientIdUseCase
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.home.domain.model.Requirement
import com.szymanski.sharelibrary.features.home.domain.usecase.ExecuteExchangeUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetUserRequirements
import com.szymanski.sharelibrary.features.home.navigation.HomeNavigation
import com.szymanski.sharelibrary.features.home.presentation.model.RequirementDisplayable
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class RequirementsViewModel(
    private val getUserRequirements: GetUserRequirements,
    private val getUsersBookUseCase: GetUsersBookUseCase,
    private val getCoverUseCase: GetCoverUseCase,
    private val executeExchangeUseCase: ExecuteExchangeUseCase,
    private val getRoomBySenderIdAndRecipientIdUseCase: GetRoomBySenderIdAndRecipientIdUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val homeNavigation: HomeNavigation,
    private val userStorage: UserStorage,
) : BaseViewModel() {

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
    private val _otherUserBooks: MutableList<Book> = mutableListOf()

    fun getOtherUserBooks(): List<BookDisplayable> {
        return _otherUserBooks.map {
            BookDisplayable(it)
        }
    }

    private val _bookDetails: LiveEvent<Book> by lazy {
        LiveEvent()
    }
    val bookDetails: LiveData<BookDisplayable> by lazy {
        _bookDetails.map {
            BookDisplayable(it)
        }
    }
    val _canDisplay: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }


    fun getRequirements() {
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
                _otherUserBooks.clear()
                _otherUserBooks.addAll(
                    it.filter { book -> book.status == BookStatus.AT_OWNER || book.status == BookStatus.SHARED })
                _canDisplay.value = true
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun refreshRequirements() {
        getRequirements()
    }

    fun downloadImage(bookDisplayable: BookDisplayable) {
        setPendingState()
        bookDisplayable.id?.let {
            getCoverUseCase(viewModelScope, params = it) { result ->
                result.onSuccess { cover ->
                    setIdleState()
                    val book =
                        _otherUserBooks.first { book -> book.id == bookDisplayable.id }
                    book.cover = cover
                    _bookDetails.postValue(book)
                }
                result.onFailure { it ->
                    handleFailure(it)

                }
            }
        }
    }

    fun executeExchange(params: Map<String, Long>) {
        setPendingState()
        executeExchangeUseCase(
            scope = viewModelScope,
            params = params
        ) { result ->
            setIdleState()
            result.onSuccess {
                showMessage("Exchange has been made")
                params[ExecuteExchangeRequest.WITH_USER_ID_KEY]?.let { it1 -> getUserBooks(it1) }
                getUserRequirements(
                    scope = viewModelScope,
                    params = userStorage.getUserId()
                ) { result ->
                    result.onSuccess {
                        _requirements.value = it

                    }
                    params.get(ExecuteExchangeRequest.WITH_USER_ID_KEY)
                        ?.let { it1 -> openChatRoom(it1) }
                }
            }
            result.onFailure { handleFailure(it) }
        }
    }

    private fun openChatRoom(otherUserId: Long) {
        getUserUseCase(
            params = otherUserId,
            scope = viewModelScope
        ) { result ->
            result.onSuccess { user ->
                getRoomBySenderIdAndRecipientIdUseCase(
                    params = Pair(userStorage.getUserId(), otherUserId),
                    scope = viewModelScope
                ) { result ->
                    result.onSuccess {
                        homeNavigation.openChatRoomIfExists(RoomDisplayable(it))
                    }
                    result.onFailure {
                        homeNavigation.openChatRoomIfNotExists(UserDisplayable(user))
                    }

                }
            }
        }

    }
}
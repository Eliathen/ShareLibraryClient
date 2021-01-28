package com.szymanski.sharelibrary.features.home.presentation.currentexchanges

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetRoomBySenderIdAndRecipientIdUseCase
import com.szymanski.sharelibrary.features.chat.presentation.model.RoomDisplayable
import com.szymanski.sharelibrary.features.home.domain.model.ExchangeDetails
import com.szymanski.sharelibrary.features.home.domain.usecase.FinishExchangeByIdUseCase
import com.szymanski.sharelibrary.features.home.domain.usecase.GetCurrentExchangesLinkedWithUserUseCase
import com.szymanski.sharelibrary.features.home.navigation.HomeNavigation
import com.szymanski.sharelibrary.features.home.presentation.model.ExchangeDetailsDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class CurrentExchangeViewModel(
    private val getCurrentExchangesLinkedWithUserUseCase: GetCurrentExchangesLinkedWithUserUseCase,
    private val userStorage: UserStorage,
    private val finishExchangeByIdUseCase: FinishExchangeByIdUseCase,
    private val getRoomBySenderIdAndRecipientIdUseCase: GetRoomBySenderIdAndRecipientIdUseCase,
    private val homeNavigation: HomeNavigation,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {


    private val _exchanges: MutableLiveData<MutableList<ExchangeDetails>> by lazy {
        MutableLiveData<MutableList<ExchangeDetails>>().also {
            getCurrentExchanges()
        }
    }

    val exchanges: LiveData<List<ExchangeDetailsDisplayable>> by lazy {
        _exchanges.map { list ->
            list.map { exchange ->
                ExchangeDetailsDisplayable(exchange)
            }
        }
    }

    val currentUserId: Long by lazy { userStorage.getUserId() }

    fun getCurrentExchanges() {
        setPendingState()
        getCurrentExchangesLinkedWithUserUseCase(
            scope = viewModelScope,
            params = userStorage.getUserId()
        ) { result ->
            setIdleState()
            result.onSuccess {
                _exchanges.value = ArrayList(it)
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun finishExchange(exchangeId: Long) {
        setPendingState()
        finishExchangeByIdUseCase(
            params = exchangeId,
            scope = viewModelScope
        ) { result ->
            setIdleState()
            result.onSuccess {
                showMessage("Exchange has been finished")
                getCurrentExchanges()
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun openChatRoom(firstUser: UserDisplayable, currentUser: UserDisplayable) {
        setPendingState()
        getRoomBySenderIdAndRecipientIdUseCase(
            scope = viewModelScope,
            params = Pair(firstUser.id!!, currentUser.id!!)
        ) { result ->
            setIdleState()
            result.onSuccess {
                homeNavigation.openChatRoomIfExists(RoomDisplayable(it))
            }
            result.onFailure {
                homeNavigation.openChatRoomIfNotExists(firstUser)

            }
        }
    }
}

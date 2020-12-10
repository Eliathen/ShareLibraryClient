package com.szymanski.sharelibrary.features.exchange.presentation.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesUseCase
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigation
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class ExchangesViewModel(
    errorMapper: ErrorMapper,
    private val getExchangesUseCase: GetExchangesUseCase,
    private val exchangeNavigation: ExchangeNavigation,
    private val getUserUseCase: GetUserUseCase,
    userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    private val userId = userStorage.getUserId()

    private val _user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().also {
            getUserDetails()
        }
    }
    val user: LiveData<UserDisplayable> by lazy {
        _user.map {
            UserDisplayable(it)
        }
    }

    private fun getUserDetails() {
        getUserUseCase(
            scope = viewModelScope,
            params = userId
        ) { result ->
            result.onSuccess { _user.value = it }
        }
    }

    private val _exchanges: MutableLiveData<List<Exchange>> by lazy {
        MutableLiveData<List<Exchange>>().also {
            getExchanges()
        }
    }
    val exchanges: LiveData<List<ExchangeDisplayable>> by lazy {
        _exchanges.map { exchanges ->
            exchanges.map { exchange ->
                ExchangeDisplayable(exchange)
            }
        }
    }
    private fun getExchanges() {
        getExchangesUseCase(
            scope = viewModelScope,
            params = userId
        ) { result ->
            result.onSuccess {
                _exchanges.value = it
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun displayExchangeDetails(exchangeId: Long) {
        exchangeNavigation.openExchangeDetails(exchangeId)
    }


}
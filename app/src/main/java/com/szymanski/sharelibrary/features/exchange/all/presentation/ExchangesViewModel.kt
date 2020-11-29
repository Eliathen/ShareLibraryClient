package com.szymanski.sharelibrary.features.exchange.all.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.exchange.all.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesUseCase

class ExchangesViewModel(
    errorMapper: ErrorMapper,
    private val getExchangesUseCase: GetExchangesUseCase,
    userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    private val userId = userStorage.getUserId()

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
    private val TAG = "ExchangesViewModel"
    private fun getExchanges() {
        getExchangesUseCase(
            scope = viewModelScope,
            params = userId
        ) { result ->
            result.onSuccess {
                _exchanges.value = it
            }
            result.onFailure {
                Log.d(TAG, "getExchanges: ${it.message}")
            }
        }
    }

}
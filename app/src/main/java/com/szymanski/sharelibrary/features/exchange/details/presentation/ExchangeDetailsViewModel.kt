package com.szymanski.sharelibrary.features.exchange.details.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.exchange.all.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangeByIdUseCase
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigation
import com.szymanski.sharelibrary.features.requirement.domain.model.Requirement
import com.szymanski.sharelibrary.features.requirement.domain.usecase.CreateRequirementUseCase
import com.szymanski.sharelibrary.features.requirement.domain.usecase.GetRequirementsUseCase
import com.szymanski.sharelibrary.features.requirement.presentation.model.RequirementDisplayable

class ExchangeDetailsViewModel(
    private val getExchangeByIdUseCase: GetExchangeByIdUseCase,
    private val getCoverUseCase: GetCoverUseCase,
    private val createRequirementUseCase: CreateRequirementUseCase,
    private val userStorage: UserStorage,
    private val getRequirementUseCase: GetRequirementsUseCase,
    private val exchangeNavigation: ExchangeNavigation,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {

    private val TAG = "ExchangeDetailsViewMode"

    val userId = userStorage.getUserId()

    private val _exchange: MutableLiveData<Exchange> by lazy {
        MutableLiveData()
    }

    val exchange: LiveData<ExchangeDisplayable> by lazy {
        _exchange.map { exchange ->
            ExchangeDisplayable(exchange)
        }
    }

    private val _requirements: MutableLiveData<List<Requirement>> by lazy {
        MutableLiveData()
    }

    val requirements: LiveData<List<RequirementDisplayable>> by lazy {
        _requirements.map {
            it.map { requirement ->
                RequirementDisplayable(requirement)
            }
        }
    }

    fun getExchangeDetails(exchangeId: Long) {
        getExchangeByIdUseCase(
            scope = viewModelScope,
            params = exchangeId
        ) { result ->

            result.onSuccess {
                _exchange.value = it
                it.book.id?.let { bookId -> getCover(bookId) }
                getRequirements(it.id!!)
            }
            result.onFailure {
                Log.d(TAG, "getExchangeDetails: ${it.message}")
                handleFailure(it)
            }
        }
    }

    private fun getRequirements(id: Long) {
        getRequirementUseCase(
            scope = viewModelScope,
            params = id
        ) { result ->
            result.onSuccess {
                _requirements.value = it
            }
            result.onFailure {
                Log.d(TAG, "getRequirements: ${it.message}")
                handleFailure(it)
            }
        }
    }

    private fun getCover(bookId: Long) {
        getCoverUseCase(
            scope = viewModelScope,
            params = bookId
        ) { result ->

            result.onSuccess {
                val exchange = _exchange.value!!
                exchange.book.cover = it
                _exchange.value = exchange
            }

        }
    }

    fun requirementBook(exchangeDisplayable: ExchangeDisplayable) {
        createRequirementUseCase(
            scope = viewModelScope,
            params = Pair(exchangeDisplayable.id!!, userStorage.getUserId())
        ) { result ->
            result.onSuccess {
                val requirements = setOf(it)
                requirements.plus(_requirements.value)
                _requirements.value = requirements.toList()
            }
            result.onFailure { throwable ->
                handleFailure(throwable)
            }
        }
    }

    fun openOtherUserScreen() {
        exchangeNavigation.openOtherUserScreen(_exchange.value?.user?.id!!)
    }


}
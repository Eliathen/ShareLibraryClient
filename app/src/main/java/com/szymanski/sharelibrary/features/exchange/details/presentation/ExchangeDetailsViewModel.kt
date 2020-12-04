package com.szymanski.sharelibrary.features.exchange.details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCoverUseCase
import com.szymanski.sharelibrary.features.exchange.all.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangeByIdUseCase

class ExchangeDetailsViewModel(
    private val getExchangeByIdUseCase: GetExchangeByIdUseCase,
    private val getCoverUseCase: GetCoverUseCase,
    errorMapper: ErrorMapper,
) : BaseViewModel(errorMapper) {

    private val _exchange: MutableLiveData<Exchange> by lazy {
        MutableLiveData()
    }

    val exchange: LiveData<ExchangeDisplayable> by lazy {
        _exchange.map { exchange ->
            ExchangeDisplayable(exchange)
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
            }

            result.onFailure { handleFailure(it) }

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


}
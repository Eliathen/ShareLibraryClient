package com.szymanski.sharelibrary.features.exchange.presentation.all

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.api.model.request.SearchRequest
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.SortOption
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesByFiltersUseCase
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesUseCase
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigation
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class ExchangesViewModel(
    errorMapper: ErrorMapper,
    private val getExchangesUseCase: GetExchangesUseCase,
    private val exchangeNavigation: ExchangeNavigation,
    private val getUserUseCase: GetUserUseCase,
    private val getExchangesByFiltersUseCase: GetExchangesByFiltersUseCase,
    userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    private val userId = userStorage.getUserId()

    private var sortOption: SortOption? = null

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

    private val copyExchanges: MutableList<Exchange> = mutableListOf()

    private val _exchanges: MutableLiveData<List<Exchange>> by lazy {
        MutableLiveData<List<Exchange>>()
//            .also {
//            getExchanges()
//        }
    }

    val exchanges: LiveData<List<ExchangeDisplayable>> by lazy {
        _exchanges.map { exchanges ->
            exchanges.map { exchange ->
                ExchangeDisplayable(exchange)
            }
        }
    }

    val mapOfExchanges: MutableLiveData<Map<Coordinate, MutableList<Exchange>>> by lazy {
        MutableLiveData<Map<Coordinate, MutableList<Exchange>>>().also {
            getExchanges()
        }
    }

    private val _currentCoordinates: MutableLiveData<Coordinate> by lazy {
        MutableLiveData<Coordinate>()
    }
    private var query: String? = ""

    private var radius: Double? = 100.0

    private val categories: List<String>? = mutableListOf()

//    private val currentCoordinates: LiveData<CoordinateDisplayable> by lazy {
//        _currentCoordinates.map {
//            CoordinateDisplayable(it)
//        }
//    }

    private fun getUserDetails() {
        getUserUseCase(
            scope = viewModelScope,
            params = userId
        ) { result ->
            result.onSuccess { _user.value = it }
        }
    }

    private val TAG = "ExchangesViewModel"
    private fun getFilteredExchanges() {
        setPendingState()
        val request = SearchRequest(
            _currentCoordinates.value?.latitude!!,
            _currentCoordinates.value?.longitude!!,
            radius,
            categories,
            query
        )
        getExchangesByFiltersUseCase(
            scope = viewModelScope,
            params = request
        ) { result ->
            setIdleState()
            result.onSuccess { _exchanges.value = it }
            result.onFailure {
                Log.e(TAG, "getFilteredExchanges: ${it.message}", it)
                handleFailure(it)
            }
        }
    }

    private fun getExchanges() {
        getExchangesUseCase(
            scope = viewModelScope,
            params = userId
        ) { result ->
            result.onSuccess {
                if (sortOption != null) {
                    _exchanges.value = sort(sortOption!!)
                } else _exchanges.value = it
                copyExchanges.clear()
                copyExchanges.addAll(it)
                createMapFromExchanges(it)
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    private fun createMapFromExchanges(exchanges: List<Exchange>) {
        val map = hashMapOf<Coordinate, MutableList<Exchange>>()
        exchanges.forEach {
            if (!map.containsKey(it.coordinates)) {
                map[it.coordinates] = mutableListOf(it)
            } else {
                map.getValue(it.coordinates).add(it)
            }
        }
        mapOfExchanges.value = map
    }

    fun setCoordinates(latitude: Double, longitude: Double) {
        val coordinate = Coordinate(null, latitude, longitude)
        _currentCoordinates.value = coordinate
        getFilteredExchanges()
    }


    fun displayExchangeDetails(exchangeId: Long) {
        exchangeNavigation.openExchangeDetails(exchangeId)
    }

    fun setSort(sort: SortOption) {
        sortOption = sort
        _exchanges.value = sort(sort)
    }

    private fun sort(sort: SortOption): List<Exchange>? {
        val toSort = _exchanges.value
        return when (sort) {
            SortOption.TITLE_ASC -> {
                toSort?.sortedBy { it.book.title }
            }
            SortOption.TITLE_DESC -> {
                toSort?.sortedByDescending { it.book.title }
            }
            SortOption.DISTANCE_ASC -> {
                toSort?.sortedBy { it.distance }
            }
            SortOption.DISTANCE_DESC -> {
                toSort?.sortedByDescending { it.distance }
            }
        }
    }
}
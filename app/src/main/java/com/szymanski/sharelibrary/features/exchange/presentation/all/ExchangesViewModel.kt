package com.szymanski.sharelibrary.features.exchange.presentation.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.api.model.request.SearchRequest
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.utils.BookCondition
import com.szymanski.sharelibrary.core.utils.SortOption
import com.szymanski.sharelibrary.core.utils.defaultRadiusDistance
import com.szymanski.sharelibrary.features.book.domain.model.Category
import com.szymanski.sharelibrary.features.book.domain.model.Language
import com.szymanski.sharelibrary.features.book.domain.usecase.GetCategoriesUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.GetLanguagesUseCase
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.exchange.domain.usecase.GetExchangesByFiltersUseCase
import com.szymanski.sharelibrary.features.exchange.navigation.ExchangeNavigation
import com.szymanski.sharelibrary.features.exchange.presentation.model.ExchangeDisplayable
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.User
import com.szymanski.sharelibrary.features.user.domain.usecase.GetUserUseCase
import com.szymanski.sharelibrary.features.user.presentation.model.CoordinateDisplayable
import com.szymanski.sharelibrary.features.user.presentation.model.UserDisplayable

class ExchangesViewModel(
    errorMapper: ErrorMapper,
    private val exchangeNavigation: ExchangeNavigation,
    private val getUserUseCase: GetUserUseCase,
    private val getExchangesByFiltersUseCase: GetExchangesByFiltersUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getLanguagesUseCase: GetLanguagesUseCase,
    userStorage: UserStorage,
) : BaseViewModel(errorMapper) {

    private val userId = userStorage.getUserId()

    var displayUserExchange: Boolean = false

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
    val categories: MutableLiveData<List<Category>> =
        MutableLiveData<List<Category>>().also {
            getCategories()
        }

    val languages: MutableLiveData<List<Language>> =
        MutableLiveData<List<Language>>().also {
            getLanguages()
        }

    private fun getLanguages() {
        getLanguagesUseCase(
            params = Unit,
            scope = viewModelScope
        ) { result ->
            result.onSuccess { list ->
                languages.value = list.sortedBy { it.name }
            }
            result.onFailure {

            }

        }
    }

    private val _exchanges: MutableLiveData<List<Exchange>> by lazy {
        MutableLiveData<List<Exchange>>()
    }

    val exchanges: LiveData<List<ExchangeDisplayable>> by lazy {
        _exchanges.map { exchanges ->
            exchanges.map { exchange ->
                ExchangeDisplayable(exchange)
            }
        }
    }

    val mapOfExchanges: MutableLiveData<Map<Coordinate, MutableList<Exchange>>> by lazy {
        MutableLiveData<Map<Coordinate, MutableList<Exchange>>>()
    }

    private val _currentCoordinates: MutableLiveData<Coordinate> by lazy {
        MutableLiveData<Coordinate>()
    }

    val currentCoordinates: LiveData<CoordinateDisplayable> by lazy {
        _currentCoordinates.map {
            CoordinateDisplayable(it)
        }
    }
    private val _query: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }
    val query: LiveData<String> by lazy {
        _query
    }

    private var radius: Double? = defaultRadiusDistance

    private val chosenCategories: MutableMap<String, Boolean>? = mutableMapOf()

    var chosenLanguageId: Int? = null

    var chosenCondition: MutableSet<BookCondition> = mutableSetOf()

    fun addCondition(condition: BookCondition) {
        chosenCondition.add(condition)
    }

    fun removeCondition(condition: BookCondition) {
        chosenCondition.remove(condition)
    }

    val circleRadius: MutableLiveData<Double> by lazy {
        MutableLiveData(radius?.times(1000.0))
    }

    private val _exchangeToDisplay: MutableLiveData<Exchange> by lazy {
        MutableLiveData<Exchange>()
    }

    val exchangeToDisplay: LiveData<ExchangeDisplayable> by lazy {
        _exchangeToDisplay.map {
            ExchangeDisplayable(it)
        }
    }

    fun setExchangeToDisplay(exchange: ExchangeDisplayable) {
        _exchangeToDisplay.value = exchange.toExchange()
    }

    private fun getCategories() {
        getCategoriesUseCase(
            scope = viewModelScope,
            params = Unit
        ) { result ->
            result.onSuccess { categories.value = it.sortedBy { category -> category.name } }
        }
    }

    fun setQuery(query: String) {
        _query.value = query
    }

    fun setRadius(radius: Double) {
        this.radius = radius
    }

    fun getRadius() = this.radius

    fun setChosenCategory(name: String, checked: Boolean) {
        chosenCategories?.set(name, checked)
    }

    fun getChosenCategories() = chosenCategories

    private fun getUserDetails() {
        getUserUseCase(
            scope = viewModelScope,
            params = userId
        ) { result ->
            result.onSuccess { _user.value = it }
        }
    }

    fun getFilteredExchanges() {
        val request = SearchRequest(
            _currentCoordinates.value?.latitude!!,
            _currentCoordinates.value?.longitude!!,
            radius,
            chosenCategories?.filter { it.value }?.keys?.toList(),
            _query.value,
            chosenLanguageId,
            chosenCondition.map { it.ordinal }
        )
        setPendingState()
        getExchangesByFiltersUseCase(
            scope = viewModelScope,
            params = request
        ) { result ->
            setIdleState()
            result.onSuccess {
                circleRadius.value = radius
                _exchanges.value = it
                createMapFromExchanges(it)
            }
            result.onFailure {
                handleFailure(it)
            }
        }
    }

    fun resetFilters() {
        _query.value = ""
        radius = defaultRadiusDistance
        chosenCategories!!.clear()
        circleRadius.value = defaultRadiusDistance
        chosenCondition.clear()
        chosenLanguageId = null
    }

    fun createMapFromExchanges(exchanges: List<Exchange>) {
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
                toSort?.sortedByDescending { it.book.title }
            }
            SortOption.TITLE_DESC -> {
                toSort?.sortedBy { it.book.title }
            }
            SortOption.DISTANCE_ASC -> {
                toSort?.sortedBy { it.distance }
            }
            SortOption.DISTANCE_DESC -> {
                toSort?.sortedByDescending { it.distance }
            }
        }
    }

    fun navigateBack() {
        exchangeNavigation.navigateBack()
    }

}
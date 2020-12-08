package com.szymanski.sharelibrary.features.home.presentation.requirements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.home.domain.model.Requirement
import com.szymanski.sharelibrary.features.home.domain.usecase.GetUserRequirements
import com.szymanski.sharelibrary.features.home.presentation.model.RequirementDisplayable

class RequirementsViewModel(
    private val getUserRequirements: GetUserRequirements,
    private val userStorage: UserStorage,
) : BaseViewModel() {

    private val TAG = "RequirementsViewModel"

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

    private fun getRequirements() {
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

    fun refreshRequirements() {
        getRequirements()
    }


}
package com.szymanski.sharelibrary.core.base

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.utils.TAG

open class BaseViewModel(private val errorMapper: ErrorMapper? = null) : ViewModel(),
    DefaultLifecycleObserver {

    private val _message by lazy { LiveEvent<String>() }

    val message: LiveData<String> by lazy { _message }

    private val _uiState by lazy { MutableLiveData<UiState>(UiState.Idle) }

    val uiState: LiveData<UiState> by lazy { _uiState }

    protected fun setPendingState() {
        _uiState.value = UiState.Pending
    }

    protected fun setIdleState() {
        _uiState.value = UiState.Idle
    }

    protected fun showMessage(message: String) {
        _message.value = message
    }

    protected fun handleFailure(throwable: Throwable) {
        Log.d(TAG, "handleFailure: ${throwable.message}")
        errorMapper?.map(throwable)?.let { showMessage(it) }
    }
}

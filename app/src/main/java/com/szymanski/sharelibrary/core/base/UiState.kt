package com.szymanski.sharelibrary.core.base

sealed class UiState {
    object Idle : UiState()
    object Pending : UiState()
}
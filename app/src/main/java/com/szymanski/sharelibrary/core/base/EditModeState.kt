package com.szymanski.sharelibrary.core.base

sealed class EditModeState {
    object Active : EditModeState()
    object Inactive : EditModeState()
}
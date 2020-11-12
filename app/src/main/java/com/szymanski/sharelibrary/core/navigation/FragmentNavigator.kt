package com.szymanski.sharelibrary.core.navigation

import androidx.annotation.IdRes

interface FragmentNavigator {

    fun navigateTo(@IdRes destinationId: Int)

    fun goBack(@IdRes destinationId: Int? = null, inclusive: Boolean = false)

    fun clearHistory()
}
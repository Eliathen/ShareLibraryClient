package com.szymanski.sharelibrary.core.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.szymanski.sharelibrary.core.provider.ActivityProvider

class FragmentNavigatorImpl(
    private val activityProvider: ActivityProvider,
    @IdRes private val navHostFragmentRes: Int,
    @IdRes private val homeHostFragmentRes: Int,
) : FragmentNavigator {

    private fun getSupportFragmentManager() =
        (activityProvider.currentActivity as? FragmentActivity)?.supportFragmentManager

    private fun getNavController() =
        getSupportFragmentManager()?.findFragmentById(navHostFragmentRes)?.findNavController()

    override fun navigateTo(destinationId: Int, bundle: Bundle?) {
        getNavController()?.navigate(destinationId)
    }

    override fun goBack(destinationId: Int?, inclusive: Boolean) {
        if (destinationId == null) {
            getNavController()?.popBackStack()
        } else {
            getNavController()?.popBackStack(destinationId, inclusive)
        }
    }

    override fun clearHistory() {
        goBack(homeHostFragmentRes)
    }
}
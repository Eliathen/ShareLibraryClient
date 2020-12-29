package com.szymanski.sharelibrary.core.navigation

import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.szymanski.sharelibrary.core.provider.ActivityProvider

class FragmentNavigatorImpl(
    private val activityProvider: ActivityProvider,
    @IdRes private val navHostFragmentRes: Int,
    @IdRes private val homeHostFragmentRes: Int,
    private val defaultNavOptions: NavOptions,
) : FragmentNavigator {

    override fun <T> navigateTo(
        destinationId: Int,
        param: Pair<String, T>?,
        fragmentTransition: FragmentTransition?,
    ) {
        val bundle = param?.let { bundleOf(it) }
        val navOptions = fragmentTransition?.let {
            navOptions {
                anim {
                    enter = it.enterAnim
                    exit = it.exitAnim
                    popEnter = it.popEnterAnim
                    popExit = it.popExitAnim
                }
            }
        } ?: defaultNavOptions

        getNavController()?.navigate(destinationId, bundle, navOptions)
    }

    override fun navigateTo(
        destinationId: Int,
        fragmentTransition: FragmentTransition?,
    ) {
        navigateTo<Unit>(destinationId, null, fragmentTransition)
    }

    override fun goBack(
        destinationId: Int?,
        inclusive: Boolean,
    ) {
        if (destinationId == null) {
            getNavController()?.popBackStack()
        } else {
            getNavController()?.popBackStack(destinationId, inclusive)
        }
    }

    override fun clearHistory() {
        goBack(homeHostFragmentRes)
    }

    private fun getSupportFragmentManager() =
        (activityProvider.currentActivity as? FragmentActivity)?.supportFragmentManager

    private fun getNavController() =
        getSupportFragmentManager()?.findFragmentById(navHostFragmentRes)?.findNavController()

}
package com.szymanski.sharelibrary.features.user.navigation

interface UserNavigation {

    fun openRegisterScreen()

    fun openBooksScreen()

    fun openLoginScreen()

    fun openBooksScreenAfterRegister()

    fun openLoginScreenAfterLogout()

    fun goBack()

    fun openOtherUserBooksScreen(userId: Long)

}
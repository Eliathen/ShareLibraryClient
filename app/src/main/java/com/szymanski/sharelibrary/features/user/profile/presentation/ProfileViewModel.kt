package com.szymanski.sharelibrary.features.user.profile.presentation

import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.features.user.navigation.UserNavigation

class ProfileViewModel(
    private val errorMapper: ErrorMapper,
    private val userNavigation: UserNavigation,
) : BaseViewModel() {


}
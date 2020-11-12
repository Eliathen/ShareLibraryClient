package com.szymanski.sharelibrary.features.users.profile.presentation

import com.szymanski.sharelibrary.core.base.BaseViewModel
import com.szymanski.sharelibrary.core.exceptions.ErrorMapper
import com.szymanski.sharelibrary.features.users.navigation.UserNavigation

class ProfileViewModel(
    private val errorMapper: ErrorMapper,
    private val userNavigation: UserNavigation,
) : BaseViewModel() {


}
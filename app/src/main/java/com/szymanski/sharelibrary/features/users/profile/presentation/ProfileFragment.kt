package com.szymanski.sharelibrary.features.users.profile.presentation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<ProfileViewModel>(R.layout.fragment_profile) {
    override val viewModel: ProfileViewModel by viewModel()

}
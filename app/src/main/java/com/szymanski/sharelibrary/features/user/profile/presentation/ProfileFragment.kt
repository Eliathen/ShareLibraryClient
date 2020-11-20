package com.szymanski.sharelibrary.features.user.profile.presentation

import android.os.Bundle
import android.view.View
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.toolbar_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : BaseFragment<ProfileViewModel>(R.layout.fragment_profile) {
    override val viewModel: ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
    }

    override fun initObservers() {
        viewModel.user.observe(this) {
            val fullName = "${it.name} ${it.surname}"
            full_name.text = fullName
        }
    }

    private fun setToolbar() {
        val toolbar = toolbar_profile_screen
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
    }

    override fun onIdleState() {
        super.onIdleState()
        progress_bar_profile.visibility = View.GONE
    }

    override fun onPendingState() {
        super.onPendingState()
        progress_bar_profile.visibility = View.VISIBLE
    }
}
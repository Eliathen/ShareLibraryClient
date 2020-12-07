package com.szymanski.sharelibrary.features.home.presentation.requirements

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequirementsFragment : BaseFragment<RequirementsViewModel>(R.layout.fragment_requirements) {
    override val viewModel: RequirementsViewModel by viewModel()


    override fun initViews() {
        super.initViews()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.requirement.observe(this) {

        }
    }

    override fun onIdleState() {
        super.onIdleState()
    }

    override fun onPendingState() {
        super.onPendingState()
    }
}
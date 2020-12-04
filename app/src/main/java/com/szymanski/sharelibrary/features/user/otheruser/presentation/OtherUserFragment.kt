package com.szymanski.sharelibrary.features.user.otheruser.presentation

import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_other_user.*
import kotlinx.android.synthetic.main.toolbar_profile.*
import org.koin.android.ext.android.inject

class OtherUserFragment : BaseFragment<OtherUserViewModel>(R.layout.fragment_other_user) {
    override val viewModel: OtherUserViewModel by inject()

    override fun initViews() {
        super.initViews()
        setToolbar()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.user.observe(this) {
            val fullName = "${it.name} ${it.surname}"
            otherUser_full_name.text = fullName
        }
    }

    private fun setToolbar() {
        val toolbar = toolbar_profile_screen
        (activity as MainActivity).setSupportActionBar(toolbar)
        toolbar.title = ""
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
    }
}
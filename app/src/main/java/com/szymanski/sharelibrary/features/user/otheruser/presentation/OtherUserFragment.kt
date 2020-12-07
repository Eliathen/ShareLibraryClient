package com.szymanski.sharelibrary.features.user.otheruser.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import com.szymanski.sharelibrary.MainActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_other_user.*
import kotlinx.android.synthetic.main.toolbar_profile.*
import org.koin.android.ext.android.inject

class OtherUserFragment : BaseFragment<OtherUserViewModel>(R.layout.fragment_other_user) {
    override val viewModel: OtherUserViewModel by inject()

    private val TAG = "OtherUserFragment"

    companion object {
        val OTHER_USER_FRAGMENT_KEY = "OtherUserIdKey"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getLong(OTHER_USER_FRAGMENT_KEY)!!
        viewModel.loadUser(id)
    }

    override fun initViews() {
        super.initViews()
        setToolbar()
        initListeners()
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

    private fun initListeners() {
        send_message_button.setOnClickListener {

        }
        view_books_button.setOnClickListener {
            Log.d(TAG, "initListeners: ")
            viewModel.openOtherUserBooksScreen(viewModel.user.value?.id!!)
        }
    }

    override fun onIdleState() {
        super.onIdleState()
        otherUser_progress_bar_profile.visibility = View.GONE
    }

    override fun onPendingState() {
        super.onPendingState()
        otherUser_progress_bar_profile.visibility = View.VISIBLE

    }
}
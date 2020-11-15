package com.szymanski.sharelibrary.features.user.login.presentation

import android.content.ContentValues.TAG
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.user.login.presentation.model.LoginDisplayable
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<LoginViewModel>(R.layout.fragment_login) {
    override val viewModel: LoginViewModel by viewModel()

    override fun initViews() {
        super.initViews()
        initListeners()
    }

    override fun initObservers() {
        super.initObservers()
        viewModel.message.observe(this) {
            error_message_wrapper.visibility = View.VISIBLE
            error_message.text = it
        }
    }

    override fun onIdleState() {
        progress_bar.visibility = View.INVISIBLE
    }

    override fun onPendingState() {
        error_message_wrapper.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        Log.d(TAG, "onPendingState: set visibility = visible")
    }

    private fun initListeners() {
        login_button.setOnClickListener {
            attemptLogin()
        }
        signInButtonText.setOnClickListener {
            viewModel.navigateToRegisterScreen()
        }

    }

    private fun attemptLogin() {
        val userNameOrEmail = userNameOrEmailEditText.text.toString()
        val password = passwordEditText.text.toString()
        var close = false
        var focusView = View(context)
        if (TextUtils.isEmpty(userNameOrEmail)) {
            userNameOrEmailEditText.error = getString(R.string.field_required_error)
            close = true
            focusView = userNameOrEmailEditText
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.error = getString(R.string.field_required_error)
            close = true
            focusView = passwordEditText
        }

        if (close) {
            focusView.requestFocus()
        } else {
            viewModel.logIn(LoginDisplayable(userNameOrEmail, password.toCharArray()))
        }
    }
}
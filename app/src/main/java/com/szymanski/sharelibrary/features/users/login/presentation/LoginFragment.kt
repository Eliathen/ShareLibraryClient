package com.szymanski.sharelibrary.features.users.login.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.users.login.presentation.model.LoginDisplayable
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<LoginViewModel>(R.layout.fragment_login) {
    override val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initViews() {
        super.initViews()
        initListeners()

    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            attemptLogin()
        }
        signInButtonText.setOnClickListener {
            viewModel.navigateToRegisterScreen()
        }

    }

    private fun attemptLogin() {
        val userNameOrEmail = userNameOrEmailEditText.text.toString()
        val password: CharArray = passwordEditText.text.toString().toCharArray()
        var close = false
        var focusView = View(context)
        if (TextUtils.isEmpty(userNameOrEmail)) {
            userNameOrEmailEditText.error = getString(R.string.field_required_error)
            close = true
            focusView = userNameOrEmailEditText
        }
        if (TextUtils.isEmpty(password.toString())) {
            passwordEditText.error = getString(R.string.field_required_error)
            close = true
            focusView = userNameOrEmailEditText
        }

        if (close) {
            focusView.requestFocus()
        } else {
            viewModel.logIn(LoginDisplayable(userNameOrEmail, password))
        }
    }
}
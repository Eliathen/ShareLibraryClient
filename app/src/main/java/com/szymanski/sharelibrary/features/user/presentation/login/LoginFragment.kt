package com.szymanski.sharelibrary.features.user.presentation.login

import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.features.user.presentation.model.LoginDisplayable
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<LoginViewModel>(R.layout.fragment_login) {
    override val viewModel: LoginViewModel by viewModel()
    private val userStorage: UserStorage by inject()
    override fun initViews() {
        super.initViews()
        initListeners()
        loginAutomatic()
    }


    private fun loginAutomatic() {
        val loginData = userStorage.getLoginAndPassword()
        if (!(loginData.first.isEmpty() && loginData.second.isEmpty())) {
            user_name_or_email_edit_text.text = Editable.Factory().newEditable(loginData.first)
            password_edit_text.text = Editable.Factory().newEditable(loginData.second)
            viewModel.logIn(LoginDisplayable(loginData.first, loginData.second.toCharArray()),
                false)
        }
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
        login_button.visibility = View.VISIBLE
    }

    override fun onPendingState() {
        error_message_wrapper.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        login_button.visibility = View.INVISIBLE
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
        val userNameOrEmail = user_name_or_email_edit_text.text.toString()
        val password = password_edit_text.text.toString()
        var close = false
        var focusView = View(context)
        if (TextUtils.isEmpty(userNameOrEmail)) {
            user_name_or_email_edit_text_wrapper.error = getString(R.string.field_required_error)
            close = true
            focusView = user_name_or_email_edit_text
        }
        if (TextUtils.isEmpty(password)) {
            password_edit_text_wrapper.error = getString(R.string.field_required_error)
            close = true
            focusView = password_edit_text
        }

        if (close) {
            focusView.requestFocus()
        } else {
            viewModel.logIn(LoginDisplayable(userNameOrEmail, password.toCharArray()),
                save_details_checkbox.isChecked)
        }
    }
}
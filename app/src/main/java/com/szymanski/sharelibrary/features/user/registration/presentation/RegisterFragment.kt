package com.szymanski.sharelibrary.features.user.registration.presentation

import android.text.TextUtils
import android.view.View
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import com.szymanski.sharelibrary.features.user.registration.presentation.model.AddressDisplayable
import com.szymanski.sharelibrary.features.user.registration.presentation.model.UserDisplayable
import kotlinx.android.synthetic.main.fragment_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : BaseFragment<RegisterViewModel>(R.layout.fragment_register) {
    override val viewModel: RegisterViewModel by viewModel()

    override fun initViews() {
        super.initViews()
        initListeners()
    }

    private fun initListeners() {
        setSignInTextListener()
        setRegisterButtonListener()
    }

    private fun setRegisterButtonListener() {
        register_button?.setOnClickListener {
            attemptRegistration()
        }

    }

    override fun initObservers() {
        super.initObservers()
        viewModel.message.observe(this) {
            error_message_wrapper.visibility = View.VISIBLE
            error_message.text = it
        }
    }

    private fun attemptRegistration() {
        val fullName = fullNameEditText.text.toString()
        val userName = userNameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString().toCharArray()
        val street = streetEditText.text.toString()
        val buildingNumber = buildingNumberEditText.text.toString()
        val postalCode = postalCodeEditText.text.toString()
        val city = cityEditText.text.toString()
        val country = countryEditText.text.toString()
        var cancel = false
        var focusView = View(context)

        if (TextUtils.isEmpty(email)) {
            emailEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = emailEditText
        } else if (isEmailInvalid(email)) {
            emailEditText.error = getString(R.string.invalid_email_error)
            cancel = true
            focusView = emailEditText
        }
        if (TextUtils.isEmpty(userName)) {
            userNameEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = userNameEditText
        }
        if (TextUtils.isEmpty(fullName)) {
            fullNameEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = fullNameEditText
        }
        if (TextUtils.isEmpty(password.toString())) {
            passwordEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = passwordEditText
        } else if (isPasswordValid(password.toString())) {
            passwordEditText.error =
                getString(R.string.invalid_password_error)
            cancel = true
            focusView = passwordEditText
        }
        if (TextUtils.isEmpty(street)) {
            streetEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = streetEditText
        }
        if (TextUtils.isEmpty(buildingNumber)) {
            buildingNumberEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = buildingNumberEditText
        }
        if (TextUtils.isEmpty(city)) {
            cityEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = cityEditText
        }
        if (TextUtils.isEmpty(postalCode)) {
            postalCodeEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = postalCodeEditText
        }
        if (TextUtils.isEmpty(country)) {
            countryEditText.error = getString(R.string.field_required_error)
            cancel = true
            focusView = countryEditText
        }

        if (cancel) {
            focusView.requestFocus()
        } else {
            var name: CharSequence
            var surname: CharSequence = ""
            val fullNameTemp = fullNameEditText.text.toString().trim()
            if (fullNameTemp.contains(' ')) {
                val firstSpace = fullNameTemp.indexOfFirst { it == ' ' }
                name = fullNameTemp.subSequence(0 until firstSpace)
                surname = fullNameTemp.subSequence((firstSpace + 1) until fullNameTemp.length)
            } else {
                name = fullNameTemp
            }
            registerUser(UserDisplayable(
                id = null,
                name = name.toString(),
                surname = surname.toString(),
                username = userName,
                email = email,
                password = password,
                address = AddressDisplayable(
                    street = street,
                    buildingNumber = buildingNumber,
                    postalCode = postalCode,
                    city = city,
                    country = country,
                    coordinate = null
                )
            ))
        }
    }

    private fun registerUser(userDisplayable: UserDisplayable) {
        viewModel.registerUser(userDisplayable)
    }

    override fun onIdleState() {
        progress_bar.visibility = View.INVISIBLE
    }

    override fun onPendingState() {
        error_message_wrapper.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.contains("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")
    }

    private fun isEmailInvalid(email: String): Boolean {
        return !email.contains("@")
    }

    private fun setSignInTextListener() {
        signInButtonText?.setOnClickListener {
            viewModel.navigateToLoginScreen()
        }
    }
}
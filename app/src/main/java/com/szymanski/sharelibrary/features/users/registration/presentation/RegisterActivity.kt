package com.szymanski.sharelibrary.features.users.registration.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.szymanski.sharelibrary.R
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val viewModel: RegisterViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setListeners()
    }


    private fun attemptRegister() {

    }

    private fun setListeners() {
        signInButtonText?.setOnClickListener {
            onBackPressed()
        }
        registerButton?.setOnClickListener {

//            viewModel.registerUser()
        }

    }
}
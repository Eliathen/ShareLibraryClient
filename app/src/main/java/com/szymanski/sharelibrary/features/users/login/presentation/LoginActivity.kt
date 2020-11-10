package com.szymanski.sharelibrary.features.users.login.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.features.users.registration.presentation.RegisterActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent);
    }
}
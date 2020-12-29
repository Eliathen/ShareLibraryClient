package com.szymanski.sharelibrary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.szymanski.sharelibrary.features.home.presentation.all.HomeFragment
import com.szymanski.sharelibrary.features.user.presentation.login.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController

        setListenersForNavController(navController)

        navView.setupWithNavController(navController)
    }

    private fun setListenersForNavController(navController: NavController) {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.login_screen -> hideBottomNav()
                R.id.register_screen -> hideBottomNav()
                R.id.other_user_books_screen -> hideBottomNav()
                R.id.chat_room_screen -> hideBottomNav()
                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.visibility = View.GONE
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment
        val currentFragment = navHost.childFragmentManager.fragments.first()
        if (currentFragment::class.java == LoginFragment::class.java) {
            finish()
        } else if (currentFragment::class.java == HomeFragment::class.java && navHost.childFragmentManager.fragments.size == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

}
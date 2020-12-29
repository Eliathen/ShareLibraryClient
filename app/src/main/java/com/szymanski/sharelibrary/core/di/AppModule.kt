package com.szymanski.sharelibrary.core.di

import androidx.navigation.navOptions
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.exception.ErrorMapper
import com.szymanski.sharelibrary.core.exception.ErrorMapperImpl
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.core.exception.ErrorWrapperImpl
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.core.navigation.FragmentNavigatorImpl
import com.szymanski.sharelibrary.core.provider.ActivityProvider
import com.szymanski.sharelibrary.core.storage.preferences.UserStorage
import com.szymanski.sharelibrary.core.storage.preferences.UserStorageImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {

    factory { LinearLayoutManager(get()) }

    factory { DividerItemDecoration(get(), LinearLayoutManager.VERTICAL) }

    single(createdAtStart = true) {
        ActivityProvider(androidApplication())
    }
    factory<FragmentNavigator> {
        FragmentNavigatorImpl(
            activityProvider = get(),
            navHostFragmentRes = R.id.nav_host_fragment,
            homeHostFragmentRes = R.id.home_screen,
            defaultNavOptions = get()
        )
    }
    factory {
        navOptions {
            anim {
                enter = R.anim.fragment_fade_enter
                exit = R.anim.fragment_fade_exit
                popEnter = R.anim.fragment_close_enter
                popExit = R.anim.fragment_close_exit
            }
        }

    }
    factory<ErrorMapper> {
        ErrorMapperImpl(get())
    }
    factory<ErrorWrapper> {
        ErrorWrapperImpl()
    }
    factory {
        Gson()
    }
    single<UserStorage> {
        UserStorageImpl(get(), get())
    }
}
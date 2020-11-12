package com.szymanski.sharelibrary.core.di

import androidx.recyclerview.widget.LinearLayoutManager
import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.exceptions.ErrorMapper
import com.szymanski.sharelibrary.core.exceptions.ErrorMapperImpl
import com.szymanski.sharelibrary.core.navigation.FragmentNavigator
import com.szymanski.sharelibrary.core.navigation.FragmentNavigatorImpl
import com.szymanski.sharelibrary.core.provider.ActivityProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    factory { LinearLayoutManager(androidContext()) }

    single(createdAtStart = true) {
        ActivityProvider(androidApplication())
    }
    factory<FragmentNavigator> {
        FragmentNavigatorImpl(
            activityProvider = get(),
            navHostFragmentRes = R.id.nav_host_fragment,
            homeHostFragmentRes = R.id.books_screen
        )
    }
    factory<ErrorMapper> {
        ErrorMapperImpl()
    }

}
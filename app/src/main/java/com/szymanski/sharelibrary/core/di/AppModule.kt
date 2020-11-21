package com.szymanski.sharelibrary.core.di

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
import com.szymanski.sharelibrary.core.storage.local.UserStorage
import com.szymanski.sharelibrary.core.storage.local.UserStorageImpl
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
            homeHostFragmentRes = R.id.books_screen
        )
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
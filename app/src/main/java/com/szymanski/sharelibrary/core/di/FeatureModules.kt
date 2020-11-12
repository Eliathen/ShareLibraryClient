package com.szymanski.sharelibrary.core.di

import com.szymanski.sharelibrary.features.books.di.bookModule
import com.szymanski.sharelibrary.features.chat.di.chatModule
import com.szymanski.sharelibrary.features.exchanges.di.exchangesModule
import com.szymanski.sharelibrary.features.users.di.userModule

val featureModules = listOf(
    userModule,
    bookModule,
    chatModule,
    exchangesModule
)
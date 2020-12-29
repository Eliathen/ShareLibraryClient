package com.szymanski.sharelibrary.core.di

import com.szymanski.sharelibrary.features.book.di.bookModule
import com.szymanski.sharelibrary.features.chat.di.chatModule
import com.szymanski.sharelibrary.features.exchange.di.exchangesModule
import com.szymanski.sharelibrary.features.home.di.homeModule
import com.szymanski.sharelibrary.features.user.di.userModule

val featureModules = listOf(
    userModule,
    bookModule,
    chatModule,
    exchangesModule,
    homeModule
)
package com.szymanski.sharelibrary.features.chat.di

import com.szymanski.sharelibrary.features.chat.data.ChatRepositoryImpl
import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetUserRoomsUseCase
import com.szymanski.sharelibrary.features.chat.presentation.all.ChatAdapter
import com.szymanski.sharelibrary.features.chat.presentation.all.ChatViewModel
import com.szymanski.sharelibrary.features.chat.presentation.room.ChatRoomAdapter
import com.szymanski.sharelibrary.features.chat.presentation.room.ChatRoomViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    //viewModel
    viewModel { ChatViewModel(get(), get(), get()) }
    viewModel { ChatRoomViewModel() }

    //useCase
    factory { GetUserRoomsUseCase(get()) }
    //repository
    factory<ChatRepository> { ChatRepositoryImpl(get()) }

    //adapters
    factory { ChatAdapter() }
    factory { ChatRoomAdapter() }

}
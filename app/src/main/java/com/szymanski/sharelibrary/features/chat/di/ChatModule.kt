package com.szymanski.sharelibrary.features.chat.di

import com.szymanski.sharelibrary.features.chat.data.ChatRepositoryImpl
import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetRoomBySenderIdAndRecipientIdUseCase
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetRoomMessagesUseCase
import com.szymanski.sharelibrary.features.chat.domain.usecase.GetUserRoomsUseCase
import com.szymanski.sharelibrary.features.chat.navigation.ChatNavigation
import com.szymanski.sharelibrary.features.chat.navigation.ChatNavigationImpl
import com.szymanski.sharelibrary.features.chat.presentation.all.ChatAdapter
import com.szymanski.sharelibrary.features.chat.presentation.all.ChatViewModel
import com.szymanski.sharelibrary.features.chat.presentation.room.ChatRoomAdapter
import com.szymanski.sharelibrary.features.chat.presentation.room.ChatRoomViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    viewModel { ChatViewModel(get(), get(), get(), get()) }
    viewModel { ChatRoomViewModel(get(), get(), get(), get(), get()) }

    factory { GetUserRoomsUseCase(get()) }
    factory { GetRoomMessagesUseCase(get()) }
    factory { GetRoomBySenderIdAndRecipientIdUseCase(get()) }

    factory<ChatRepository> { ChatRepositoryImpl(get(), get()) }

    factory { ChatAdapter() }
    factory { ChatRoomAdapter() }

    factory<ChatNavigation> { ChatNavigationImpl(get()) }

}
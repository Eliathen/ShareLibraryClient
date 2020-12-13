package com.szymanski.sharelibrary.features.chat.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import com.szymanski.sharelibrary.features.chat.domain.model.Room

class GetUserRoomsUseCase(
    private val chatRepository: ChatRepository,
) : BaseUseCase<List<Room>, Long>() {
    override suspend fun action(params: Long): List<Room> {
        return chatRepository.getUserChatRooms(params)
    }
}
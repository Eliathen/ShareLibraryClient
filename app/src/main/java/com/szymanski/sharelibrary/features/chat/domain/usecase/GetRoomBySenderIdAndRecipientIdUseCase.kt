package com.szymanski.sharelibrary.features.chat.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import com.szymanski.sharelibrary.features.chat.domain.model.Room

class GetRoomBySenderIdAndRecipientIdUseCase(
    private val chatRepository: ChatRepository,
) : BaseUseCase<Room, Pair<Long, Long>>() {
    override suspend fun action(params: Pair<Long, Long>): Room {
        return chatRepository.getRoomBySenderIdAndRecipientId(params.first, params.second)
    }
}
package com.szymanski.sharelibrary.features.chat.domain.usecase

import com.szymanski.sharelibrary.core.base.BaseUseCase
import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import com.szymanski.sharelibrary.features.chat.domain.model.Message

class GetRoomMessagesUseCase(
    private val chatRepository: ChatRepository,
) : BaseUseCase<List<Message>, Long>() {
    override suspend fun action(params: Long): List<Message> {
        return chatRepository.getRoomMessages(params)
    }

}

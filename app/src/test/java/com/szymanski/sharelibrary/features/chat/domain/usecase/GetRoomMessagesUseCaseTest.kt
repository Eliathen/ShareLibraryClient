package com.szymanski.sharelibrary.features.chat.domain.usecase

import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetRoomMessagesUseCaseTest {
    @Test
    fun `when use case is invoked than execute getRoomMessages from repository`() {
        //given
        val repository = mockk<ChatRepository>(relaxed = true)
        val useCase = GetRoomMessagesUseCase(repository)
        val roomId = 1L
        //when
        useCase(
            scope = GlobalScope,
            params = roomId
        )
        //then
        coVerify { repository.getRoomMessages(roomId) }
    }
}
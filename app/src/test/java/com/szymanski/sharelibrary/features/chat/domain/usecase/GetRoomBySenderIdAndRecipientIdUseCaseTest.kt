package com.szymanski.sharelibrary.features.chat.domain.usecase

import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetRoomBySenderIdAndRecipientIdUseCaseTest {
    @Test
    fun `when use case is invoked than execute getRoomBySenderIdAndRecipientId from repository`() {
        //given
        val repository = mockk<ChatRepository>(relaxed = true)
        val useCase = GetRoomBySenderIdAndRecipientIdUseCase(repository)
        val senderId = 1L
        val recipientId = 2L
        val params = Pair(senderId, recipientId)
        //when
        useCase(
            scope = GlobalScope,
            params = params
        )
        //then
        coVerify { repository.getRoomBySenderIdAndRecipientId(senderId, recipientId) }
    }
}
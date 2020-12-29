package com.szymanski.sharelibrary.features.chat.domain.usecase

import com.szymanski.sharelibrary.features.chat.domain.ChatRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import org.junit.jupiter.api.Test

internal class GetUserRoomsUseCaseTest {
    @Test
    fun `when use case is invoked than execute getUserRooms from repository`() {
        //given
        val repository = mockk<ChatRepository>(relaxed = true)
        val useCase = GetUserRoomsUseCase(repository)
        val userId = 1L
        //when
        useCase(
            scope = GlobalScope,
            params = userId
        )
        //then
        coVerify { repository.getUserChatRooms(userId) }

    }
}
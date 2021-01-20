package com.szymanski.sharelibrary.features.chat.data

import com.szymanski.sharelibrary.core.api.Api
import com.szymanski.sharelibrary.core.api.model.response.ChatRoomResponse
import com.szymanski.sharelibrary.core.api.model.response.MessageResponse
import com.szymanski.sharelibrary.core.exception.ErrorWrapper
import com.szymanski.sharelibrary.mock.mock
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ChatRepositoryImplTest {


    @Test
    fun `WHEN request get user chat rooms THEN fetch chat rooms from API `() {
        //given
        val userId = 1L
        val api = mockk<Api> {
            coEvery { getRooms(userId) } returns listOf(ChatRoomResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = ChatRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getUserChatRooms(userId)
        }
        //then
        coVerify {
            api.getRooms(userId)
        }
    }

    @Test
    fun `WHEN request get room messages THEN fetch messages from API `() {
        //given
        val roomId = 1L
        val api = mockk<Api> {
            coEvery { getRoomMessages(roomId) } returns listOf(MessageResponse.mock())
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = ChatRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getRoomMessages(roomId)
        }
        //then
        coVerify {
            api.getRoomMessages(roomId)
        }
    }

    @Test
    fun `WHEN request get room by sender id and recipient id THEN fetch room from API `() {
        //given
        val senderId = 1L
        val recipientId = 2L
        val api = mockk<Api> {
            coEvery {
                getRoomBySenderIdAndRecipientId(senderId,
                    recipientId)
            } returns ChatRoomResponse.mock()
        }
        val errorWrapper = mockk<ErrorWrapper>(relaxed = true)
        val repository = ChatRepositoryImpl(api, errorWrapper)
        //when
        runBlocking {
            repository.getRoomBySenderIdAndRecipientId(senderId, recipientId)
        }
        //then
        coVerify {
            api.getRoomBySenderIdAndRecipientId(senderId, recipientId)
        }
    }
}
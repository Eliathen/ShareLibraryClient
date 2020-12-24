package com.szymanski.sharelibrary.core.api.model.request

data class SendMessageRequest(
    val chatId: Long? = null,

    val senderId: Long? = null,

    val recipientId: Long? = null,

    val content: String? = null,
)


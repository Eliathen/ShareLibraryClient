package com.szymanski.sharelibrary.features.chat.presentation

import com.szymanski.sharelibrary.R
import com.szymanski.sharelibrary.core.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : BaseFragment<ChatViewModel>(R.layout.fragment_chat) {

    override val viewModel: ChatViewModel by viewModel()


}
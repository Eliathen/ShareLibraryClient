package com.szymanski.sharelibrary.core.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : BaseViewModel>(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    abstract val viewModel: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        bindViewModelToLifeCycle()
    }

    private fun bindViewModelToLifeCycle() {
        lifecycle.addObserver(viewModel)
    }

    open fun initViews() {

    }

    open fun initObservers() {
        observeUiState()
        observeMessage()
    }

    private fun observeMessage() {
        viewModel.message.observe(viewLifecycleOwner) {
            showToast(it)
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                UiState.Idle -> {
                    onIdleState()
                }
                UiState.Pending -> {
                    onPendingState()
                }
            }
        }
    }

    protected fun showToast(message: String?) {
        Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showToast(stringResources: Int) {
        showToast(getString(stringResources))
    }

    open fun onIdleState() {}

    open fun onPendingState() {}
}
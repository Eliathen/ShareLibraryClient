package com.szymanski.sharelibrary.utils

import androidx.arch.core.executor.TaskExecutor

object FakeMainThreadExecutor : TaskExecutor() {
    override fun executeOnDiskIO(runnable: Runnable) {
        runnable.run()
    }

    override fun postToMainThread(runnable: Runnable) {
        runnable.run()
    }

    override fun isMainThread(): Boolean {
        return true
    }
}
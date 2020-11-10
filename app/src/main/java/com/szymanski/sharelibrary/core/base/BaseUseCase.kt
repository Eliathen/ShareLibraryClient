package com.szymanski.sharelibrary.core.base

import kotlinx.coroutines.*

abstract class BaseUseCase<out Type, in Params> {


    abstract suspend fun action(params: Params): Type

    operator fun invoke(
        scope: CoroutineScope,
        executeDispatcher: CoroutineDispatcher = Dispatchers.IO,
        params: Params,
        onResult: (Result<Type>) -> Unit = {},
    ) {
        scope.launch {
            val result = withContext(executeDispatcher) {
                kotlin.runCatching {
                    action(params = params)
                }
            }
            onResult(result)
        }
    }
}
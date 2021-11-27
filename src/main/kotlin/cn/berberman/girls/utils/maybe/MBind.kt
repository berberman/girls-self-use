package cn.berberman.girls.utils.maybe

import cn.berberman.girls.utils.BindException
import kotlin.coroutines.*

object NotationScopeMaybe {
    fun <T> pure(value: T) = Maybe.from(value)

    suspend fun <T> bind(value: Maybe<T>): T {
        return suspendCoroutine {
            it.resumeWith(
                if (value.isPresent)
                    Result.success(value.value)
                else
                    Result.failure(BindException)
            )
        }
    }
}

fun <T> Maybe.Companion.fx(lambda: suspend NotationScopeMaybe.() -> Maybe<T>): Maybe<T> {
    var r = empty<T>()
    lambda.startCoroutine(NotationScopeMaybe, object : Continuation<Maybe<T>> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<Maybe<T>>) {
            result
                .onSuccess { r = it }
                .onFailure {
                    if (it !is BindException)
                        throw it
                }
        }
    })
    return r
}
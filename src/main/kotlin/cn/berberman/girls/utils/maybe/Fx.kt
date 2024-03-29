package cn.berberman.girls.utils.maybe

import kotlin.coroutines.*

private object BindException : Exception()

object MaybeFx {
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

    @JvmName("bindExt")
    suspend fun <T> Maybe<T>.bind() = bind(this)
    suspend fun <T> T?.bindNullable() = bind(toMaybe())
}

fun <T> Maybe.Companion.fx(lambda: suspend MaybeFx.() -> Maybe<T>): Maybe<T> {
    var r = empty<T>()
    lambda.startCoroutine(MaybeFx, object : Continuation<Maybe<T>> {
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
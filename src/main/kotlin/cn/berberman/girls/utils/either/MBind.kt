package cn.berberman.girls.utils.either

import cn.berberman.girls.utils.BindException
import kotlin.coroutines.*

object NotationScopeEither {
    fun <L, R> `return`(value: R) = Either.right<L, R>(value)

    suspend fun <R> bind(value: Either<Any?, R>): R {
        return suspendCoroutine {
            it.resumeWith(
                if (value is Right)
                    Result.success(value.value)
                else
                    Result.failure(BindException)
            )
        }
    }
}

fun <R> Either.Companion.fx(lambda: suspend NotationScopeEither.() -> Either<Any?, R>): Either<Any?, R> {
    var r = left<Any?, R>(null)
    lambda.startCoroutine(NotationScopeEither, object : Continuation<Either<Any?, R>> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<Either<Any?, R>>) {
            result
                .onSuccess { r = it }
                .onFailure {
                    if (it !is BindException)
                        throw it
                }
        }
    })
    @Suppress("UNCHECKED_CAST")
    return r
}
package cn.berberman.girls.utils.either

import kotlin.coroutines.*

private class BindException(val failureValue: Either<*, *>) : Exception()

class EitherFx<L, R> {
    fun pure(value: R) = Either.right<L, R>(value)

    suspend fun <T> bind(value: Either<L, T>): T {
        return suspendCoroutine {
            it.resumeWith(
                if (value is Right)
                    Result.success(value.value)
                else
                    Result.failure(BindException(value))
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun <L, R> Either.Companion.fx(lambda: suspend EitherFx<L, R>.() -> Either<L, R>): Either<L, R> {
    var r: Either<L, R>? = null
    lambda.startCoroutine(EitherFx(), object : Continuation<Either<L, R>> {
        override val context: CoroutineContext = EmptyCoroutineContext

        override fun resumeWith(result: Result<Either<L, R>>) {
            result
                .onSuccess { r = it }
                .onFailure {
                    if (it is BindException)
                        r = it.failureValue as Either<L, R>?
                    else
                        throw it
                }
        }
    })
    return r!!
}
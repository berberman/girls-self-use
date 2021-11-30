package cn.berberman.girls.utils.either


inline fun <L, R> Either<L, R>.then(block: (R) -> Unit) = apply {
    if (this is Right)
        block(value)
}

inline fun <L, R> Either<L, R>.otherwise(block: (L) -> Unit) = apply {
    if (this is Left)
        block(value)
}

fun <L, R> Either<L, R>.valueOrNull(): R? =
    when (this) {
        is Left  -> null
        is Right -> value
    }

fun <L, R> wrapEither(value: R) = Right<L, R>(value)

fun <T, R, U> either(f: (T) -> U, g: (R) -> U, e: Either<T, R>) =
    when (e) {
        is Left  -> f(e.value)
        is Right -> g(e.value)
    }

inline fun <T, reified L : Throwable> Result<T>.toEither() =
    if (isSuccess)
        Either.right<Throwable, T>(getOrThrow())
    else
    // throw cast error if the throwable is unexpected
        Either.left(exceptionOrNull()!! as L)

inline fun <reified L : Throwable, T> Result<T>.specify() =
    if (isSuccess)
        Either.right<L, T>(getOrThrow())
    else
        Either.left(exceptionOrNull() as L)


inline fun <reified L : Throwable, R> runCatchingEither(block: () -> R): Either<L, R> =
    try {
        wrapEither(block())
    } catch (e: Throwable) {
        if (e is L)
            Left(e)
        else
            throw e
    }

inline fun <T, reified L : Throwable, R> T.runCatchingEither(block: T.() -> R): Either<L, R> =
    try {
        wrapEither(block())
    } catch (e: Throwable) {
        if (e is L)
            Left(e)
        else
            throw e
    }
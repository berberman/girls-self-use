package cn.berberman.girls.utils.maybe

import cn.berberman.girls.utils.curried
import java.util.*


fun <T> Maybe<Maybe<T>>.join() =
    flatMap { a -> a.flatMap { b -> Maybe.from(b) } }

inline fun <T> Maybe<T>.then(block: (T) -> Unit) = apply {
    if (isPresent) block(value)
}

inline fun <T> Maybe<T>.otherwise(block: () -> Unit) = apply {
    if (!isPresent) block()
}

fun <T, R> maybe(y: R, f: (T) -> R, mx: Maybe<T>) =
    if (mx.isPresent)
        f(mx.value)
    else y

fun <T> fromMaybe(y: T, mx: Maybe<T>): T = maybe(y, { it }, mx)

fun <T> T?.toMaybe(): Maybe<T> = Maybe.fromNullable(this)

fun <T> wrapMaybe(value: T): Maybe<T> = Just(value)

fun <T> Optional<T>.toMaybe() =
    if (isPresent)
        wrapMaybe(get()!!)
    else Nothing<T>()

inline fun <T> T.justIf(predicate: (T) -> Boolean) =
    takeIf(predicate).toMaybe()

inline fun <T> T.justUnless(predicate: (T) -> Boolean) =
    takeUnless(predicate).toMaybe()

fun <T1, T2, R> liftA2(t1: Maybe<T1>, t2: Maybe<T2>, f: ((T1, T2) -> R)) =
    t2 ap t1.map { a -> f.curried()(a) }

fun <T1, T2, T3, R> liftA3(t1: Maybe<T1>, t2: Maybe<T2>, t3: Maybe<T3>, f: ((T1, T2, T3) -> R)) =
    t3 ap t2.ap(t1.map { a -> f.curried()(a) })

fun <T1, T2, T3, T4, R> liftA4(t1: Maybe<T1>, t2: Maybe<T2>, t3: Maybe<T3>, t4: Maybe<T4>, f: ((T1, T2, T3, T4) -> R)) =
    t4 ap t3.ap(t2 ap t1.map { a -> f.curried()(a) })

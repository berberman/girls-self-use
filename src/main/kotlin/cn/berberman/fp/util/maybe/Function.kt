package cn.berberman.fp.util.maybe

import cn.berberman.fp.util.curried


fun <T> Maybe<Maybe<T>>.join() =
    flatMap { a -> a.flatMap { b -> Maybe.from(b) } }

inline fun <T> Maybe<T>.then(block: (T) -> Unit) = apply {
    if (isPresent) block(value)
}

inline fun <T> Maybe<T>.otherwise(block: () -> Unit) = apply {
    if (!isPresent) block()
}

fun <T> T?.maybe() = Maybe.fromNullable(this)

fun <T> maybe(value: T) = Just(value)

fun <T1, T2, R> liftA2(t1: Maybe<T1>, t2: Maybe<T2>, f: ((T1, T2) -> R)) =
    t2 ap t1.map { a -> f.curried()(a) }

fun <T1, T2, T3, R> liftA3(t1: Maybe<T1>, t2: Maybe<T2>, t3: Maybe<T3>, f: ((T1, T2, T3) -> R)) =
    t3 ap t2.ap(t1.map { a -> f.curried()(a) }
    )

fun <T1, T2, T3, T4, R> liftA4(t1: Maybe<T1>, t2: Maybe<T2>, t3: Maybe<T3>, t4: Maybe<T4>, f: ((T1, T2, T3, T4) -> R)) =
    t4 ap t3.ap(t2 ap t1.map { a -> f.curried()(a) })

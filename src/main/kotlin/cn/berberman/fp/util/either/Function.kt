package cn.berberman.fp.util.either

import cn.berberman.fp.util.curried


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
        is Left -> null
        is Right -> value
    }

fun <T> T.either() = Right<Any?, T>(this)

fun <L, R1, R2, R> liftA2(t1: Either<L, R1>, t2: Either<L, R2>, f: ((R1, R2) -> R)) =
    t2 ap t1.map { a -> f.curried()(a) }

fun <L, R1, R2, R3, R> liftA3(t1: Either<L, R1>, t2: Either<L, R2>, t3: Either<L, R3>, f: ((R1, R2, R3) -> R)) =
    t3 ap t2.ap(t1.map { a -> f.curried()(a) }
    )

fun <L, R1, R2, R3, R4, R> liftA4(
    t1: Either<L, R1>,
    t2: Either<L, R2>,
    t3: Either<L, R3>,
    t4: Either<L, R4>,
    f: ((R1, R2, R3, R4) -> R)
) =
    t4 ap t3.ap(t2 ap t1.map { a -> f.curried()(a) })

package cn.berberman.fp.util.state

import cn.berberman.fp.util.curried

fun <T> get(): State<T, T> = state { it to it }

fun <T> put(a: T): State<T, Unit> = state { Unit to a }

fun <T, R1, R2, R> liftA2(t1: State<T, R1>, t2: State<T, R2>, f: ((R1, R2) -> R)): State<T, R> =
    t2 ap t1.map { a -> f.curried()(a) }

fun <T, R1, R2, R3, R> liftA3(
    t1: State<T, R1>,
    t2: State<T, R2>,
    t3: State<T, R3>,
    f: ((R1, R2, R3) -> R)
): State<T, R> =
    t3 ap t2.ap(t1.map { a -> f.curried()(a) })

fun <T, R1, R2, R3, R4, R> liftA4(
    t1: State<T, R1>,
    t2: State<T, R2>,
    t3: State<T, R3>,
    t4: State<T, R4>,
    f: ((R1, R2, R3, R4) -> R)
): State<T, R> =
    t4 ap t3.ap(t2 ap t1.map { a -> f.curried()(a) })


package cn.berberman.fp.util

inline infix fun <P1, P2, R> ((P2) -> R).compose(crossinline f: (P1) -> P2) =
    { p1: P1 ->
        this(f(p1))
    }
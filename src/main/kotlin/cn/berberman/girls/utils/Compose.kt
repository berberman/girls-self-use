package cn.berberman.girls.utils

inline infix fun <P1, P2, R> ((P2) -> R).compose(crossinline f: (P1) -> P2) =
    { p1: P1 ->
        this(f(p1))
    }

@JvmName("identityExt")
fun <T> T.identity() = this

@JvmName("identity")
fun <T> identity(x: T) = x
package cn.berberman.fp.util.state

interface State<T, R> {

    fun runState(state: T): Pair<R, T>

    infix fun <S> map(f: (R) -> S): State<T, S> = state {
        val (r, s) = runState(it)
        f(r) to s
    }

    infix fun <S> ap(f: State<T, (R) -> S>): State<T, S> =
        state {
            val (a, s) = runState(it)
            val (frs, s1) = f.runState(s)
            frs(a) to s1
        }

    infix fun <S> flatMap(f: (R) -> State<T, S>): State<T, S> =
        state {
            val (a, s) = runState(it)
            f(a).runState(s)
        }

}

fun <T, R> state(f: (T) -> Pair<R, T>): State<T, R> = object : State<T, R> {
    override fun runState(state: T): Pair<R, T> = f(state)
}


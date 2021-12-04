package cn.berberman.girls.utils.state

import kotlin.coroutines.*

class StateFx<T> : Continuation<State<T, *>> {

    internal lateinit var r: State<T, *>

    override val context: CoroutineContext = EmptyCoroutineContext

    override fun resumeWith(result: Result<State<T, *>>) {
        r = result.getOrThrow()
    }

    fun <R> pure(x: R) = State<T, R> { x to it }

    suspend fun <R> bind(value: State<T, R>): R = suspendCoroutine { cont ->
        r = value.flatMap {
            cont.resume(it)
            r
        }
    }

}

@Suppress("UNCHECKED_CAST")
fun <T, R> State.Companion.fx(block: suspend StateFx<T>.() -> State<T, R>): State<T, R> {
    val scope = StateFx<T>()
    block.startCoroutine(scope, scope)
    return scope.r as State<T, R>
}
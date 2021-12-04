package cn.berberman.girls.utils

import cn.berberman.girls.utils.state.State
import cn.berberman.girls.utils.state.fx
import org.junit.Assert
import org.junit.Test

class TestState {
    @Test
    fun testFx() {
        val inc = State.modify<Int> { it + 1 }
        val s = State.fx<Int, String> {
            bind(inc)
            bind(inc)
            bind(inc)
            val finalState = bind(State.get())
            pure("Final state is $finalState")
        }
        Assert.assertEquals("Final state is 3" to 3, s.runState(0))
    }
}
package cn.berberman.girls.utils

import cn.berberman.girls.utils.maybe.*
import cn.berberman.girls.utils.maybe.Nothing
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class TestMaybe {

    @Test
    fun testMap() {
        val x = wrapMaybe(233)
        assertEquals(wrapMaybe(234), x map { it.inc() })
    }

    @Test
    fun testAp() {
        val x = wrapMaybe(233)
        val y = { i: Int -> i.inc() }.maybe()
        assertEquals(wrapMaybe(234), x ap y)
    }

    @Test
    fun testFlatMap() {
        val x = wrapMaybe(233)
        assertEquals(wrapMaybe(234), x flatMap { wrapMaybe(it.inc()) })
        assertEquals(Nothing(), x flatMap { Nothing<Int>() })
    }

    @Test
    fun testNotation() {
        val x = wrapMaybe(233)
        val y = wrapMaybe(123)
        assertEquals(
            wrapMaybe(356),
            Maybe.fx {
                val xx = bind(x)
                val yy = bind(y)
                pure(xx + yy)
            }
        )
        assertEquals(
            Nothing(),
            Maybe.fx {
                val xx = bind(x)
                val yy = bind(Maybe.empty<Int>())
                pure(xx + yy)
            }
        )
    }

    fun t(){

    }
}
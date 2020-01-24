package cn.berberman.fp.util

import cn.berberman.fp.util.maybe.Nothing
import cn.berberman.fp.util.maybe.`do`
import cn.berberman.fp.util.maybe.bind
import cn.berberman.fp.util.maybe.maybe
import org.junit.Test
import kotlin.test.assertEquals

class TestMaybe {

    @Test
    fun testMap() {
        val x = maybe(233)
        assertEquals(maybe(234), x map { it.inc() })
    }

    @Test
    fun testAp() {
        val x = maybe(233)
        val y = { i: Int -> i.inc() }.maybe()
        assertEquals(maybe(234), x ap y)
    }

    @Test
    fun testFlatMap() {
        val x = maybe(233)
        assertEquals(maybe(234), x flatMap { maybe(it.inc()) })
        assertEquals(Nothing(), x flatMap { Nothing<Int>() })
    }

    @Test
    fun testNotation() {
        val x = maybe(233)
        val y = maybe(123)
        assertEquals(
            maybe(356),
            `do` {
                val xx = bind(x)
                val yy = bind(y)
                `return`(xx + yy)
            }
        )
        assertEquals(
            Nothing(),
            `do` {
                val xx = bind(x)
                val yy = bind(Nothing<Int>())
                `return`(xx + yy)
            }
        )
    }
}
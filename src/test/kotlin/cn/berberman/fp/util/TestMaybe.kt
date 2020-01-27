package cn.berberman.fp.util

import cn.berberman.fp.util.maybe.Nothing
import cn.berberman.fp.util.maybe.`do`
import cn.berberman.fp.util.maybe.bind
import cn.berberman.fp.util.maybe.maybe
import cn.berberman.fp.util.maybe.wrapMaybe
import org.junit.Test
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
package cn.berberman.girls.utils

import cn.berberman.girls.utils.maybe.*
import cn.berberman.girls.utils.maybe.Nothing
import org.junit.Assert
import org.junit.Test

class TestPatternMatchingMaybe {
    @Test
    fun testJust() {
        val m = Just(233)
        val a = m.patternMatching<Int, String>()
        val b = a.onJust { "Just: $it" }
        val c = b.onNothing { "Nothing" }
        val d = c.eval()
        val e = a.onNothing { "Nothing" }
        val f = e.onJust { "Just: $it" }
        val g = f.eval()
        Assert.assertEquals(d, g)
    }

    @Test
    fun testNothing() {
        val m = Nothing<Int>()
        val a = m.patternMatching<Int, String>()
        val b = a.onJust { "Just: $it" }
        val c = b.onNothing { "Nothing" }
        val d = c.eval()
        val e = a.onNothing { "Nothing" }
        val f = e.onJust { "Just: $it" }
        val g = f.eval()
        Assert.assertEquals(d, g)
    }
}
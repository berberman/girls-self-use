package cn.berberman.girls.utils

import cn.berberman.girls.utils.either.*
import org.junit.Assert
import org.junit.Test


class TestPatternMatchingEither {
    @Test
    fun testRight() {
        val e = Either.right<Double, Int>(233)
        val start = e.patternMatching<Double, Int, String>()
        // set on left
        val b = start.onLeft { "Double: $it" }
        // set on right
        val c = b.onRight { "Int: $it" }
        // run pattern matching
        val d = c.eval()

        // similarly, but set on right first
        val f = start.onRight { "Int: $it" }
        // set on left
        val g = f.onLeft { "Double: $it" }
        // run pattern matching
        val h = g.eval()

        // no eval available if pattern matching is non-exhaustive
        // val i = f.eval()

        Assert.assertEquals(d, h)
    }

    @Test
    fun testLeft() {
        val e = Either.left<Double, Int>(233.3)
        val start = e.patternMatching<Double, Int, String>()
        // set on left
        val b = start.onLeft { "Double: $it" }
        // set on right
        val c = b.onRight { "Int: $it" }
        // run pattern matching
        val d = c.eval()

        // similarly, but set on right first
        val f = start.onRight { "Int: $it" }
        // set on left
        val g = f.onLeft { "Double: $it" }
        // run pattern matching
        val h = g.eval()

        // no eval available if pattern matching is non-exhaustive
        // val i = f.eval()

        Assert.assertEquals(d, h)
    }
}
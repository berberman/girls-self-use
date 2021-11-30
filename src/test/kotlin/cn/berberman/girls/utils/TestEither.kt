package cn.berberman.girls.utils

import cn.berberman.girls.utils.either.Either
import cn.berberman.girls.utils.either.Left
import cn.berberman.girls.utils.either.runCatchingEither
import cn.berberman.girls.utils.either.toEither
import org.junit.Assert
import org.junit.Test

class TestEither {
    class DummyException : Exception()

    @Test
    fun testException() {
        // catches expected exception
        val x: Either<DummyException, Int> = runCatchingEither { throw DummyException() }
        Assert.assertTrue(x is Left)

        // doesn't catch unexpected exception
        val y = runCatching {
            runCatchingEither<DummyException, Int> { throw RuntimeException() }
        }
        Assert.assertTrue(y.isFailure)

        // converts Result to Either
        val z = y.toEither<Any?, RuntimeException>()
        Assert.assertTrue(z is Left)

        // throw runtime exception if throwable type is unexpected
        val q = runCatching { y.toEither<Any?, DummyException>() }
        Assert.assertTrue(q.isFailure)

    }
}

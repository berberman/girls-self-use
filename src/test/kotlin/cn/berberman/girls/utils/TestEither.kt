package cn.berberman.girls.utils

import cn.berberman.girls.utils.either.*
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

    @Test
    fun testFx() {
        val e0 = Either.left<String, Int>("Left0")
        val e1 = Either.right<String, Int>(233)
        // do { Left x } == Left x
        Assert.assertEquals(e0, Either.fx<String, Int> { e0 })
        // do { x <- Left x; pure x } == Left x
        Assert.assertEquals(e0, Either.fx<String, Int> { pure(bind(e0)) })
        // do { Right x } == Right x
        Assert.assertEquals(e1, Either.fx<String, Int> { e1 })
        // do { x <- Right x; pure x } == Right x
        Assert.assertEquals(e1, Either.fx<String, Int> { pure(bind(e1)) })
        // do { x <- Right x; y <- Right y; pure (x + y) } == Right (x + y)
        Assert.assertEquals(e0, Either.fx<String, Int> {
            val x = bind(e0)
            val y = bind(e1)
            pure(x + y)
        })
        val e2 = Either.right<String, Int>(666)
        // Right x >>= \x -> Left y >>= \y -> pure (x + y) == do { x <- Right x; y <- Right y; pure (x + y) }
        Assert.assertEquals(e1.flatMap { a -> e2.flatMap { b -> Either.right(a + b) } }, Either.fx<String, Int> {
            val a = bind(e1)
            val b = bind(e2)
            pure(a + b)
        })
        val e3 = Either.left<String, Int>("Left1")
        // do { _ <- Right x; _ <- Left y; error "This exception won't be thrown!" } == Left y
        Assert.assertEquals(e3, Either.fx<String, Int> {
            bind(e1)
            bind(e3)
            throw RuntimeException("This exception won't be thrown!")
        })
    }
}

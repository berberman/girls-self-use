package cn.berberman.girls.utils.either

class PatternMatchingEither<T, R, U, S : PatternMatchingEither.Stage> internal constructor(internal val core: Either<T, R>) {

    sealed interface Stage {
        interface Pending : Stage
        interface SetLeft : Stage
        interface SetRight : Stage
        interface Ready : Stage
    }

    internal var onLeft: ((T) -> U)? = null
    internal var onRight: ((R) -> U)? = null

    internal companion object {
        fun <T, R, U> new(core: Either<T, R>) = PatternMatchingEither<T, R, U, Stage.Pending>(core)
    }

}

@JvmName("eitherPending2SetLeft")
fun <T, R, U> PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.Pending>.onLeft(block: (T) -> U) =
    PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.SetLeft>(core).apply {
        onLeft = block
    }

@JvmName("eitherSetRight2SetLeft")
fun <T, R, U> PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.SetRight>.onLeft(block: (T) -> U) =
    PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.Ready>(core).apply {
        onLeft = block
        onRight = this@onLeft.onRight
    }

@JvmName("eitherPending2SetRight")
fun <T, R, U> PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.Pending>.onRight(block: (R) -> U) =
    PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.SetRight>(core).apply {
        onRight = block
    }

@JvmName("eitherSetLeft2SetLeft")
fun <T, R, U> PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.SetLeft>.onRight(block: (R) -> U) =
    PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.Ready>(core).apply {
        onRight = block
        onLeft = this@onRight.onLeft
    }

fun <T, R, U> PatternMatchingEither<T, R, U, PatternMatchingEither.Stage.Ready>.eval() =
    either(onLeft!!, onRight!!, core)


fun <T, R, U> Either<T, R>.patternMatching() = PatternMatchingEither.new<T, R, U>(this)


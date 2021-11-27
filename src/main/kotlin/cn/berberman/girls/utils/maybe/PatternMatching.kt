package cn.berberman.girls.utils.maybe

class PatternMatchingMaybe<T, R, S : PatternMatchingMaybe.Stage> internal constructor(internal val core: Maybe<T>) {

    sealed interface Stage {
        interface Pending : Stage
        interface SetJust : Stage
        interface SetNothing : Stage
        interface Ready : Stage
    }

    internal var onJust: ((T) -> R)? = null
    internal var onNothing: (() -> R)? = null

    internal companion object {
        fun <T, R> new(core: Maybe<T>) = PatternMatchingMaybe<T, R, Stage.Pending>(core)
    }

}

@JvmName("maybePending2SetJust")
fun <T, R> PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.Pending>.onJust(block: (T) -> R) =
    PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.SetJust>(core).apply {
        onJust = block
    }

@JvmName("maybeSetNothing2SetJust")
fun <T, R> PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.SetNothing>.onJust(block: (T) -> R) =
    PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.Ready>(core).apply {
        onJust = block
        onNothing = this@onJust.onNothing
    }

@JvmName("maybePending2SetNothing")
fun <T, R> PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.Pending>.onNothing(block: () -> R) =
    PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.SetNothing>(core).apply {
        onNothing = block
    }

@JvmName("maybeSetJust2SetNothing")
fun <T, R> PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.SetJust>.onNothing(block: () -> R) =
    PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.Ready>(core).apply {
        onNothing = block
        onJust = this@onNothing.onJust
    }

fun <T, R> PatternMatchingMaybe<T, R, PatternMatchingMaybe.Stage.Ready>.eval() =
    maybe(onNothing!!(), onJust!!, core)


fun <T, R> Maybe<T>.patternMatching() = PatternMatchingMaybe.new<T, R>(this)


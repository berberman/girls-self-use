package cn.berberman.fp.util.maybe

sealed class Maybe<T> {

    abstract val value: T

    abstract val isPresent: Boolean

    abstract infix fun <R> map(f: (T) -> R): Maybe<R>

    abstract infix fun <R> ap(f: Maybe<(T) -> R>): Maybe<R>

    abstract infix fun <R> flatMap(f: (T) -> Maybe<R>): Maybe<R>

    abstract infix fun or(other: Maybe<T>): Maybe<T>

    companion object {

        fun <T> from(value: T): Maybe<T> = Just(value)

        fun <T> empty(): Maybe<T> = Nothing()

        fun <T> fromNullable(value: T?) =
            value?.let { from(it) } ?: empty()

    }


    override fun equals(other: Any?): Boolean =
        when {
            this is Nothing<*> && other is Nothing<*> -> true
            this is Just<*> && other is Just<*> -> this.value == other.value
            else -> false
        }

    override fun hashCode() =
        if (isPresent)
            value.hashCode()
        else super.hashCode()

}

class Just<T>(override val value: T) : Maybe<T>() {
    override val isPresent: Boolean = true

    override fun <R> map(f: (T) -> R): Maybe<R> =
        from(f(value))

    override fun <R> ap(f: Maybe<(T) -> R>): Maybe<R> =
        f.map { it(value) }

    override fun <R> flatMap(f: (T) -> Maybe<R>): Maybe<R> =
        f(value)

    override fun or(other: Maybe<T>): Maybe<T> = this

    override fun toString(): String = "Just[$value]"
}

class Nothing<T> : Maybe<T>() {
    override val value: T
        get() = throw UnsupportedOperationException("Nothing")

    override val isPresent: Boolean = false

    override fun <R> map(f: (T) -> R): Maybe<R> =
        empty()

    override fun <R> ap(f: Maybe<(T) -> R>): Maybe<R> =
        empty()

    override fun <R> flatMap(f: (T) -> Maybe<R>): Maybe<R> =
        empty()

    override fun or(other: Maybe<T>): Maybe<T> = other

    override fun toString(): String = javaClass.simpleName
}
package cn.berberman.fp.util.either

sealed class Either<L, out R> {

    abstract infix fun <T> map(f: (R) -> T): Either<L, T>

    abstract infix fun <T> ap(f: (Either<L, (R) -> T>)): Either<L, T>

    abstract infix fun <T> flatMap(f: (R) -> Either<L, T>): Either<L, T>

    companion object {
        fun <L, R> right(value: R): Either<L, R> = Right(value)

        fun <L, R> left(value: L): Either<L, R> = Left(value)
    }
}

class Left<L, R>(val value: L) : Either<L, R>() {

    override fun <T> map(f: (R) -> T): Either<L, T> = Left(value)

    override fun <T> ap(f: Either<L, (R) -> T>): Either<L, T> = Left(value)

    override fun <T> flatMap(f: (R) -> Either<L, T>): Either<L, T> = Left(value)

    override fun equals(other: Any?): Boolean =
        (other is Left<*, *> && value == other.value)

    override fun toString(): String = "Left[$value]"

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

}

class Right<L, R>(val value: R) : Either<L, R>() {

    override fun <T> map(f: (R) -> T): Either<L, T> = Right(f(value))

    override fun <T> ap(f: Either<L, (R) -> T>): Either<L, T> = f.map { it(value) }

    override fun <T> flatMap(f: (R) -> Either<L, T>): Either<L, T> = f(value)

    override fun equals(other: Any?): Boolean =
        (other is Right<*, *> && value == other.value)

    override fun toString(): String = "Right[$value]"

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

}


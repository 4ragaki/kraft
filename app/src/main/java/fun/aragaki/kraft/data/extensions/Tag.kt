package `fun`.aragaki.kraft.data.extensions

abstract class Tag {

    abstract fun tagGetName(): String?
    abstract fun tagGetId(): Long?
    abstract fun tagGetType(): Int?
}
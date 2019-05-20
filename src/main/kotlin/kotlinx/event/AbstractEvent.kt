package kotlinx.event

abstract class AbstractEvent<T> : Event<T> {

    @JvmSynthetic
    override operator fun plusAssign(handler: (T) -> Unit) {
        add(handler)
    }

    @JvmSynthetic
    override operator fun minusAssign(handler: (T) -> Unit) {
        remove(handler)
    }

    override fun invoke(data: T) = forEach { it(data) }

}

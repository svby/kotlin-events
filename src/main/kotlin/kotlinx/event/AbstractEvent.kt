package kotlinx.event

abstract class AbstractEvent<T> : Event<T> {

    @JvmSynthetic
    override operator fun plusAssign(handler: (T) -> Unit) {
        synchronized(this) {
            add(handler)
        }
    }

    @JvmSynthetic
    override operator fun minusAssign(handler: (T) -> Unit) {
        synchronized(this) {
            remove(handler)
        }
    }

    override fun invoke(data: T) = synchronized(this) { forEach { it(data) } }

}

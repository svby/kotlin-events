package kotlinx.event

interface Event<T> : MutableCollection<(T) -> Unit> {

    @JvmSynthetic
    operator fun plusAssign(handler: (T) -> Unit)

    @JvmSynthetic
    operator fun minusAssign(handler: (T) -> Unit)

    operator fun invoke(data: T)

}

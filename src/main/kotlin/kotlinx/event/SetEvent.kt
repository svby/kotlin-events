package kotlinx.event

open class SetEvent<T> private constructor(private val backing: MutableSet<(T) -> Unit>)
    : AbstractEvent<T>(), MutableCollection<(T) -> Unit> by backing {

    constructor() : this(HashSet())

}

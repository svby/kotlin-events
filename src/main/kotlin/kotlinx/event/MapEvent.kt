package kotlinx.event

import java.util.concurrent.atomic.AtomicInteger

class MapEvent<T>(private val backing: MutableMap<String, (T) -> Unit>)
    : AbstractEvent<T>(), MutableMap<String, (T) -> Unit> by backing {

    constructor() : this(HashMap())

    private val nextUnnamedIndex = AtomicInteger()

    override val size get() = backing.size
    override fun contains(element: (T) -> Unit) = backing.containsValue(element)
    override fun containsAll(elements: Collection<(T) -> Unit>) = backing.values.containsAll(elements)
    override fun isEmpty() = backing.isEmpty()
    override fun iterator() = backing.values.iterator()
    override fun remove(element: (T) -> Unit) = backing.values.remove(element)
    override fun removeAll(elements: Collection<(T) -> Unit>) = backing.values.removeAll(elements)
    override fun retainAll(elements: Collection<(T) -> Unit>) = backing.values.retainAll(elements)

    override fun add(element: (T) -> Unit): Boolean {
        backing[nextUnnamedIndex.getAndIncrement().toString()] = element
        return true
    }

    override fun addAll(elements: Collection<(T) -> Unit>): Boolean {
        elements.forEach { add(it) }
        return true
    }

    @JvmSynthetic
    operator fun set(name: String, handler: ((T) -> Unit)?) {
        if (handler == null) backing.remove(name)
        else backing[name] = handler
    }

    override fun clear() = backing.clear()

}

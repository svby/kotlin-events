package kotlinx

typealias EventHandler<EventType> = java.util.function.Consumer<EventType>

internal operator fun <T> java.util.function.Consumer<T>.invoke(t: T) = accept(t)

class Event<T> : Iterable<MutableMap.MutableEntry<String, EventHandler<T>>> {

    private val list = java.util.LinkedHashMap<String, EventHandler<T>>()

    var nextUnnamedIndex = 0L
        private set

    val size: Int @JvmName("size") get() = list.size
    val listeners: MutableCollection<MutableMap.MutableEntry<String, EventHandler<T>>> get() = list.entries

    fun clear() = list.clear()

    override operator fun iterator() = list.iterator()

    @JvmName("add")
    operator fun plusAssign(handler: EventHandler<T>) {
        list.put("${nextUnnamedIndex++}", handler)
    }

    @JvmName("put")
    operator fun set(name: String, handler: EventHandler<T>) {
        list.put(name, handler)
    }

    @JvmName("add")
    inline operator fun plusAssign(crossinline handler: (T) -> Unit) {
        this += java.util.function.Consumer { handler(it) }
    }

    @JvmName("put")
    inline operator fun set(name: String, crossinline handler: (T) -> Unit) {
        this[name] = java.util.function.Consumer { handler(it) }
    }

    @JvmName("remove")
    operator fun minusAssign(name: String) {
        list.remove(name)
    }

    @JvmName("handle")
    operator fun invoke(data: T) {
        for ((_, value) in this) value(data)
    }

}
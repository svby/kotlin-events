import kotlinx.event.event
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UnnamedEventTest {

    data class ServerEvent(val name: String, val joined: Boolean)

    @Test
    fun test() {
        val event = event<ServerEvent>()

        var count = 0

        val handler: (ServerEvent) -> Unit = { (name, joined) ->
            count += if (joined) 1 else -1
            println("$name ${if (joined) "joined" else "left"} (total: $count)")
        }

        event += handler

        assertEquals(1, event.size)

        event(ServerEvent("Alice", true))
        event(ServerEvent("Bob", true))
        event(ServerEvent("Charles", true))

        assertEquals(3, count)

        event(ServerEvent("Bob", false))

        assertEquals(2, count)

        event -= handler

        event(ServerEvent("Alice", false))
        event(ServerEvent("David", true))

        assertEquals(2, count)
    }

}

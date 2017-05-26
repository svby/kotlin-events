import kotlinx.event.Event
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class KotlinTest {

    data class ServerEvent(val joined: Boolean, val user: String)

    @Test
    fun testEvents() {
        val event = Event<ServerEvent>()
        var userCount = 0

        event["joinListener"] = { (joined, user) -> println("$user ${if (joined) "joined" else "left"}") }

        /*
        This also works:
        event += { (joined, user) -> println("$user ${if (joined) "joined" else "left"}") }
        */

        event["counter"] = { userCount += if (it.joined) 1 else -1 }

        assertEquals(event.size, 2)

        event(ServerEvent(true, "Alice"))
        event(ServerEvent(true, "Bob"))
        event(ServerEvent(true, "Charles"))

        assertEquals(userCount, 3)

        event(ServerEvent(false, "Bob"))

        assertEquals(userCount, 2)

        //Disable the counter
        event -= "counter"
        event(ServerEvent(false, "Alice"))

        assertEquals(userCount, 2)
    }

}
import kotlinx.event.Event;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;

public class JavaTest {

    public static class ServerData {
        public final boolean joined;
        public final String name;

        public ServerData(boolean joined, String name) {
            this.joined = joined;
            this.name = name;
        }
    }

    @Test
    public void testEvents() {
        final Event<ServerData> event = new Event<>();
        final AtomicInteger count = new AtomicInteger();

        event.put("joinListener", data -> {
            System.out.printf("%s has %s%n", data.name, data.joined ? "joined" : "left");
        });

        /*
        This also works:
        event.add(data -> {
            System.out.printf("%s has %s%n", data.name, data.joined ? "joined" : "left");
        });
        */

        event.put("counter", data -> {
            if (data.joined) count.incrementAndGet();
            else count.decrementAndGet();
        });

        assertEquals(event.size(), 2);

        event.handle(new ServerData(true, "Alice"));
        event.handle(new ServerData(true, "Bob"));
        event.handle(new ServerData(true, "Charles"));

        assertEquals(count.get(), 3);

        event.handle(new ServerData(false, "Bob"));

        assertEquals(count.get(), 2);

        //Disable the counter
        event.remove("counter");
        event.handle(new ServerData(false, "Alice"));

        assertEquals(count.get(), 2);
    }

}

# Kotlin events

`kotlinx.events` is a simple event library for Kotlin which is designed to be intuitive and similar in design to C#'s
event features.

A typical C# event handling system looks like this:

```cs
using System;

namespace EventTest {
    public struct ServerEvent { ... }

    public delegate void EventHandler(ServerEvent data);

    internal class Program {
        private static event EventHandler Event;

        private static void Main() {
            var count = 0;
            Event += data => Console.WriteLine($"{data.Name} {(data.Joined ? "joined" : "left")}");
            Event += data => count += data.Joined ? 1 : -1;
            
            Event(new ServerEvent(true, "Alice"));
            Event(new ServerEvent(true, "Bob"));
            Event(new ServerEvent(true, "Charles"));
            
            Console.WriteLine($"Users: {count}");
            
            Event(new ServerEvent(false, "Bob"));
            
            Console.WriteLine($"Users: {count}");
        }
    }
}
```

The code outputs
```
Alice joined
Bob joined
Charles joined
Users: 3
Bob left
Users: 2
```
which is what we expect.
Can we do better?

Of course we can, using our favorite language. Here's the Kotlin equivalent, using `events`:

```kotlin
data class ServerEvent(val name: String, val joined: Boolean)

val event = event<ServerEvent>()
var count = 0

fun main() {
    event += { (name, joined) ->
        count += if (joined) 1 else -1
        println("$name ${if (joined) "joined" else "left"} (total: $count)")
    }

    event(ServerEvent("Alice", true))
    event(ServerEvent("Bob", true))
    event(ServerEvent("Charles", true))

    event(ServerEvent("Bob", false))
}
```

The syntax is similar, but now you can use it in your JVM projects with little modification.

## Usage

The top-level functions `event` and `namedEvent` can be used to initialize standard set-backed events and
`Map<String, (T) -> Unit>`-backed events respectively.

The `MapEvent` type allows retrieving and adding events ("named events") by a string key.

The base interface `Event` is a collection type, and can be used accordingly.

## Maven

You can retrieve this artifact from [JitPack](https://jitpack.io/):

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

...
	
<dependency>
    <groupId>com.github.halcyxn</groupId>
    <artifactId>kotlin-events</artifactId>
    <version>v2.0</version>
</dependency>
```

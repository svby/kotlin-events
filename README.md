# Kotlin Events

`kotlinx.events` is a simple event library for Kotlin (and thereby Java and all other JVM languages) which is designed
to be intuitive and similar in design to C#'s event features.

A typical C# event handling system looks like this:

```cs
using System;
using System.Diagnostics;

namespace EventTest {
    public struct ServerEvent { ... }

    public delegate void EventHandler(ServerEvent data);

    internal class Program {
        private static event EventHandler Event;

        public static void Main(string[] args) {
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
data class ServerEvent(val joined: Boolean, val user: String)

fun main(args: Array<String>) {
    val event = Event<ServerEvent>()
    var userCount = 0

    event += { (joined, user) -> println("$user ${if (joined) "joined" else "left"}") }
    event += { userCount += if (it.joined) 1 else -1 }

    event(ServerEvent(true, "Alice"))
    event(ServerEvent(true, "Bob"))
    event(ServerEvent(true, "Charles"))

    println("Users: $userCount")

    event(ServerEvent(false, "Bob"))

    println("Users: $userCount")
}
```

The syntax is practically identical, but now you can use it in your JVM projects with little modification.

`events` also includes some extra features to make certain things easier!

## Features

You can easily create an event handler with the `Event` class - that's it.

```kotlin
val handler = Event<MyType>()
```

Now you can add event handlers to this class using the `+=` operator (JVM name: `add`):

```kotlin
handler += { data -> /* do something */ }
```

The event handlers can even be named with the `[]` operator (JVM name: `put`):

```kotlin
handler["myHandler"] = { data -> /* do something */ }
```

These named handlers can be removed via the `-=` operator (JVM name: `remove`),

```kotlin
handler -= "myHandler"
```

*Do not forget that `[]` does not accept `null` values - that is why `-=`/`remove` is used instead.*

Unnamed handlers are assigned an integral name equivalent to the amount of unnamed handlers added so far, starting from
zero.

To handle an event, use the `()` operator on the event (JVM name: `handle`):

```kotlin
handler(MyEvent(...))
```

`Event` also implements `Iterable<MutableEntry<String, EventHandler<T>>>`, so you can iterate all handlers.

### Additional

Methods have been renamed with `@JvmName` and accept `java.util.function.Consumer`s, so using `events`
from other JVM languages is definitely an option!

Java test code can be found in the `test` package.

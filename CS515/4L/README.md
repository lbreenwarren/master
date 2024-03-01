# Functional lists in Java

Common, simple (not higher-order) methods on functional (persistent) lists.  Sorting illustration.

## Design

  - Making `nil` a singleton is a bit messy, and may not be strictly necessary.
    It involves an unchecked typecast.
    On the plus side, it leads to an interesting discussion of equality (`equals` is *not* overridden in class `Nil`).
  - The `funlist` implementation makes a good exercise on pointers (especially methods `take` and `join`).
  - The `recfunlist` implementation makes a good exercise on recursion. 
   The code is arguably simpler than in `funlist`, but methods will blow the stack on large lists because Java has no tail-recursion optimization.
  - Methods `take` and `drop` follow the Scala semantics (taking or dropping more than the length of the list is allowed) rather that the SML semantics (exception).
  - Classes `funlist.Nil` and `recfunlist.Nil` are identical.
  - Interfaces `funlist.List` and `recfunlist.List` are identical.
  - Unit tests are identical for both implementations.
  - Method `join` should allow the elements of the second list to be of a supertype of the type of the first list (e.g., joining a list of books and a list of magazines would result in a list of publications).
    It can be done in Scala, but it doesn't seem to be possible in Java.
  - Class `recfunlist.Cons` could technically be a record in Java 17 (as suggested by IntelliJ), but there is no good reason for it.
  - The project has a dependency to [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/) for easier argument validation.

## Setup

Tests can be run from an IDE, or by using Gradle:

```
./gradlew test 
```

A [plugin](https://github.com/radarsh/gradle-test-logger-plugin) is used to display tests on the terminal, but this is not strictly necessary.
Test results are always available in `build/reports/tests/test/index.html`. 

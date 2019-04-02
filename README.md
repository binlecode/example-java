## Welcome to Basic Java Tutorial With Code Snippets


### Requirements

- JDK 11+


### Todos

- [x] build source code example index in README
- [ ] review equal and hashCode examples
- [x] add function reference example
- [ ] add more stream examples
- [ ] add interface default method example
- [ ] add RxJava exmaple

=== Reactive types to cover:
- [x] Observable: the heart of Rx, a class that emits a stream of data or events
- [ ] Single : a version of an Observable that emits a single item or fails
- [ ] Maybe: lazy emission pattern, can emit 1 or 0 items or an error signal
- [ ] RxJava2 core observables: Publisher and Flowable

=== Operators to cover:

- [ ] map: transforms the items by applying a function to each item
- [ ] flatMap: takes the emissions of one Observable and returns merged emissions in another Observable to take its place
- [ ] filter: emits only those items that pass a criteria (predicate test)
- [ ] skip/take: suppress or takes the first n items
- [ ] all: determines whether all items meet some criteria
- [ ] reduce: applies a function to each item sequentially, and emits the final value. For example, it can be used to sum up all emitted items
- [ ] toMap: converts an Observable into another object or data structure
- [ ] test: returns TestObserver with current Observable subscribed
- [ ] timeout: to handle timeouts, e.g. deliver some fallback data 


 

#### misc notes list
- [Core Java basics](./docs/core-java.md)
- [Java 8 basics](./docs/java-8-basics.md)
- [Java concurrency](./docs/java-concurrency.md)


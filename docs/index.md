## Welcome to Basic Java Tutorial With Code Snippets

[![Build Status](https://travis-ci.org/binlecode/example-java.svg?branch=master)](https://travis-ci.org/binlecode/example-java)

## Site and API documents

[Site document](./site/index.html)

[API document](./site/apidocs/index.html)

## misc notes list
- [Core Java basics](./docs/core-java.md)
- [Java 8 basics](./docs/java-8-basics.md)
- [Java concurrency](./docs/java-concurrency.md)

#### JDK

- JDK 11

for vscode specific workspace specific `settings.json`:
`"java.home": "/Users/binle/.sdkman/candidates/java/11.0.6.hs-adpt"`


#### Oracle jdbc [optional]

Due to Oracle license restriction, there are no public repositories that provide ojdbc jar.
You need to download it and install in your local repository. Get jar from Oracle and install it in your local maven repository using:
```bash
mvn install:install-file -Dfile={path/to/your/ojdbc.jar} \
    -DgroupId=com.oracle \
    -DartifactId=ojdbc6 \
    -Dversion=11.2.0 \
    -Dpackaging=jar
```

#### Javadoc

Both maven javadoc and site plugins are installed.

To generate site doc:
```
./mvnw site
```

To generate package API doc:
```
./mvnw javadoc:javadoc
```

Site documents are generated at folder path `target/site`.
API documents are generated at `target/site/apidocs`.




### Todos

- [x] build source code example index in README
- [ ] build sonar and travis-CI hook-up
- [ ] review equal and hashCode examples
- [x] add function reference example
- [x] add more stream examples
- [ ] add interface default method example
- [ ] add RxJava example

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


 



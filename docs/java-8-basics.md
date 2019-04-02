## Java 8 Basic Knowledge: 

    
Q: what’s the type of this Lambda: 
```java
() -> System.out.println(“hello”);
```
> should be Runnable functional interface, because it doesn’t return any result

Q: what is functional interface
> an interface that bridges an object to its (single) method, so that it can be called as a function object

Q: is it possible to define our own functional interfaces?
> yes, we can, and we should

Q: how to define a functional interface?
> 1. use @FunctionalInterface annotation to make an interface to be a functional interface
> 2. or: define an interface with ONE abstract method

Q: is @FunctionalInterface annotation mandatory?
> - No, any interface with ONE abstract method can be viewed as functional interface.
> - The annotation enforces compiler check and IDE code assistance

Q: what are rules of defining a functional interface
> - one and only one abstract method, no more
> - any number of default and static methods
    
Q: why functional interface demand ONE abstract method
> language design originally aimed at facilitating lambda function which can only provide one method

Q: Stream API typical use cases
> 1. lazy operations, such as group-by, order-by reactive execution with functional programming
> 2. collection parallelism
> 3. pipeline operations
> 4. better performance
        
Q: difference b/t Collection API and Stream API
> 1. collection api stores data, stream api processes data
> 2. collection api internal iteration, stream api external iteration, using forEach
> 3. collection api construct eagerly, stream api lazily
> 4. collection api data recurring availability, stream api data consume only once
> 5. collection api block code nature, stream api functional callback nature
    
Q: memory runtime difference b/t stream and loop API for collection
> 1. stream work on view, not changing the original data 
> 2. in stream case, the source collection element is accessed in lazy way, aka consumed from the last stage of the operation being called in 
> 3. in loop case, options are in the same block and executed in procedural way and all data in memory within the block
> 4. loop case forces all elements in sequence in memory, while in stream way collection can be parallel and each has its own memory data lifecycling


Q: what’s internal iteration
> api takes care of how to iterate collection, no explicit coding for iterating the collection
        
Q: what’s the benefit of internal iteration
> 1. focus only on what’s need to be done for each iteration
> 2. not forced in sequential only, enables parallelism

Q: difference b/t for and forEach
> 1. imperative vs functional programming model
> 2. for is for code block, forEach is for functional callback in each iteration

Q: why do we need interface having both static and non-static method 
> 1. static for Interface specific utilities, to save legacy way of ``` ClassXs.staticMethod() ``` for X class 
> 2. non-static: default methods

Q: diamond inheritance, what is it
> java diamond inheritance is via interfaces with non-abstract methods

Q: example of diamond inheritance dilemma
```java
public interface A{   
  default void display() { //code goes here }
}
public interface B extends A{ }
public interface C extends A{ }
public class D implements B,C{ }
```
- is there any error in above code?
> compile error: D doesn’t know which interface (B or C) to get method ```display()```
- how to resolve this dilemma?
> override method ```display()``` in class D to pick super interface explicitly
```java
    void display() { B.super.display(); }
```

Q: What are the most common two ways of defining a method reference
> 1. reference: ```Class::method()```
> 2. and in-line: lambda function

Q: Java 8 Date time API, why new Date time API
> 1. immutable, and thread safe
> 2. old Date time and DateFormat class objects are mutable, and not thread safe
> 3. highly performant


## Core Java Basic Knowledge

Q: java final keyword
> 1. final variable: can be assigned only once, ie, initialization only (can be later than definition though)
> 2. final method: not overridable
> 3. final class: not extendable

Q: java static keyword
> 1. static variable
> 2. static method
> 3. static block: 
>       - Java static block is the group of statements that only gets executed once when the class is loaded into memory by Java ClassLoader
>       - most commonly used to group initialization of static variables
> 4. static class
>       - only for static nested class

Q: can inner classes be static
> only non-static nested classes are inner classes, they can be named or anonymous (common in call back scenario)

Q: why we need inner classes
> hosting instance binding scope + freedom of interface adaptation
        
Q: explain try-with-resource
> 1. automatic resource management, saving ```finally { xx.close() }``` type of explicite resource closing
> 2. target resource must implement AutoClosable interface
> 3. can have multiple resources in one try-with-resource block, auto close in reverse order

Q: is it legal to have try without catch
> it is legal, and **sometimes it is needed** as try-finally to bubble exception while still ensure some resource housekeeping is done after exception is thrown

Q: is java reflection anti-pattern? what’s violated if yes
> 1. Yes, it voilates OO encapsulation principle
> 2. but it is needed for runtime adaptive and proxy frameworks like Spring, Hibernate, Jackson, etc.

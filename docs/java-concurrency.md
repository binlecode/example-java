## Java Concurrency Basic Knowledge

Q: what’s the difference b/t process and thread
> 1. process is self contained JVM runtime, thread is a single task within the process
> 2. process has main memory, thread has its own ‘local’ memory called thread-local memory

Q: thread lifecycle
> 1. new: after new Thread(…)
> 2. runnable: after call thread.start(). it will look for thread scheduler for control logic to run instantly, or stay alive in thread pool waiting
> 3. running: can enter either blocked or dead status, depends on time slicing, thread dependency, or thread control
> 4. blocked: thread is back to thread pool by thread scheduler
> 5. dead: execution is complete

Q: thread priority
> 1. specify the priority of a thread to inform (suggest) thread scheduler so that a thread with a higher priority is executed before lower priority thread, although this is not guaranteed. 
> 2. thread priority is of int type and range from 1 to 10 (highest priority).

Q: how to make sure ```main()``` is the last thread to finish in multi-threaded java program
> we should use ```thread.join()``` to make sure all threads created by the main thread is dead before finishing the main thread. 

Q: diff b/t notify and notifyAll
> Object methods, wait, notify and notifyAll for thread scheduler control logic

Q: why are these Object methods, not specific to Thread class
> 1. in java, every object has monitor, and there’s no additional monitor on thread class.
> 2. synchronization should be applicable to any java object, not only thread

Q: should ```wait()```, ```notify()``` and ```notifyAll()``` only be called in synchronized blocks or methods
> yes, it’s a **must** to make sure the monitor on the target lock object is not released before such method is called

Q: diff b/t synchronized and volatile
> 1. synchronized on method or code block, and thread has thread-local (cache) memory
> 2. volatile on variable, and threads don’t have local cache for such variable, always read from process main memory

Q: diff b/t synchronized method and synchronized block
> 1. synchronized method acquires a method on the whole object. This means no other thread can use any synchronized method in the whole object while the method is being run by one thread.
> 2. synchronized blocks acquires a lock in the object between parentheses after the synchronized keyword. Meaning no other thread can acquire a lock on the locked object until the synchronized block exits. for example: ```synchronized(targetObject) {… // code }``` 
the targetObject may not be the the class or instance this block belongs, so this other parts of the class or instance are not locked from other threads visit

Q: why thread sleep() and yield() are static methods
> because such operation is always executed on current running thread, such methods being static ensures system determines which thread is the current running thread

Q: diff b/t callable and runnable
> return and exp vs no return or exp of future handler

Q: what’s java thread dump
> Thread dump is list of all the threads active in the JVM, thread dumps are very helpful in analyzing bottlenecks in the application and analyzing deadlock situations. There are many ways using which we can generate Thread dump – Using Profiler, Kill -3 command, jstack tool etc. I prefer jstack tool to generate thread dump of a program because it’s easy to use and comes with JDK installation. Since it’s a terminal based tool, we can create script to generate thread dump at regular intervals to analyze it later on. Read this post to know more about generating thread dump in java.

Q: what’s java executor framework
> ```java.util.concurrent.Executor``` interface, for standardizing invocation, scheduling, execution, and control of asynchronous tasks according to a set of execution policies. Executors framework facilitate process of creating Thread pools in java to optimize resource consumption for concurrency.



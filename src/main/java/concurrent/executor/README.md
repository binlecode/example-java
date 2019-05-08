## Notes on thread based concurrency implementation vs Executors

#### Quote from Venkat's book "Programming Concurrency on the JVM" chapter 2:

The old threading API has several deficiencies. We’d use and throw away the instances of the Thread class since they don’t allow restart. To handle multiple tasks, we typically create multiple threads instead of reusing them. If we decide to schedule multiple tasks on a thread, we had to write quite a bit of extra code to manage that. Either way was not efficient and scalable.

Methods like `wait()` and `notify()` require synchronization and are quite hard to get right when used to communicate between threads. The `join()` method leads us to be concerned about the death of a thread rather than a task being accomplished.

In addition, the `synchronized` keyword lacks granularity. It does not give us a way to time out if we do not gain the lock. It also doesn’t allow concurrent multiple readers. Furthermore, it is **very** difficult to unit test for thread safety if we use synchronized.

The newer generation of concurrency APIs in the `java.util.concurrent` package, spearheaded by Doug Lea, among others, has nicely replaced the old threading API:
- Wherever we use the Thread class and its methods, we can now rely upon the `ExecutorService` class and related classes.
- If we need better control over acquiring locks, we can rely upon the `Lock` interface and its methods.
- Wherever we use `wait/notify`, we can now use synchronizers such as `CyclicBarrier` and `CountdownLatch`.


#### Modern java concurrent API

The `Executors` class serves as a factory to create different types of thread pools that we can manage using the `ExecutorService` interface:
- single-threaded pool that runs all scheduled tasks in a single thread, one after another (sequentially)
- fixed threaded pool allows us to configure the pool size and concurrently runs, in one of the available threads, the tasks we throw at it. If there are more tasks than threads, the tasks are queued for execution, and each queued task is run as soon as a thread is available.
- A cached threaded pool will create threads as needed and will reuse existing threads if possible. If no activity is scheduled on a thread for well over a minute, it will start shutting down the inactive threads.




## Notes on thread based concurrency implementation vs Executors

#### Quote from Venkat's book "Programming Concurrency on the JVM" chapter 2:

The old threading API has several deficiencies. We’d use and throw away the instances of the Thread class since they don’t allow restart. To handle multiple tasks, we typically create multiple threads instead of reusing them. If we decide to schedule multiple tasks on a thread, we had to write quite a bit of extra code to manage that. Either way was not efficient and scalable.

Methods like `wait()` and `notify()` require synchronization and are quite hard to get right when used to communicate between threads. The `join()` method leads us to be concerned about the death of a thread rather than a task being accomplished.

In addition, the `synchronized` keyword lacks granularity. It does not give us a way to time out if we do not gain the lock. It also doesn’t allow concurrent multiple readers. Furthermore, it is **very** difficult to unit test for thread safety if we use synchronized.

The newer generation of concurrency APIs in the `java.util.concurrent` package, spearheaded by Doug Lea, among others, has nicely replaced the old threading API.
- Wherever we use the Thread class and its methods, we can now rely upon the `ExecutorService` class and related classes.
- If we need better control over acquiring locks, we can rely upon the `Lock` interface and its methods.
- Wherever we use `wait/notify`, we can now use synchronizers such as `CyclicBarrier` and `CountdownLatch`.



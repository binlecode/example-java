package rxjava;


/**
 * Backpressure and Flowable
 * <p>
 * 数据流发射，处理，响应可能在各自的线程中独立进行，上游在发射数据的时候，不知道下游是否处理完，也不会等下游处理完之后再发射。
 * 这样，如果上游发射的很快而下游处理的很慢，会怎样呢？
 * 将会产生很多下游没来得及处理的数据，这些数据既不会丢失，也不会被垃圾回收机制回收，而是存放在一个异步缓存池中，如果缓存池中的数据一直得不到处理，越积越多，最后就会造成内存溢出，这便是Rxjava中的背压问题。
 * ---------------------
 * 原文：https://blog.csdn.net/waterseason/article/details/84912973
 */
public class TestBackpressure {
}

package stream;


import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Parallel streams internally use the default ForkJoinPool, which by default has as many threads as
 * you have processors, as returned by Runtime.getRuntime().available-Processors(). You can change the
 * size of this pool using the system property java.util.concurrent.ForkJoinPool.common.parallelism.
 * This is a global setting, so it will affect all the parallel streams in your code.
 * In most cases, modifying this value is not recommended.
 */
public class TestParallelStream {

    public static void main(String[] args) {
        long ts = System.nanoTime();
        IntStream.rangeClosed(0, 100000)
//                .parallel()
                .map(n -> n + 1)
                .filter(n -> n % 3 == 0)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("sequential stream run elapse time: " + (System.nanoTime() - ts) + " ns");
    }



}

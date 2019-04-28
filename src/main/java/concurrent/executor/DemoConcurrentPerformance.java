package concurrent.executor;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * This example shows for a computational intensive task the currency vs performance trend.
 */
public class DemoConcurrentPerformance {

    public static void main(String[] args) {

        testSequentialVsParallel();

        testCustomParallelismPerformance();
    }

    /**
     * from a 2017 macbook pro with 8 virtual cores:
     * 262141664 ns without parallelism
     * 87141196 ns with parallelism
     * <p>
     * Note this example, the parallel stream uses system default thread pool, which is
     * {@link ForkJoinPool#commonPool}. This pool is shared by all parallel stream processing.
     * This is viewed by many as one of the biggest limitations of Stream API.
     * (@see https://www.baeldung.com/java-8-parallel-streams-custom-threadpool)
     */
    static void testSequentialVsParallel() {
        long ts = System.nanoTime();

        int upperB = 1000000;

        long pCnt = IntStream.rangeClosed(0, upperB)
                .filter(DemoConcurrentPerformance::isPrime)
                .count();
        long tLapse = System.nanoTime() - ts;
        System.out.printf("total count of primes under %d is %d \n", upperB, pCnt);
        System.out.printf("using sequential stream, lapse time: %s ns\n", tLapse);

        System.out.println("system cpu cores: " + Runtime.getRuntime().availableProcessors());

        ts = System.nanoTime();
        pCnt = IntStream.rangeClosed(0, upperB).parallel()  // default parallelism is number of cpu cores
                .filter(DemoConcurrentPerformance::isPrime)
                .count();
        tLapse = System.nanoTime() - ts;
        System.out.printf("total count of primes under %d is %d \n", upperB, pCnt);
        System.out.printf("using parallel stream, lapse time: %s ns\n", tLapse);
    }

    /**
     * Run a series of parallel processing with custom parallelism ranging from 1 to 20.
     * We can see the performance improves rapidly then saturates with increasing pool size.
     * <p>
     * The difference between the optimal pool size and the number of system cpu cores depends on
     * <code>blocking coefficient</code> of the target process. In other words, how CPU intensive
     * or IO intensive the target process is.
     * <p>
     * In this example, the target process is detecting primes and count them with an upper bound,
     * which is CPU intensive, in this case the <code>blocking coefficient</code> should be low.
     * This means little time is spent on IO (blocking) waiting, therefore the optimal pool size won't
     * be too much different from system CPU core numbers. This is proven by the resulting performance
     * curve that saturates around pool size around 8 and 9 (system cpu core number is 8).
     * <p>
     * <code>blocking coefficient</code> is important factor in estimating optimal parallelism.
     */
    static void testCustomParallelismPerformance() {
        System.out.println("system cpu cores: " + Runtime.getRuntime().availableProcessors());

        int upperB = 1000000;
        System.out.println("counting number of primes under " + upperB);

        for (int i = 1; i <= 20; i ++) {
            long tLapse = runParallelismWithCustomForkJoinPool(i, upperB);
            System.out.printf("parallelism: %2d => lapse time: %10d ns \n", i, tLapse);
        }

        // the following is the pool size vs lapse time running result:
        // system cpu cores: 8
        // counting number of primes under 1000000
        // parallelism:  1 => lapse time:  248838313 ns
        // parallelism:  2 => lapse time:  126267393 ns
        // parallelism:  3 => lapse time:   91725570 ns
        // parallelism:  4 => lapse time:   82940054 ns
        // parallelism:  5 => lapse time:   65288920 ns
        // parallelism:  6 => lapse time:   67760517 ns
        // parallelism:  7 => lapse time:   59839125 ns
        // parallelism:  8 => lapse time:   54471149 ns
        // parallelism:  9 => lapse time:   51678858 ns
        // parallelism: 10 => lapse time:   49888272 ns
        // parallelism: 11 => lapse time:   49602951 ns
        // parallelism: 12 => lapse time:   55252755 ns
        // parallelism: 13 => lapse time:   46760816 ns
        // parallelism: 14 => lapse time:   55868222 ns
        // parallelism: 15 => lapse time:   55979251 ns
        // parallelism: 16 => lapse time:   54564100 ns
        // parallelism: 17 => lapse time:   49284931 ns
        // parallelism: 18 => lapse time:   47158936 ns
        // parallelism: 19 => lapse time:   49339585 ns
        // parallelism: 20 => lapse time:   48558785 ns
    }


    // customize thread pool
    static long runParallelismWithCustomForkJoinPool(int parallelism, int upperBound) {
        assert parallelism > 0 && parallelism < 99;
        assert upperBound > 0;
        try {
            ForkJoinPool fjp = new ForkJoinPool(parallelism);
            long ts = System.nanoTime();
            long count = fjp.submit(() -> IntStream.rangeClosed(0, upperBound).parallel()  // default parallelism is number of cpu cores
                        .filter(DemoConcurrentPerformance::isPrime)
                        .count()).get();
            long tLapse = System.nanoTime() - ts;
//            System.out.printf("number of primes under %d is %d \n", upperBound, count);
//            System.out.println("lapse time: " + tLapse);
            return tLapse;

        } catch (ExecutionException | InterruptedException ex) {
            return -1;
        }
    }


    // this is pure function, can be a lambda or a functional object
    static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

}

package stream;

import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * The collect method on a stream triggers a reduction operation (parameterized by a Collector) on
 * the elements of the stream.
 */
public class TestStreamCollectReduce {

    public static void main(String[] args) {
        streamMaxMinSum();
        collectorReducingAndStreamReduce();
    }

    public static void streamMaxMinSum() {
        System.out.println("max of int stream: " +
                IntStream.generate(() -> (int)(Math.random() * 100))
                        .distinct()
                        .limit(10)
                        .boxed()
                        .collect(Collectors.maxBy(Integer::compareTo)) // or .minBy(..)
                        .orElse(0)
        );

        System.out.println("sum of double stream: " +
                Stream.generate(Math::random)
                        .limit(10)
                        .collect(Collectors.summingDouble(Double::doubleValue))
        );

        // the Collectors.summingDouble(...) can be simplified by .sum() method
        System.out.println("sum of int stream: " +
                IntStream.generate(() -> (int)(Math.random() * 100))
                        .distinct()
                        .limit(10)
                        .sum()
        );

        System.out.println("average of double stream: " +
                DoubleStream.generate(Math::random)
                .limit(10)
                .average()
                .orElse(0)
        );

        // all above can be combined into one convenient summarizing method that provides
        // count, sum, max, min, and average in SummaryStatistics object
        System.out.println("summary of double stream: " +
                Stream.generate(Math::random)
                .limit(10)
                .collect(Collectors.summarizingDouble(Double::doubleValue))
        );
    }

    /**
     * The Collectors.reducing factory method is a generalization of stream reducing support.
     * In most cases, the same logic can be implemented via Stream.reduce functional API.
     */
    public static void collectorReducingAndStreamReduce() {
        // the Collectors.summing method is essentially doing:
        System.out.println("summing with Collectors.reducing: " +
                DoubleStream.generate(Math::random)
                        .limit(10)
                        .boxed()
        //                .collect(Collectors.reducing(Double::sum))
                        .collect(Collectors.reducing((a, b) -> a + b))
                        .orElse(0d)
        );

        // this summing logic can be rewritten using Stream.reduce() method
        System.out.println("summing with Stream.reduce: " +
                Stream.generate(() -> (int)(Math.random() * 100))
                        .limit(10)
                        .reduce((a, b) -> a + b)
                        .orElse(0)
        );

        // the result Optional object can be replaced by providing a seed variable for the reduce function
        System.out.println("summing with Stream.reduce with seed: " +
                Stream.generate(() -> (int)(Math.random() * 100))
                        .limit(10)
                        .reduce(0, (a, b) -> a + b)
        );

        // now doing comparison reducing for max / min value
        System.out.println("max with Collectors.reducing: " +
                DoubleStream.generate(Math::random)
                        .limit(10)
                        .boxed()
                        .collect(Collectors.reducing((a, b) -> a > b ? a : b))
                        .orElse(null)
        );

        // or using Stream.reduce() with BinaryOperator functional interface
        System.out.println("min with Stream.reduce: " +
                DoubleStream.generate(Math::random)
                        .limit(10)
                        .boxed()
                        .reduce((a, b) -> a < b ? a : b)
                        .orElse(null)
        );
    }

}

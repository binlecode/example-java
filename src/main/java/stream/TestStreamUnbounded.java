package stream;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestStreamUnbounded {

    public static void main(String[] args) {

//        streamFromIterate();
//        streamFromGenerate();
        distinctTrap();
    }


    /**
     * Stream.iterate() is a common way of building source unbounded stream of element by
     * defining the element with seed value and iterating logic (unary operator functional
     * interface).
     */
    public static void streamFromIterate() {

        System.out.println(
                Stream.iterate(1, n -> n *2 )
                        .limit(10)
                        .collect(Collectors.toList())
        );

        // a popular example of .iterator is to generate a fibonacci sequence
        // need a two-element tuple in the iteration lambda to carry the state by input-output relation
        List<Integer> fiboList = Stream.iterate(new int[]{0, 1}, tpl -> new int[]{tpl[1], tpl[0] + tpl[1]})
                .filter(tpl -> {
                    System.out.println("fibo lamba generator array: [" + tpl[0] + ", " + tpl[1] + "]");
                    return true;
                })
                .map(tpl -> tpl[0])
                .takeWhile(n -> n <= 1000)   // cap the sequence at a max value
                .collect(Collectors.toList());
        System.out.println(fiboList);

        // the lambda can be replaced by an in-line function implementation
        // the key difference is that this is a function object instance thus it can contain internal state
        // across the iterations
        List<Integer> fiboList2 = Stream.iterate(0, new UnaryOperator<>() {
            private Integer pre = 1;   // internal state to record previous value
            private Integer ppre = 0;  // internal state to record pre-previous value
            @Override
            public Integer apply(Integer n) {
                System.out.println("n = " + n + ", pre = " + pre + ", ppre = " + ppre);
                n = pre + ppre;
                ppre = pre;
                pre = n;
                return n;
            }
        }).takeWhile(n -> n < 1000).collect(Collectors.toList());
        System.out.println(fiboList2);
    }

    /**
     * Stream.generate lets you produce an infinite stream of values computed on demand.
     * But generate doesn’t apply successively a function on each new produced value.
     */
    public static void streamFromGenerate() {


        // generate sequence in state-less mode using lambda or method reference
        System.out.println(
                Stream.generate(Math::random)
                        .limit(5)
                        .collect(Collectors.toList())
        );

        // generate sequence leveraging some insternal state, use inline function implementation
        System.out.println(
                Stream.generate(new Supplier<Integer>() {
                    private int seed = 0;
                    @Override
                    public Integer get() {
                        return seed++;
                    }
                })
                        .takeWhile(n -> n < 5)
                        .collect(Collectors.toList())
        );

        // internal state between iterations of sequence generation sometimes is a must, like fibonacci
        System.out.println("fibonacci below 1000: " +
                IntStream.generate(new IntSupplier() {
                    // here two internal states for previous two fibo numbers
                    int ppre = 0;
                    int pre = 1;

                    @Override
                    public int getAsInt() {
                        int fibo = ppre;  // initial ppre value (0) represents the first fibonacci number
                        int n = ppre + pre;  // calculate new fibo from previous two fibo
                        ppre = pre;
                        pre = n;

                        return fibo;
                    }
                }).takeWhile(n -> Integer.valueOf(n) < 1000)
                        .boxed()
                        .collect(Collectors.toList())
        );
    }

    /**
     * When have a unbounded stream, ordering b/t distinct and limit (or takeWhile) is critical.
     */
    public static void distinctTrap() {
        int upperBound = 5;
        // this code will run endlessly if upperBound is less than 5
        // since distinct is before limit, the distince values will never be more than upperbound value
        // therefore, the stream will run forever to fill 5 element limit
        // once upperBound is 6 or larger, the range generator will soon fill 0, 1, 2, 3, 4 values to meet
        // the 5-element limit
        System.out.println("5 distinct numbers: ");
        Stream.generate(() -> (int)(Math.random() * upperBound))
                .distinct()
                .limit(5)
                .forEach(System.out::println);

        // now reverse the order putting limit before distinct, this limits only 5 random number generated
        System.out.println("distinct numbers from 5 random numbers: ");
        Stream.generate(() -> (int)(Math.random() * upperBound))
                .limit(5)
                .distinct()
                .forEach(System.out::println);
    }

}

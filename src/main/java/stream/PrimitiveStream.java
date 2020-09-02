package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Java 8 introduces three primitive specialized stream interfaces,
 * IntStream, DoubleStream, and LongStream, which respectively specialize the elements 
 * of a stream to be int, long, and double—and thereby avoid hidden boxing costs.
 *
 * This cost saving is nontrivial when stream is very long (or unlimited) and computation
 * is heavy.
 */
public class PrimitiveStream {

    public static void main(String[] args) {

        List<Integer> lst = Arrays.asList(1, 2, 3, 4, 5);

        int sum = lst.stream()
                .mapToInt(Integer::intValue)  // mapToInt, mapToDouble and mapToLong
                .sum();
        System.out.println("int sum: " + sum);

        // reverse way from primitive to boxed Integer stream
        IntStream iStr = IntStream.rangeClosed(0, 5);  // a convenient way to build a numerical range
        Stream<Integer> itgStr = iStr.boxed();
        Integer itgSum = itgStr.reduce(0, Integer::sum);  // Integer::sum is method ref for BiFunction interface
        System.out.println("Integer sum: " + itgSum);

    }


}
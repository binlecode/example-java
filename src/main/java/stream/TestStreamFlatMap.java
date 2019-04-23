package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStreamFlatMap {


    public static void main(String[] args) {

        simpleFlatMap();
        showUniqueStage();

    }

    /**
     * Show the functional interface of flatMap and its implicit stream merging nature.
     */
    public static void simpleFlatMap() {
        List<String> wordList = Arrays.asList("hello", "this", "is", "stream", "world");
        Integer maxLength = wordList.stream()
                .map(String::length)
                .max(Integer::compareTo)
                .get();
        System.out.println(maxLength);

        // now try to return unique characters from the string stream
        // the flatMap takes a lambda that splits each word to a character stream
        // the flagMap then merges those streams into single global stream for next stage process
        List<String> uniqueCharList = wordList.stream()
                .flatMap(word -> Stream.of(word.split("")))
                .distinct()
                .collect(Collectors.toList());
        System.out.println(uniqueCharList);
    }

    /**
     * Now run the same stream including unique stage processing but with additional logging.
     * You will find the output is like below:
     * <code>
     *     show element: h
     *     show unique element: h
     *     show element: e
     *     show unique element: e
     *     show element: l
     *     show unique element: l
     *     show element: l
     *     show element: o
     *     show unique element: o
     *     show element: t
     *     show unique element: t
     *     show element: h
     *     show element: i
     *     show unique element: i
     *     show element: s
     *     show unique element: s
     *     show element: i
     *     show element: s
     *     show element: s
     *     show element: t
     *     show element: r
     *     show unique element: r
     *     show element: e
     *     show element: a
     *     show unique element: a
     *     show element: m
     *     show unique element: m
     *     show element: w
     *     show unique element: w
     *     show element: o
     *     show element: r
     *     show element: l
     *     show element: d
     *     show unique element: d
     * </code>
     * This reveals the internal stream processing on distinct check which 'blocks' duplicate element from
     * 'flowing' down the stream to next stage.
     */
    public static void showUniqueStage() {

        List<String> wordList = Arrays.asList("hello", "this", "is", "stream", "world");

        // now try to return unique characters from the string stream
        long uniqueCharCount = wordList.stream()
                .map(word -> word.split(""))    // split each word to a char (single char String) array
                .flatMap(charArray -> Stream.of(charArray))  // flattens each stream into a single stream
                .filter(c -> {  // does nothing but logging intermediate variable
                    System.out.println("show element: " + c);
                    return true;
                })
                .distinct()
                .filter(c -> {  // does nothing but logging intermediate variable
                    System.out.println("show unique element: " + c);
                    return true;
                })
                .count();
        System.out.println(uniqueCharCount);

        // this can be rewritten as method reference as
        long ts = System.nanoTime();
        System.out.println(
                wordList.stream()
                        .map(word -> word.split(("")))
                        .flatMap(Arrays::stream)
//                        .distinct()
                        .count()
        );
        System.out.println("sequential stream run elapse time: " + (System.nanoTime() - ts) + " ns");

        // when apply parallelStream on this stream, it actually takes more time then sequential version
        // mainly due to flatMap nest streams merging with parallel thread overhead.
        ts = System.nanoTime();
        System.out.println(
                wordList.parallelStream()
                        .map(word -> word.split(("")))
                        .flatMap(Arrays::stream)
//                        .distinct()
                        .count()
        );
        System.out.println("parallel stream run elapse time: " + (System.nanoTime() - ts) + " ns");

    }


}

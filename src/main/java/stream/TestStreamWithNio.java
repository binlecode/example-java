package stream;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;

/**
 * NIO File and Path examples with Stream API
 */
public class TestStreamWithNio {

    public static void main(String[] args) throws IOException, URISyntaxException {

        // use nio.Files to get a file from resource path
        Path path = Paths.get(TestStream.class.getClassLoader()
                .getResource("test_csv_2_html.csv").toURI());

        List<String> linesCopy = new ArrayList<>();

        Files.lines(path)
                .filter(line -> line.contains("ok"))
                .peek(line -> {   // peek doesn't consume the element from the stream
                    System.out.println("filtered original line = " + line);
                    linesCopy.add(line);
                })  // peek is like branching/logging
                .map(line -> line.split(",")[3])  // get the number from last column
                .forEach(line -> System.out.println("filtered value = " + line));

        linesCopy.forEach(System.out::println);

        Files.lines(path)
                // Stream.sorted returns a new sorted stream
                .sorted(Comparator.comparing(s -> -1 * Double.parseDouble(s.split(",")[3])))  // reverse value sort
                .forEach(line -> System.out.println("sorted line = " + line));

        // use Double::sum as reducer function to get overall sum
        Double sum = Files.lines(path)
                .map(line -> Double.parseDouble(line.split(",")[3]))
                .reduce(0d, Double::sum); // if stream is empty, then return 0d
        System.out.println("sum = " + sum);

        // use map collector to transform list to map with dup key resolving
        Map r = Files.lines(path)
                .map(line -> line.split(","))
                .collect(Collectors.toMap(
                        elm -> elm[0],
                        elm -> elm[3],
                        (x, y) -> {
                            System.out.println("dup keys found, retaining existing one");
                            return x;
                        },
                        TreeMap::new // customize map supplier
                ));
        System.out.println("r = " + r);

        // use groupBy with a Function (by lambda) to collect a map by lambda customized key

        // use the 4-th column for the grouping key
        Function<String, String> getGroupingKey = str -> str.split(",")[3];

        Map<String, List<String>> rm = Files.lines(path)
                .collect(Collectors.groupingBy(getGroupingKey));
        rm.keySet().forEach(k -> System.out.println("k = " + k + ", v = " + rm.get(k)));

        Map<String, Long> rc = Files.lines(path)
                .collect(Collectors.groupingBy(getGroupingKey, counting()));
        rc.keySet().forEach(k -> System.out.println("k = " + k + ", count = " + rc.get(k)));
    }

}

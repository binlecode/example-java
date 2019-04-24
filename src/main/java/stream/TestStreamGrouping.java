package stream;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStreamGrouping {

    public static void main(String[] args) {

//        simpleCollectorGrouping();

//        multiLevelGrouping();
        partitioning();
    }

    public static void simpleCollectorGrouping() {
        System.out.println(
                Stream.generate(() -> (int)(Math.random() * 100))
                        .distinct()
                        .limit(20)
                        .peek(System.out::println)  // just for logging internal element state
                        .collect(Collectors.groupingBy(n -> n % 2))  // group by odd/even
        );

        // group by custom categorization
        Stream.generate(() -> (int)(Math.random() * 100))
                .distinct()
                .limit(20)
                .collect(Collectors.groupingBy(n -> {
                            if (n < 20) return "low";
                            else if (n > 100) return "crazy"; // wont find one
                            else if (n > 90) return "very-high";  // case check ordering matters
                            else if (n > 80) return "high";
                            else return "medium";
                        },
                        Collectors.counting()  // apply counting, which turns groupBy to countBy
                ))
                .forEach((k, v) -> System.out.println(k + " => count: " + v)); // print consuming map k v pair
    }

    public static void multiLevelGrouping() {
        Stream.generate(() -> (int)(Math.random() * 100))
                .limit(100)
                .distinct()
                .collect(Collectors.groupingBy(   // 1st level
                        n -> n % 2,
                        Collectors.groupingBy(n -> {  // 2nd level
                                    if (n < 20) return "low";
                                    else if (n > 90) return "very-high";
                                    else if (n > 80) return "high";
                                    else return "medium";
                            }, Collectors.groupingBy(n -> {  // 3rd level, can nest on and on
                                    if (n.toString().endsWith("3")) return "3-ending";
                                    else return "others";
                            }, Collectors.counting()) // transform final sub list to count
                        )
                ))
                .forEach((k, v) -> {
                    System.out.println(k % 2 == 0 ? "even:" : "odd:");
                    v.forEach((k2, v2) -> {
                        System.out.println("\t" + k2);
                        v2.forEach((k3, v3) -> {
                            System.out.println("\t\t" + k3 + ": " + v3);
                        });
                    });
                } );
    }


    /**
     * Partitioning is a special case of grouping: having a predicate called a partitioning function as
     * a classification function.
     * The fact that the partitioning function returns a boolean means the resulting grouping Map will
     * have a Boolean as a key type, and therefore, there can be at most two different groups—one for
     * true and one for false.
     */
    public static void partitioning() {
        System.out.println("Is number even:");
        Stream.generate(() -> (int)(Math.random() * 100))
                .distinct()
                .limit(10) // get 10 distinct integers
                .collect(Collectors.partitioningBy(
                        n -> n % 2 == 0,
                        Collectors.collectingAndThen(  // takes two functions
                                Collectors.summingInt(Integer::intValue),  // get sum for sub group values
                                Integer::intValue)   // simply collect the result
                ))
                .forEach((k, v) -> System.out.println(k + " : " + v));
    }

}

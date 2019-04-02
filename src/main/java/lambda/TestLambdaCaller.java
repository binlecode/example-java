package lambda;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestLambdaCaller {

    /**
     * Call Supplier functional interface for lambda with no input arguments.
     */
    static void callSupplier(String name, Supplier<String> greetBuilder) {
        System.out.println("Hello! " + name + ", " + greetBuilder.get());
    }

    /**
     * Call Consumer functional interface for lambda with void return
     */
    static void callConsumer(String name, Consumer<String> greeter) {
        greeter.accept(name);
    }

    static void callBiConsumer(String person, String hobby, BiConsumer<String, String> decisionMaker) {
        decisionMaker.accept(person, hobby);
    }

    /**
     * call functional interface with input and output
     */
    static void callFunction(int seed, Function<Integer, Integer> calculator) {
        System.out.println("calculator: " + seed + " => " + calculator.apply(seed));
    }

    /**
     * call functional interface with two inputs and one output
     */
    static void callBiFunction(String person, Date time, BiFunction<String, Date, String> decisionMaker) {
        System.out.println("decision: " + decisionMaker.apply(person, time));
    }

    /**
     * call operator when input and output are of the same type
     */
    static void callOperator(Date date, UnaryOperator<Date> dateCalculator) {
        System.out.println("date calculator: " + date + " => " + dateCalculator.apply(date));
    }

    /**
     * call predicate as list filter
     */
    static void applyIntPredicate(IntPredicate intEvalutate) {
        System.out.println(Arrays.toString(IntStream.rangeClosed(1, 10).filter(intEvalutate).toArray()));
    }

    /**
     * call predicate with custom type
     */
    static void applyPredicate(Predicate<Date> evaluate) {
        List<Date> evaluatedDates = IntStream.rangeClosed(-3, 3)
                .mapToObj(i -> new Date().getTime() + i * 1000L * 24 * 3600)
                .map(l -> new Date(l))
                .filter(evaluate)
                .collect(Collectors.toList());

        System.out.println("evaluated dates are:");
        evaluatedDates.forEach(d -> {
            System.out.println(" " + d);
        });
    }

    public static void main(String[] args) {
        callSupplier("Test-Caller", () -> "Have a nice day!");
        // this is the same as:
        callSupplier("another caller", () -> {
            return "Have a great day!";
        });

        callConsumer("consumer", (String name) -> System.out.println("How are you " + name + " !"));

        System.out.println("when dicision is like:");
        callBiConsumer("Jenny", "Tennis", (p, h) -> System.out.println(p + " likes " + h));

        System.out.println("when dicision is hate:");
        callBiConsumer("Jenny", "Swimming", (p, h) -> System.out.println(p + " hates " + h));

        System.out.println("when calculator is power:");
        callFunction(11, seed -> seed * seed);

        System.out.println("when calculator is plus 100:");
        callFunction(11, seed -> seed + 100);

        System.out.println("when date calculator is plus 1 week:");
        callOperator(new Date(), date -> new Date(date.getTime() + TimeUnit.DAYS.toMillis(7)));

        System.out.println("when date calculator is minus 10 days:");
        callOperator(new Date(), date -> new Date(date.getTime() + TimeUnit.DAYS.toMillis(-10)));

        System.out.println("when decision maker is lazy by time:");
        callBiFunction("Mike", new Date(new Date().getTime() - 10000), (String p, Date t) -> {
            if (t.before(new Date())) {
                return p + " is not lazy because he finished job before " + t;
            } else {
                return p + "is lazy because he didn't finish job before " + t;
            }
        });

        System.out.println("when decision maker is mood by time:");
        callBiFunction("Mike", new Date(new Date().getTime() + 10000), (String p, Date t) -> {
            if (t.after(new Date())) {
                return p + " is hungry after " + t;
            } else {
                return p + " is not hungry before " + t;
            }
        });

        int threshold = 4;
        System.out.println("filter integers 1 to 10 with value > threshold " + threshold);
        applyIntPredicate(number -> {
            // lambda has binding scope of caller
            return threshold < number;
        });

        System.out.println("filter dates with evaluator of later than today:");
        applyPredicate(d -> {
            return d.after(new Date());
        });

    }

}

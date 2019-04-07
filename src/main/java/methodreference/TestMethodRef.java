package methodreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * There are three type of Java method references.
 * - static method reference at class level: Class::staticMethod
 * - instance method reference at class level: Class::instanceMethod
 * - instance method reference at instance level: instance::instanceMethod
 */
public class TestMethodRef {

    public static void main(String[] args) {

        List<Apple> inventory = Arrays.asList(
                new Apple(Color.GREEN, 80),
                new Apple(Color.RED, 120),
                new Apple(Color.GREEN, 160));

        List greenApples = filterByColorImperative(inventory, Color.GREEN);
        System.out.println("green apples: " + greenApples);

        List greenApples2 = filterByColor(inventory, Color.GREEN);
        System.out.println("green apples: " + greenApples2);

        List heavyApples = filterByWeight(inventory, 100);
        System.out.println("heavy apples: " + heavyApples);

        List redApples = filterByRedColor(inventory);
        System.out.println("red  apples: " + redApples);

        List greenApples3 = filterByGreenColor(inventory);
        System.out.println("greens  apples: " + greenApples3);

        List heavyApples2 = filterByWeight(inventory);
        System.out.println("heavy apples: " + heavyApples2);


    }

    /**
     * This is imperative (verbose) way of looping and filtering
     */
    public static List<Apple> filterByColorImperative(List<Apple> apples, Color color) {
        List<Apple> filteredApples = new ArrayList<>();
        for(Apple apple: apples) {
            if (apple.color == color) {
                filteredApples.add(apple);
            }
        }
        return filteredApples;
    }

    /**
     * Define a general functional interface to serve as {@link java.util.function.Predicate} in the apple list
     * converted stream.
     */
    public static List<Apple> filterBy(List<Apple> apples, Function<Apple, Boolean> f) {
        return apples.stream().filter(apple -> f.apply(apple))
                .collect(Collectors.toList());
    }

    /**
     * Implement the functional interface with inline function definition
     */
    public static List<Apple> filterByWeight(List<Apple> apples, int weight) {
        return filterBy(apples, new Function<Apple, Boolean>() {
            @Override
            public Boolean apply(Apple apple) {
                return apple.isHeavy(weight);
            }
        });
    }

    /**
     * Implement the functional interface with a lambda
     */
    public static List<Apple> filterByColor(List<Apple> apples, Color color) {
        return filterBy(apples, apple -> apple.isColor(color));
    }

    /**
     * Implement the functional reference with an instance method with class
     */
    public static List<Apple> filterByRedColor(List<Apple> apples) {
        return apples.stream().filter(Apple::isRed)
                .collect(Collectors.toList());
    }

    /**
     * Implement the functional interface with a static method with class
     */
    public static List<Apple> filterByGreenColor(List<Apple> apples) {
        return apples.stream().filter(AppleChecker::isBlue)
                .collect(Collectors.toList());
    }

    public static List<Apple> filterByWeight(List<Apple> apples) {
        return apples.stream().filter(new AppleChecker(110)::isHeavy)
                .collect(Collectors.toList());
    }

}

class Apple {
    Color color;
    int weight;

    Apple(Color color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    public boolean isRed() {
        return this.color == Color.RED;
    }

    public boolean isGreen() {
        return this.color == Color.GREEN;
    }

    public boolean isColor(Color color) {
        return this.color == color;
    }

    public boolean isHeavy(int weight) {
        return this.weight >= weight;
    }

    public int getWeight() {
        return weight;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return this.color + " apple: " + weight;
    }
}

/**
 * Helper class
 */
class AppleChecker {
    int weightThreshold = 100;

    public AppleChecker(int weightThreshold) {
        this.weightThreshold = weightThreshold;
    }

    public static boolean isBlue(Apple apple) {
        return apple.color == Color.GREEN;
    }

    public boolean isHeavy(Apple apple) {
        return apple.weight >= this.getWeightThreshold();
    }

    public int getWeightThreshold() {
        return weightThreshold;
    }
}

enum Color {
    GREEN,
    RED
}

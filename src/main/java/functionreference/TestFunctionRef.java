package functionreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestFunctionRef {


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

        List redApples = filterByRed(inventory);
        System.out.println("red  apples: " + redApples);
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
     * Use functional interface with inline implementation that returns boolean
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
     * The same functional interface implemented by a lambda
     */
    public static List<Apple> filterByColor(List<Apple> apples, Color color) {
        return filterBy(apples, apple -> apple.isColor(color));
    }

    /**
     * Now use instance method for function reference in the stream
     */
    public static List<Apple> filterByRed(List<Apple> apples) {
        return apples.stream().filter(Apple::isRed)
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

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return this.color + " apple: " + weight;
    }
}

enum Color {
    GREEN,
    RED
}

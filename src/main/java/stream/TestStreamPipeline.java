package stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * A collection is an in-memory data structure that holds all the values the data structure currently
 * has—every element in the collection has to be computed before it can be added to the collection.
 * On the contrary, a stream is a conceptually fixed data structure (you can’t add or remove elements
 * from it) whose elements are computed on demand.
 * <p>
 * The idea behind a stream pipeline is similar to the builder pattern
 * (see http://en.wikipedia.org/wiki/Builder_pattern).
 * In the builder pattern, there’s a chain of calls to set up a configuration (for streams this is a
 * chain of intermediate operations), followed by a call to a build method (for streams this is a
 * terminal operation).
 */
public class TestStreamPipeline {

    public static void main(String[] args) {

        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH) );

        simplePipeline(menu);
        simplePipelineWithLog(menu);
        simplePipelineTruncation(menu);
        findAndMatch(menu);
        simpleReduce(menu);
    }

    /**
     * show simple stream pipeline with common stage operations
     */
    public static void simplePipeline(List<Dish> menu) {
        Predicate<Dish> filterCal = (dish) -> dish.getCalories() > 300;

        List<String> threeHighCaloricDishNames = menu.stream()
//                .filter(dish -> dish.getCalories() > 300)
                .filter(filterCal)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList());

        System.out.println(threeHighCaloricDishNames);
    }

    /**
     * Now rewrite the same pipeline with additional logging to show 'loop fusion' optimization by JVM
     * From the log print you can see output like this:
     * <code>
     * filter: pork
     * map: pork
     * filter: beef
     * map: beef
     * filter: chicken
     * map: chicken
     * </code>
     * <p>
     * This shows that filter and map stages are merged (fused) in same pass (loop), an optimization by stream
     * internal iteration nature. The reason being filter and map implies the operation throughout the whole
     * stream, thus bundling them together in one traversing saves memory and cpu.
     */
    public static void simplePipelineWithLog(List<Dish> menu) {
        List<String> threeHighCaloricDishNames = menu.stream()
                .filter(dish -> {
                    System.out.println("filter: " + dish);
                    return dish.getCalories() > 300;
                })
                .map(dish -> {
                    System.out.println("map: " + dish);
                    return dish.getName();
                })
                .limit(3)
                .collect(Collectors.toList());

        System.out.println(threeHighCaloricDishNames);
    }

    /**
     * skip and limit are complementary
     */
    public static void simplePipelineTruncation(List<Dish> menu) {
        System.out.println(
                menu.stream()
                        .skip(2)
                        .limit(2)
                        .findAny()
                        .map(Dish::getName)
                        .get()
        );

        System.out.println(
                menu.stream()
                        .skip(12)  // no indexOutOfBound error, just return an empty stream
                        .limit(2)
                        .findAny()
                        .map(Dish::getName)
                        .orElse("no dish")
        );
    }

    /**
     * From anyMatch processing stage we can see log stops at 'french fry' the first vegetarian dish,
     * this means anyMatch processes in short-circuit way.
     * Besides anyMatch, allMatch and nonMatch have similar short-circuit processing logic.
     * Other short-circuit processors include findFirst and findAny.
     * <p>
     * When to use findFirst and findAny? The answer is parallelism:
     * Finding the first element is more constraining in parallel. If you don’t care about which element 
     * is returned, use findAny because it’s less constraining when using parallel streams.
     */
    public static void findAndMatch(List<Dish> menu) {
        System.out.println("the menu has any vegetarian dish?");
        System.out.println(
        menu.stream()
                .filter(dish -> {   // just to log internal state
                    System.out.println("anyMatch for vegetarian dish: " + dish);
                    return true;
                })
                .anyMatch(Dish::isVegetarian)
        );

        System.out.println(
                menu.stream()
                        .filter(Dish::isVegetarian)
                        .findAny()  // short-circuit applied
                        .get()
        );

        System.out.println(
                menu.stream()
                .filter(dish -> dish.getCalories() < 300)
                .findFirst()    // short-circuit applied
                .get()
        );
    }

    /**
     * Reducer is a special terminal operation that combine all the elements in the stream repeatedly 
     * to produce a single value
     */
    public static void simpleReduce(List<Dish> menu) {
        int totalCalorie = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, (a, b) -> a + b);
        System.out.println("total calorie: " + totalCalorie);
        // this is equivalent to below
        // note that if initial value 
        System.out.println("total cal: " + 
                menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum)
        );

        // getting the dish with max calorie
        Dish dishWithMaxCalorie = menu.stream()
                .reduce((a, b) -> a.getCalories() > b.getCalories() ? a : b)
                .get();
        System.out.println("dish with max calorie: " + 
                dishWithMaxCalorie + " (" + dishWithMaxCalorie.getCalories() + " cal)");
        
        // essentially equivalent with max() shortcut
        menu.stream()
                .max(Comparator.comparing(Dish::getCalories))
                .ifPresent(dish -> System.out.println("dish with max calorie: " + 
                        dishWithMaxCalorie + " (" + dishWithMaxCalorie.getCalories() + " cal)"));
    }

}

class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;
    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public boolean isVegetarian() {
        return vegetarian;
    }
    public int getCalories() {
        return calories;
    }
    public Type getType() {
        return type;
    }
    @Override
    public String toString() {
        return name;
    }
    public enum Type { MEAT, FISH, OTHER }
}

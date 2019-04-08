package methodreference;


import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Instance method is very useful in reference mode as 'helper' functions but holds some of the closure
 * feature that can access (not change) local variables in enclosing defining context.
 * <p/>
 * Such helper instance method can be private and read instance scope state.
 */
public class TestInstanceMethodRef {

    int marketValueThreshold;

    public TestInstanceMethodRef(int marketValueThreshold) {
        this.marketValueThreshold = marketValueThreshold;
    }

    public static void main(String[] args) {
        TestInstanceMethodRef tim = new TestInstanceMethodRef(1000);

        BiFunction<Make, Integer, Car> carBuilder = Car::new;



        List<Car> cars = Arrays.asList(
                new Car(Make.BMW, 2500),
                new Car(Make.HONDA, 850),
                new Car(Make.LEXUS, 1750),
                new Car(Make.TOYOTA, 900),
                carBuilder.apply(Make.NISSAN, 825),
                carBuilder.apply(Make.KIA, 750),
                carBuilder.apply(Make.JEEP, 1125)
        );

        List<Car> carsOnWholeSale = tim.filterSalesOrder(cars);
        carsOnWholeSale.forEach(System.out::println);


        // in the lambda, a and b's type (Car) is inferred
        Comparator<Car> carValueComparator = (a, b) -> a.marketValue - b.marketValue;
        cars.sort(carValueComparator);
        System.out.println(cars);

        // Comparator includes a static helper method called comparing that takes a Function
        // extracting a Comparable key and produces a Comparator object
        Comparator<Car> carValComp = Comparator.comparing((car) -> car.getMarketValue());
        cars.sort(carValComp.reversed());  // .reversed() return another comparator
        System.out.println(cars);

        // to make it more simplified and readable with instance method reference to replace the lambda
        cars.sort(Comparator.comparing(Car::getMarketValue));
        System.out.println(cars);

    }

//    public List<Car> sortCarsByValue(List<Car> cars) {
//
//        cars.sort();
//    }


    /**
     * Use instance method with its instance object as method reference working as a helper function
     */
    public List<Car> filterSalesOrder(List<Car> cars) {
        return cars.stream().filter(this::isOnWholeSale)
                .collect(Collectors.toList());
    }

    /**
     * Note that this instance method is private, but can be used in form of method reference.
     * Instance variable is used in this method.
     */
    private boolean isOnWholeSale(Car car) {
        return car.marketValue < this.marketValueThreshold;
    }

}


class Car {

    int marketValue;

    Make make;

    public Car(Make make, int marketValue) {
        this.make = make;
        this.marketValue = marketValue;
    }

    public int getMarketValue() {
        return marketValue;
    }

    @Override
    public String toString() {
        return "car with make: " + make + ", marketValue: " + marketValue;
    }

}

enum Make {
    BMW,
    LEXUS,
    TOYOTA,
    HONDA,
    NISSAN,
    MAZDA,
    KIA,
    JEEP
}

package lambda;


import java.util.function.Consumer;
import java.util.function.Function;


/**
 * A functional interface is an interface that declares exactly one abstract method.
 * Lambda expressions can be used only where a functional interface is expected.
 * Lambda expressions let you provide the implementation of the abstract method of
 * a functional interface directly inline and treat the whole expression as an instance
 * of a functional interface.
 */
public class TestLambdaForFunctionalInterface {

    /**
     * calling lambda based on functional interface
     */
    public static String greeting(String target, Greeter greeter) {
        return greeter.greet(target);
    }

    /**
     * calling lambda based on Function interface, which is equivalent to Greeter interface type
     */
    public static String greeting(String target, Function<String, String> funLambda) {
        return funLambda.apply(target);
    }

    /**
     * calls methods from an external class, accepting an Consumer functional interface
     */
    public static void extGreeting(String target, Consumer<String> func) {
        func.accept(target);
    }

    /**
     * defining a method as a named function, as far as its input-output signature matches function contract
     */
    public static String customGreeter(String target) {
        return "Custom greeter: " + target;
    }

    /**
     * define a custom caller that can call a function
     */
    public static void customGreeting(String target, Function<String, String> function) {
        System.out.println("custom greeting to " + function.apply(target));
    }


    /**
     * A lambda expression can be assigned to a variable or passed to a method expecting a functional interface
     * as argument, provided the lambda expression has the same signature as the abstract method of the functional
     * interface.
     * <p/>
     * Code below shows different cases of using lambda to match functional interfaces.
     */
    public static void main(String[] args) {
        System.out.println("main is running");

        // define an inline lambda definition implementing the Greeter functional interface
        String result = greeting("test-target",
                // casting inline lambda to interface to avoid method call ambiguity
                (Greeter) tgt -> {
                    System.out.println("Greetings! " + tgt);
                    return tgt;
                }
        );
        System.out.println(result);

        // define an explicitly named lambda
        Greeter greeterLambda = target -> {
            System.out.println("Greetings too! " + target);
            return target;
        };
        // and then call lambda by its name
        result = greeting("test-target-with-named-lambda", greeterLambda);
        System.out.println(result);

        // use java.util.function.Function to define lambda to skip functional interface definition
        // in our case the underlying function type is Function<T, R> as there's only one input parameter
        Function<String, String> greeterFunc = target -> {
            System.out.println("Functional Greeting! " + target);
            return target;
        };
        // then call it
        result = greeting("test-func-lambda", greeterFunc);
        System.out.println(result);

        // call a custom method as function by its reference
        customGreeting("method value reference as function", TestLambdaForFunctionalInterface::customGreeter);

        // call an external class (static) method
        extGreeting("test-ext-class-method-as-func", MethodProvider::staticGreeterMethod);

        // call an external class instance method
        extGreeting("test-ext-instance-method-as-func", new MethodProvider("myTag")::greeterMethod);
    }
}


class MethodProvider {

    String tag;

    public MethodProvider(String tag) {
        this.tag = tag;
    }

    static void staticGreeterMethod(String target) {
        System.out.println("static greeterMethod used as functional greeting: " + target);
    }

    void greeterMethod(String target) {
        System.out.println("instance greeterMethod (tag: " + tag + ") used as functional greeting: " + target);
    }
}


/**
 * In short, a functional interface is an interface that specifies exactly one abstract method.
 * <p/>
 * Functional interfaces, which are gathered in the java.util.function package, satisfy most
 * developers’ needs in providing target types for lambda expressions and method references.
 * Each of these interfaces is general and abstract, making them easy to adapt to almost any
 * lambda expression.
 * <p>
 * The @FunctionalInterface annotation is is used to indicate that the interface is intended to
 * be a functional interface and is therefore useful for documentation.
 * Using the @FunctionalInterface annotation, the compiler will trigger an error in response
 * to any attempt to break the predefined structure of a functional interface.
 */
@FunctionalInterface
interface Greeter {

    /**
     * The abstract method defined in the functional interface is called 'function descriptor'.
     */
    String greet(String target);

}

package lambda;

import java.util.Arrays;


/**
 * JVM has local variable final or effectively final restriction when referenced by lambda.
 * To ensure thread-safe local var state in lambda, Java implements access to a free local
 * variable as access to a copy of it, rather than access to the original variable.
 * This restriction also discourages typical imperative programming patterns that mutate an
 * outer variable.
 * <p>
 * This is an important difference between lambda (or anonymous inner class) and closure.
 * Scientifically, a closure is an instance of a function that can reference nonlocal variables
 * of that function with no restrictions.
 * By closure's definition it can access and modify variables defined outside its scope.
 * But Java has a restriction that lambda (and anonymous inner class) can’t modify the content of
 * local variables of a method in which the lambda is defined. Those variables have to be implicitly
 * final.
 * <p>
 * This restriction exists because local variables live on the stack and are implicitly confined to
 * the thread they’re in. Allowing capture of mutable local variables opens new thread-unsafe possibilities.
 */
public class TestLambdaLocalVar {

    public static void main(String[] args) {

        int seed = 10;
        // if the line below is not commented, compilation will raise error: "local var ref
        // from lambda must be final or effectively final"
        // If a variable is assigned only once, it can be viewed as effectively final.
        //seed += 10;

        // 'seed' is referenced in the lambda below as a local variable of 'main' method
        Arrays.asList(1, 2, 3, 4, 5).forEach(n -> System.out.println(n + seed));

        // if the line below is not commented, compilation will raise error: "local var ref
        // from lambda must be final or effectively final"
        //seed += 10;

    }



}

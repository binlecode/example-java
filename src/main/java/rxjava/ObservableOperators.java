package rxjava;


import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;


/**
 * An operator is a function that takes one Observable (the source) as its first argument and returns
 * another Observable (the destination).
 * Then for every item that the source observable emits, it will apply a function to that item, and then
 * emit the result on the destination Observable.
 *
 * Operators can be chained together to create complex data flows that filter event based on certain criteria.
 * Multiple operators can be applied to the same observable.
 *
 * It is not difficult to get into a situation in which an Observable is emitting items faster than an operator
 * or observer can consume them. This is a problem of backpressure.
 */



public class ObservableOperators {


    public static void main(String[] args) {

        String[] words = "hello this is an rx array".split(" ");

        // map operator

        Observable<String> hey2 = Observable.fromArray(words);
        // map operator transforms items emitted by an Observable by applying a function to each item
        hey2.map(elm -> elm.toUpperCase())
            .subscribe(  // three methods on the Observer interface
                elm -> System.out.println(elm),  // onNext callback, mandatory
                Throwable::printStackTrace,      // onError callback, optional
                () -> System.out.println("-- done --") // onComplete callback, optional
        );

        // scan operator: scan operator applies a function to each item emitted by an Observable sequentially
        //                and emits each successive value.
        // It allows us to carry forward state from event to event:

        Observable.fromArray(words)
                .scan((s, s2) -> "" + s + " is passed to " + s2)
                .subscribe(System.out::println);

        // group by: allows to classify the events in the input Observable into output categories

        Observable.fromArray(words)
                .groupBy(w -> w.length())
                .subscribe(
                        group -> {
                            group.subscribe(w -> System.out.println("word " + w + " belongs to group of size " + group.getKey()));
                        },
                        Throwable::printStackTrace,
                        () -> System.out.println("-- done --")
                );

        // filter function

        List<String> longWords = new ArrayList<>();
        Observable.fromArray(words)
                .filter(w -> w.length() > 2)
                .collect(() -> longWords, (l, w) -> l.add(w))
                .subscribe(System.out::println);

        System.out.println("long words:");
        longWords.forEach(System.out::println);

        // conditional filtering functions

        Observable.empty()
                .defaultIfEmpty("observable is empty")
                .subscribe(System.out::println);

        Observable.fromArray(words)
                .takeWhile(w -> w.length() > 2) // will stop when receiving first length<=2 word
                .subscribe(System.out::println);


        // Using operation associates resources, such as a JDBC database connection, a network connection,
        // or open files to our observables.

        Observable.using(
                () -> "this is a long, long source string",
                str -> {
                    System.out.println("using source string: " + str);
                    return Observable.fromArray(str.split(" ")); // break string into word array
                },
                r -> System.out.println("-- done --")
        ).subscribe(msg -> System.out.println("got word: " + msg));

    }
}

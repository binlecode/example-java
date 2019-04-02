package rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


/**
 * This is prob the best explanation I can find about FlatMap concept.
 * Ref url: http://reactivex.io/documentation/operators/flatmap.html
 * <p>
 * The FlatMap operator transforms an Observable by applying a function that you specify to each item emitted by
 * the source Observable, where that function returns an Observable that itself emits items.
 * FlatMap then merges the emissions of these resulting Observables, emitting these merged results as its own sequence.
 * <p>
 * This method is useful, for example, when you have an Observable that emits a series of items that themselves
 * have Observable members or are in other ways transformable into Observables, so that you can create a new
 * Observable that emits the complete collection of items emitted by the sub-Observables of these items.
 * <p>
 * Note that FlatMap merges the emissions of these Observables, so that they may interleave.
 * <p>
 * In several of the language-specific implementations there is also an operator that does not interleave the
 * emissions from the transformed Observables, but instead emits these emissions in strict order, often called
 * ConcatMap or something similar.
 */
public class TestFlatMap {

    public static void main(String[] args) {

        lowLevelFlatMap();

        simpleFlatMap();
    }

    /**
     * Show the low-level mechanical way of constructing inline Function.
     * <p/>
     * Note the functional interface in <code>.flatMap()</code> that takes in {@code List<String>} but
     * output observable with String type. The flatMap operator expects this observable and merge it into
     * the new stream it creates.
     */
    public static void lowLevelFlatMap() {
        List<List<String>> list = Arrays.asList(
                Arrays.asList("ab", "sf", "de", "fg"),
                Arrays.asList("byd", "nc", "mk"));
        System.out.println(list);

        Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) throws Exception {
                emitter.onNext(Arrays.asList("ab", "sf", "de", "fg"));
                emitter.onNext(Arrays.asList("byd", "nc", "mk"));
            }
        }).flatMap(new Function<List<String>, Observable<String>>() {
            @Override
            public Observable<String> apply(List<String> words) throws Exception {
                return Observable.fromIterable(words);
            }
        }).subscribe(System.out::println);
    }

    /**
     * This is the common (simplified syntax) way of using flatMap with lambda or method reference.
     */
    public static void simpleFlatMap() {

        List<List<String>> list = Arrays.asList(
                Arrays.asList("ab", "sf", "de", "fg"),
                Arrays.asList("byd", "nc", "mk"));
        System.out.println(list);

        System.out.println(list.stream()
                // stream method is converting a collection to an observable stream ready to be 'flattened'
                .flatMap(Collection::stream)
                // now this new flattened stream can be applied a normal mapping function (each elm is a string)
                .map(String::toUpperCase)
                .collect(Collectors.toList())
        );

        System.out.println(list.stream().count());
        System.out.println(list.stream().flatMap(Collection::stream).collect(Collectors.toList()));
        System.out.println(list.stream().map(Collection::stream).collect(Collectors.toList()));
    }


}

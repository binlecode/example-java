package rxjava;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.time.LocalDate;
import java.util.Date;
import java.util.stream.IntStream;

/**
 * In RxJava, the Observable3 plays the role of the Publisher in the Flow API, so the Observer similarly corresponds
 * to Flow’s Subscriber interface.
 * The RxJava Observer interface declares the same methods as the Java 9 <code>Subscriber</code>.
 *
 * Observable doesn’t support backpres- sure, so it doesn’t have a request method that forms part of a
 * <code>Subscription</code>.
 *
 * Observable can emit an arbitrary number of values optionally followed by completion or error (but not both).
 *
 * There are two types of Observables: code and hot
 * - cold observable: only emit when subscribed, does not cause backpressure
 * - hot observable: emit data once established regardless of subscription
 */
public class TestObservable {


    public static void main(String[] args) {
        coldObservableWithInlineClass();
        coldObserverWithLambdaFunction();
        observableFactories();
        asyncObservable();
    }

    /**
     * A synchronous cold observable construction with inline class to emit message by calling
     * emitter's event handler callbacks (onNext, onError, and onComplete).
     *
     * <code>
     * interface Observable<T> {
     *     Subscription subscribe(Observer s)
     * }
     * interface Observer<T> {
     *     void onNext(T t)
     *     void onError(Throwable t)
     *     void onComplete()
     * }
     * </code>
     *
     * It is legal to put either onError or onComplete followed by additional onNext event(s),
     * but the subscriber has already stopped consumption.
     */
    public static void coldObservableWithInlineClass() {

        // this is a cold observable, only emitting data by emitter onNext() call
        // the emitter is passed in as an argument to the in-line class's subscribe() method

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.tryOnError(new RuntimeException("blown up"));
                emitter.onNext(3);
                emitter.onComplete();
                emitter.onNext(4);
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("subscribed");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("consume: " + integer);
            }

            @Override
            public void onError(Throwable e) { System.out.println("error: " + e.getMessage()); }

            @Override
            public void onComplete() {
                System.out.println("done");
            }
        };

        // So every time you subscribe to an Observable, a new Subscriber instance is created and passed
        // to Observable create() factory method. Calling onNext() or other methods of subscriber inside
        // create() indirectly invokes the subscriber.
        observable.subscribe(observer);
    }

    /**
     * Shows definition of Observable and Subscribers using lambdas functions.
     */
    public static void coldObserverWithLambdaFunction() {

        // a lambda function is passed to the create() method which takes a functional interface argument
        Observable<LocalDate> dateStream = Observable.create(emitter -> {
            // this try-catch is a common pattern to address error propagation down to subscriber(s)
            try {
                LocalDate d = LocalDate.now();
                // generate a range of dates to emit
                IntStream.rangeClosed(1, 3).forEach(i -> {
                    LocalDate dp = d.plusDays(i);
                    System.out.println("pushing date: " + dp);
                    emitter.onNext(dp);   // feeding data to the subscriber's onNext() call
                });

                emitter.onComplete();           // subscriber will stop pulling data from now on

                emitter.onNext(d.plusDays(30));  // won't be consumed
            } catch (Exception e) {
                // pass the error as an event to the subscriber
                emitter.onError(e);
            }
        });

        // define a subscriber with lambda functions as its event callbacks
        dateStream.subscribe(
                date -> System.out.println(date),
                err -> System.out.println("error reported: \n" + err.getStackTrace()),
                () -> System.out.println("completed")
        );

        // define another subscriber, which only supplies onNext callback.
        // In other words, completion and error events are ignored
        dateStream.subscribe(date -> System.out.println("from another subscriber: " + date));
    }

    /**
     * Shows some commonly used observable factory methods.
     */
    public static void observableFactories() {
        // Observable.just(value): a shortcut to emit just a few elements and complete
        Observable<String> obsl1 = Observable.just("First Item", "Second Item");
        obsl1.subscribe(System.out::println);

        // Observable.from(values): a shortcut to emit multiple elements
        Observable<Date> obsl2 = Observable.fromArray(new Date[]{new Date(), new Date(2000, 11, 10)});
        obsl2.subscribe(elm -> System.out.println("subscriber one: " + elm));
        // an observable can have numerous subscribers
        obsl2.subscribe(elm -> System.out.println("subscriber two: " + elm));

        // Observable.range(start, count) to emit a range of integers
        Observable.range(1, 3).subscribe(System.out::println);

        // fromCallable() factory takes a callback function as a single onNext event, and wrap with try-catch
        // to support onError event
        Observable.fromCallable(() -> new Date())
                .subscribe(
                        (msg) -> { System.out.println("onNext: " + msg); },
                        (err) -> { System.out.println(("onError: " + err)); },
                        () -> { System.out.println(("onComplete")); }
                );
        // exception in callback is observed as onError event
        Observable.fromCallable(() -> { throw new RuntimeException("callback thrown error"); })
                .subscribe(
                        (msg) -> System.out.println("onNext: " + msg),
                        (err) -> System.out.println(("onError: " + err)),
                        () -> System.out.println(("onComplete"))  // won't be triggered since onError is observed
                );

    }

    /**
     * The real criteria of sync/async observable is whether the Observable event production is blocking
     * or nonblocking.
     * <p>
     * Most Observable function pipelines are synchronous (unless a specific operator needs to be async,
     * such as timeout or observeOn), whereas the Observable itself can be async.
     * <p>
     * Events (onNext(), onCompleted(), onError()) can never be emitted concurrently.
     * <p>
     * In this example, the Observable is async (it emits on a thread different from that of the subscriber),
     * so subscribe is nonblocking, and the println at the end will output before events are propagated.
     * <p>
     * However, the filter() and map() functions are synchronously executed on the calling thread that emits
     * the events. This is generally the behavior we want: an asynchronous pipeline (the Observable and composed
     * operators) with efficient synchronous computation of the events.
     */
    public static void asyncObservable() {

        Observable.create(s -> {
           new Thread(() -> {    // event emitting asynchronously
              s.onNext(1);
              s.onNext(2);
           }).start();
        }) // all the pipeline steps below are from same thread, different from the main thread
        .doOnNext(elm -> System.out.println("onNext thread: " + Thread.currentThread()))
        .filter(i -> (Integer) i > 0)
        .map(i -> "converting to string for: " + i + " by thread: " + Thread.currentThread())
        .subscribe(System.out::println);

        System.out.println("get printed first by main thread: " + Thread.currentThread());
    }

}

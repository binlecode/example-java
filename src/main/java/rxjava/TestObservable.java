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
 * Observable can emit an arbitrary number of values optionally followed by completion or error (but not both).
 *
 * there are two type of Observables: code and hot
 * - cold observable: only emit when subscribed, does not cause backpressure
 * - hot observable: emit data once established regardless of subscription
 */
public class TestObservable {


    public static void main(String[] args) {

        basicObserver();
        simpleObserver();
        observableFactory();
        asyncObserver();
    }

    /**
     * Mechanical observer construction with inline class overridden methods to show low level functionality.
     * Below is a synchronous cold observable.
     * <code>
     * interface Observable<T> {
     *     Subscription subscribe(Observer s)
     * }
     * interface Observer<T> {
     *     void onNext(T t)
     *     void onError(Throwable t)
     *     void onCompleted()
     * }
     * </code>
     * It is legal to put either onError or onComplete followed by additional onNext event(s), but the subscriber
     * has already stopped consumption.
     *
     */
    public static void basicObserver() {

        // an example of cold observable, emit data when calling subscribe(...)

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.tryOnError(new RuntimeException("blowed up"));
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
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("done");
            }
        };

        // So every time you subscribe to an Observable, a new Subscriber instance is created and passed
        // to Observable's create() factory method. Calling onNext() or other methods of subscriber inside
        // create() indirectly invokes the subscriber.
        observable.subscribe(observer);
    }

    /**
     * Simplified version of observable and subscriber defintion using lambdas.
     * <p>
     * Actual criteria of async/sync observable is whether the Observable event production is blocking or nonblocking.
     */
    public static void simpleObserver() {

        Observable<LocalDate> dateStream = Observable.create(subscriber -> {
            // this try-catch is a common pattern to address error propagation down to subscriber(s)
            try {
                LocalDate d = LocalDate.now();
                // generate a range of dates to emit
                IntStream.rangeClosed(1, 3).forEach(i -> {
                    LocalDate dp = d.plusDays(i);
                    System.out.println("pushing date: " + dp);
                    subscriber.onNext(dp);   // feeding data to the subscriber's onNext() call
                });

                subscriber.onComplete();           // subscriber will stop pulling data from now on

                subscriber.onNext(d.plusDays(30));  // won't be consumed
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

        dateStream.subscribe(
                date -> System.out.println(date),
                err -> System.out.println("error reported: \n" + err.getStackTrace()),
                () -> System.out.println("completed")
        );

        // Add another subscriber, which only supplies onNext callback.
        // In other words, completion and error events are ignored
        dateStream.subscribe(date -> System.out.println("from another subscriber: " + date));
    }

    /**
     * Show some commonly used observable factory methods.
     */
    public static void observableFactory() {
        // Observable.just(value): a shortcut to emit just one element and complete
        Observable<String> obsl1 = Observable.just("One Item");
        obsl1.subscribe(System.out::println);

        // Observable.from(values): a shortcut to emit multiple elements
        Observable<Date> obsl2 = Observable.fromArray(new Date[]{new Date(), new Date(2000, 11, 10)});
        obsl2.subscribe(elm -> System.out.println(elm));
        // an observable can have numerous subscribers
        obsl2.subscribe(elm -> System.out.println("from another subscriber: " + elm));

        // Observable.range(start, count) to emit a range of integers
        Observable.range(1, 3).subscribe(System.out::println);


        // fromCallable() factory addresses try-catch wrapped onError support with single onNext event
        Observable.fromCallable(() -> new Date()).subscribe(System.out::println);
    }

    /**
     * Most Observable function pipelines are synchronous (unless a specific operator needs to be async,
     * such as timeout or observeOn), whereas the Observable itself can be async.
     * <p>
     * events (onNext(), onCompleted(), onError()) can never be emitted concurrently.
     * <p>
     * In this example, the Observable is async (it emits on a thread different from that of the subscriber),
     * so subscribe is nonblocking, and the println at the end will output before events are propagated.
     * <p>
     * However, the filter() and map() functions are synchronously executed on the calling thread that emits
     * the events. This is generally the behavior we want: an asynchronous pipeline (the Observable and composed
     * operators) with efficient synchronous computation of the events.
     */
    public static void asyncObserver() {

        Observable.create(s -> {
           new Thread(() -> {    // event emitting asynchronously
              s.onNext(1);
              s.onNext(2);
           }).start();
        }).doOnNext(elm -> System.out.println("onNext thread: " + Thread.currentThread()))
                .filter(i -> (Integer) i > 0)
                .map(i -> "consuming: " + i)
                .subscribe(System.out::println);
        System.out.println("get printed first, main thread: " + Thread.currentThread());
    }


}

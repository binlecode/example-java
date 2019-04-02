package rxjava;


import io.reactivex.Observable;

public class HelloRx {


    public static void main(String[] args) {

        Observable<String> hey1 = Observable.just("hello rx");
        hey1.subscribe(elm -> System.out.println(elm));


        /*
         * interface Observer<T> {
         *     void onCompleted();
         *     void onError(java.lang.Throwable e);
         *     void onNext(T t);
         * }
         */
        Observable<String> hey2 = Observable.fromArray(new String[] {"a", "b", "c"})
                // map operator transforms items emitted by an Observable by applying a function to each item
                .map(elm -> elm.toUpperCase());
        hey2.subscribe(  // three methods on the Observer interface
                elm -> System.out.println(elm),  // onNext
                Throwable::printStackTrace,      // onError callback takes an Consumer functional interface
                () -> System.out.println("done") // onComplete callback takes an Action functional interface
        );

    }
}

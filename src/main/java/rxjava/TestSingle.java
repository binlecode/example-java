package rxjava;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * {@code Single} is a lazy version of {@code Future}.
 */
class TestSingle {

    public static void main(String[] args) {
        

        Single<String> ss = Single.<String>create(o -> {
            o.onSuccess("data");
        }).subscribeOn(Schedulers.io());

        ss.subscribe(System.out::println);

    }




}


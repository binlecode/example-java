package concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * In Java 8, the CompletableFuture class was introduced. Along with the Future interface,
 * it also implemented the CompletionStage interface. This interface defines the contract
 * for an asynchronous computation step that can be combined with other steps.
 * CompletableFuture is at the same time a building block and a framework with about 50
 * different methods for composing, combining, executing asynchronous computation steps
 * and handling errors.
 */
public class DemoCompletableFuture {

    protected static void threadPrint(String msg) {
        System.out.println(Thread.currentThread().getName() + ": " + msg);
    }


    public static void main(String[] args) {

        // ** simple case: use as normal Future

        CompletableFuture cf = new CompletableFuture();

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(() -> {
            try {
                threadPrint("task started");
                TimeUnit.MILLISECONDS.sleep(1000);

                // this is also equivalent to using factory method: cf = CompletableFuture.completedFuture("...")

                cf.complete("task done");  // note that Future can not be manually completed
                threadPrint("task done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        assert !cf.isDone();
        assert !cf.isCancelled();
        String result = null;
        try {
            result = (String) cf.get();
            threadPrint("got result: " + result);
        } catch (ExecutionException | InterruptedException ex) {
            //
        }

        assert cf.isDone();
        assert "task done".equals(result);

        // Static methods runAsync and supplyAsync allow us to create a CompletableFuture
        // instance out of Runnable and Supplier functional types correspondingly.

        // ** use convenient factory methods to feed task as runnable

        CompletableFuture<Void> vf = CompletableFuture.runAsync(() -> {
            threadPrint("task running");
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            threadPrint("task done");
        });

        try {
            vf.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // The most generics way to process the result of a computation is to feed it to a function.
        // The thenApply method does exactly that: accepts a Function instance, uses it to process
        // the result and returns a Future that holds a value returned by a function

        // ** form a future's callback pipeline in a series of actions

        CompletableFuture<Void> vf1 = CompletableFuture.supplyAsync(() -> {
            try {
                threadPrint("first stage running");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "first stage";
        })  // The methods with async suffix execute their tasks in a different thread from the previous task
        .thenApplyAsync(msg -> {
            try {
                threadPrint("second stage running");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return msg + " -> second stage";
        }).thenAccept(msg -> {
            threadPrint(msg + " accepted");
        });

        // in the example above, there's no difference b/t thenApply and thenApplyAsync
        // but the following examples show tasks run in separate threads.

        // The best part of the CompletableFuture API is the ability to combine CompletableFuture
        // instances in a chain of computation steps.
        // The result of this chaining is itself a CompletableFuture that allows further chaining
        // and combining.
        // This approach is ubiquitous in functional languages and is often referred to as a monadic
        // design pattern.

        // first the chaining

        CompletableFuture<String> sf1 = CompletableFuture.supplyAsync(() -> {
            try {
                threadPrint("stage 1 running");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "stage 1";
        });

        CompletableFuture<String> vf2 = sf1.thenCompose(s -> CompletableFuture.supplyAsync(() -> {
            threadPrint(s + " is chained");
            return s + " => chained";
        }));

        try {
            assert vf2.get().endsWith("=> chained");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // now the combining

        CompletableFuture<String> sf2a = sf1.thenApplyAsync(msg -> {
            try {
                threadPrint("stage 2_A running");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return msg + " -> stage 2_A";
        });


        CompletableFuture<String> sf2b = sf1.thenApplyAsync(msg -> {
            try {
                threadPrint("stage 2_B running");
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return msg + " -> stage 2_B";
        });

        sf2a.thenCombine(sf2b, (msga, msgb) -> {
            return msga + " | " + msgb;
        }).thenAccept(msg -> {
            threadPrint(msg + " accepted");
        });

        try {
            sf2a.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // When we need to execute multiple Futures in parallel, we usually want to wait for all of them
        // to execute and then process their combined results.
        // The CompletableFuture.allOf static method allows to wait for completion of all of the Futures
        // provided as a var-arg.

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            threadPrint("task A running, it is quick");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            threadPrint("task A done");
            return "Hello";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            threadPrint("task B running, it is quicker");
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            threadPrint("task B done");
            return "Beautiful";
        });
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> {
            threadPrint("task C running, it is slow");
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            threadPrint("task C done");
            return "World";
        });

        try {
            CompletableFuture.allOf(future1, future2, future3).get();
            System.out.println(future1.get() + " " + future2.get() + " " + future3.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        // fluent API (aka the call pipeline) with declarative exception handling

        // exceptionally() method gives a chance to recover by taking an alternative function that will
        // be executed if preceding calculation fails with an exception.
        // This way succeeding callbacks can continue with the alternative result as input.

        CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                threadPrint("throwing exception !");
                throw new RuntimeException("exception thrown by thread " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }).exceptionally(ex -> {
            threadPrint("handling exception: " + ex.getMessage());
            return null;
        }).thenAccept(Object::notify);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // now use .handle() as generics error handling method which takes a BiFunction<result, error>
        CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                threadPrint("throwing exception !!!");
                throw new RuntimeException("exception thrown by thread " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            return "wait is over";
        }).handle((rs, throwable) -> {
            if (throwable != null) {
                threadPrint("handling throwable: " + throwable.getMessage());
            } else {
                threadPrint(rs);
            }
            threadPrint("throwable error handled");
            return null;
        });

    }


}

package stream;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestStreamUnbounded {

    public static void main(String[] args) {

        streamFromIterate();

    }

    public static void streamFromIterate() {

        System.out.println(
                Stream.iterate(1, n -> n *2 )
                        .limit(10)
                        .collect(Collectors.toList())
        );

        // a popular example of .iterator is to generate a fibonacci sequence
        // need a two-element tuple in the iteration lambda to carry the state by input-output relation
        List<Integer> fiboList = Stream.iterate(new int[]{0, 1}, tpl -> new int[]{tpl[1], tpl[0] + tpl[1]})
                .filter(tpl -> {
                    System.out.println("fibo lamba generator array: [" + tpl[0] + ", " + tpl[1] + "]");
                    return true;
                })
                .map(tpl -> tpl[0])
                .takeWhile(n -> n <= 1000)   // cap the sequence at a max value
                .collect(Collectors.toList());
        System.out.println(fiboList);

        // the lambda can be replaced by an in-line function implementation
        // the key difference is that this is a function object instance thus it can contain internal state
        // across the iterations
        List<Integer> fiboList2 = Stream.iterate(0, new UnaryOperator<>() {
            private Integer pre = 1;   // internal state to record previous value
            private Integer ppre = 0;  // internal state to record pre-previous value
            @Override
            public Integer apply(Integer n) {
                System.out.println("n = " + n + ", pre = " + pre + ", ppre = " + ppre);
                n = pre + ppre;
                ppre = pre;
                pre = n;
                return n;
            }
        }).takeWhile(n -> n < 1000).collect(Collectors.toList());
        System.out.println(fiboList2);
    }

}

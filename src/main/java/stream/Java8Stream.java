package stream;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class Java8Stream {

    public static class Foo {
        public Long id;
        public String name;
    }

    public static void main(String[] args) {
        List<Foo> fooList = new ArrayList<>();

        //
        String sortToString = fooList.stream()
                .sorted(Comparator.comparingLong(e->e.id))
                .map(x->x.id.toString()).collect(Collectors.joining(","));

        //
        List<Foo> noDuplicate = fooList.stream().collect(
                collectingAndThen(toCollection(
                        () -> new TreeSet<>(Comparator.comparing(r -> String.format("%s,%s", r.id, r.name)))
                ), ArrayList::new)
        );

        //
        List<String> numberList = Lists.newArrayList("1", "2", "3", "4");
        Integer sum = numberList.stream().mapToInt(Integer::parseInt).sum();
        System.out.println(sum);

        //
        Map<Long, List<Foo>> groupBy = fooList.stream().collect(Collectors.groupingBy(e->e.id));

        // when conflict, use k2
        Map<Long, Foo> toMap = fooList.stream().collect(Collectors.toMap(e->e.id, e->e, (k1,k2)->k2));

        // ["hello", "world"] => ["h", "e", "l", "l", "o", "w", "o", "r", "l", "d"]
        List<String> strList = Lists.newArrayList("hello", "world");
        List<String> charList = strList.stream().flatMap(s -> {
            return Arrays.stream(s.split(""));
        }).collect(Collectors.toList());
        System.out.println("flat map: " + charList);
    }
}

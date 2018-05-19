package com.githuib.davinkevin.montyhall;

import io.vavr.Tuple2;
import io.vavr.collection.List;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static final Random seed = new Random();;

    public static void main(String[] args) {
        Integer numberOfExperimentation = 1_000_000;
        Integer numberOfDoors = 3;
        Function<Integer, Integer> asPercentage = withGlobal(numberOfExperimentation);

        Stream<Experimentation> experimentations = IntStream
                .range(0, numberOfExperimentation)
                .mapToObj(i -> new Experimentation(numberOfDoors, seed));

        Tuple2<List<Boolean>, List<Boolean>> repartitionAfterSecondTry = experimentations
                .map(e -> e.playerHasWinAfterRemove(true))
                .collect(List.collector())
                .partition(v -> v);

        Integer totalWin = asPercentage.apply(repartitionAfterSecondTry._1().size());
        Integer totalError = asPercentage.apply(repartitionAfterSecondTry._2().size());
        System.out.println("Has win " + totalWin + "%");
        System.out.println("Has loose " + totalError + "%");
    }

    private static Function<Integer, Integer> withGlobal(Integer global) {
        return v  -> v * 100 / global;
    }
}


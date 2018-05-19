package com.githuib.davinkevin.montyhall;

import com.githuib.davinkevin.montyhall.Experimentation.ExperimentationResult;
import io.vavr.collection.List;
import io.vavr.collection.Map;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.githuib.davinkevin.montyhall.Experimentation.ExperimentationResult.GAME_ABORTED;
import static com.githuib.davinkevin.montyhall.Experimentation.ExperimentationResult.PLAYER_LOOSE;
import static com.githuib.davinkevin.montyhall.Experimentation.ExperimentationResult.PLAYER_WINS;

public class Main {

    private static final Random seed = new Random();;

    public static void main(String[] args) {
        Integer numberOfExperimentation = 1_000_000;
        Integer numberOfDoors = 3;
        Function<Integer, Integer> asPercentage = withGlobal(numberOfExperimentation);

        Stream<Experimentation> experimentations = IntStream
                .range(0, numberOfExperimentation)
                .mapToObj(i -> new Experimentation(numberOfDoors, seed));

        Map<ExperimentationResult, List<ExperimentationResult>> repartition = experimentations
                .map(e -> e.playerHasWinAfterRemove(true))
                .collect(List.collector())
                .groupBy(v -> v);

        Integer totalWin = asPercentage.apply(repartition.get(PLAYER_WINS).get().size());
        Integer totalLoose = asPercentage.apply(repartition.get(PLAYER_LOOSE).get().size());
        Integer totalAborted = asPercentage.apply(repartition.get(GAME_ABORTED).get().size());

        System.out.println("Has win " + totalWin + "%");
        System.out.println("Has loose " + totalLoose + "%");
        System.out.println("Has aborted " + totalAborted + "%");
    }

    private static Function<Integer, Integer> withGlobal(Integer global) {
        return v  -> v * 100 / global;
    }
}


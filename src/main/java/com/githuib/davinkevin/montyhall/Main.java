package com.githuib.davinkevin.montyhall;

import com.githuib.davinkevin.montyhall.Experimentation.ExperimentationResult;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.collection.Traversable;

import java.util.Random;
import java.util.function.Function;

import static com.githuib.davinkevin.montyhall.Experimentation.ExperimentationResult.*;

public class Main {

    private static final Random seed = new Random();

    public static void main(String[] args) {
        Integer numberOfExperimentation = 1_000_000;
        Integer numberOfDoors = 1000;

        Map<ExperimentationResult, List<ExperimentationResult>> repartition = Stream.from(0)
                .take(numberOfExperimentation)
                .map(i -> new Experimentation(numberOfDoors, seed))

                // .map(Experimentation::playerHasWinAfterRemoveAndRandomPresentator)
                .map(e -> e.playerHasWinAfterRemoveAndOmniscientPresentator(true))

                .toList()
                .groupBy(v -> v);

        Function<ExperimentationResult, Double> from = withRepartition(repartition);

        Double totalWin = from.apply(PLAYER_WINS);
        Double totalLoose = from.apply(PLAYER_LOOSE);
        Double totalAborted = from.apply(GAME_ABORTED);

        System.out.println(String.format("Has win %1$,.2f%%", totalWin));
        System.out.println(String.format("Has loose %1$,.2f%%", totalLoose));
        System.out.println(String.format("Has been aborted %1$,.2f%%", totalAborted));
    }

    private static Function<Integer, Double> withGlobal(Integer global) {
        return v  -> v.doubleValue() * 100 / global;
    }

    private static Function<ExperimentationResult, Double> withRepartition(Map<ExperimentationResult, List<ExperimentationResult>> repartition) {
        return e -> repartition.get(e)
                .map(Traversable::size)
                .map(withGlobal(repartition.flatMap(Tuple2::_2).size()))
                .getOrElse(0d);
    }
}


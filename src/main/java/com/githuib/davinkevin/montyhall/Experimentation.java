package com.githuib.davinkevin.montyhall;


import io.vavr.Tuple2;
import io.vavr.collection.List;
import lombok.extern.java.Log;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

@Log
public class Experimentation {

    private final Random seed;
    private final List<Door> doors;

    Experimentation(Integer numberOfDoors, Random seed) {
        this.seed = seed;
        this.doors = generateDoors(numberOfDoors);
    }

    Boolean playerHasWinAtFirstTry() {
        Integer choice = randomChoice(doors.size());
        return doors.get(choice).has(Price.CAR);
    }

    Boolean playerHasWinAfterRemove(Boolean isChanging) {
        Door firstDoorChoice = this.doors.get(randomChoice(doors.size()));

        List<Tuple2<Door, Integer>> presentatorPossibilities = doors
                .zipWithIndex()
                .filter(v -> !v._1().equals(firstDoorChoice))
                .filter(v -> v._1().has(Price.NOTHING))
                ;

        Door presentatorChoice = presentatorPossibilities
                .get(randomChoice(presentatorPossibilities.size()))
                ._1();

        List<Door> doorsAfterPresentatorModification = this.doors
                .filter(v -> !v.equals(presentatorChoice));

        Door finalChoice;
        if (isChanging) {
            finalChoice = doorsAfterPresentatorModification
                    .filter(v -> !v.equals(firstDoorChoice))
                    .head();
        } else {
            finalChoice = firstDoorChoice;
        }

        return finalChoice.has(Price.CAR);
    }

    private Integer randomChoice(Integer maxChoice) {
        return seed.nextInt(maxChoice);
    }

    private static List<Door> generateDoors(Integer number) {
        final Random random = new Random();

        Integer positionOfPrice = random.nextInt(number);

        return IntStream.range(0, number-1)
                .mapToObj(i -> new Door(Price.NOTHING))
                .collect(List.collector())
                .insert(positionOfPrice, new Door(Price.CAR));
    }

    private static Function<Boolean, Price> toPrice() {
        return b -> b ? Price.CAR : Price.NOTHING;
    }
}

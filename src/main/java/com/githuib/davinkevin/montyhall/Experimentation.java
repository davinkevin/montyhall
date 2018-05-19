package com.githuib.davinkevin.montyhall;


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

    Boolean playerHasWinAfterRemove(Boolean isChanging) {
        Door firstDoorChoice = randomChoiceIn(doors);

        Door presentatorChoice = firstDoorChoice.has(Price.CAR)
                ? randomChoiceIn(doors.filter(v -> !v.equals(firstDoorChoice)))
                : doors.filter(v -> v.has(Price.CAR)).get();

        Door finalChoice = isChanging ? presentatorChoice : firstDoorChoice;

        return finalChoice.has(Price.CAR);
    }

    private Door randomChoiceIn(List<Door> list) {
        return list.get(seed.nextInt(list.size()));
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

package com.githuib.davinkevin.montyhall;


import io.vavr.collection.List;
import io.vavr.collection.Stream;
import lombok.extern.java.Log;

import java.util.Random;

import static com.githuib.davinkevin.montyhall.Experimentation.ExperimentationResult.*;

@Log
public class Experimentation {

    private final Random seed;
    private final List<Door> doors;

    Experimentation(Integer numberOfDoors, Random seed) {
        this.seed = seed;
        this.doors = generateDoors(numberOfDoors);
    }

    ExperimentationResult playerHasWinAfterRemoveAndRandomPresentator() {
        Door firstDoorChoice = randomChoiceIn(doors);
        Door presentatorChoice = randomChoiceIn(doors.remove(firstDoorChoice));

        if (presentatorChoice.has(Price.CAR)) {
            return GAME_ABORTED;
        }

        return firstDoorChoice.has(Price.CAR) ? PLAYER_WINS : PLAYER_LOOSE;
    }

    ExperimentationResult playerHasWinAfterRemoveAndOmniscientPresentator(Boolean isChanging) {
        Door firstDoorChoice = randomChoiceIn(doors);

        Door presentatorChoice = firstDoorChoice.has(Price.CAR)
                ? randomChoiceIn(doors.remove(firstDoorChoice))
                : doors.filter(v -> v.has(Price.CAR)).get();

        Door finalChoice = isChanging ? presentatorChoice : firstDoorChoice;

        return finalChoice.has(Price.CAR) ? PLAYER_WINS : PLAYER_LOOSE;
    }

    private Door randomChoiceIn(List<Door> list) {
        return list.get(seed.nextInt(list.size()));
    }

    private static List<Door> generateDoors(Integer number) {
        return Stream.from(0)
                .take(number-1)
                .map(i -> Door.with(Price.NOTHING))
                .toList()
                .push(Door.with(Price.CAR))
                .shuffle();
    }

    public enum ExperimentationResult {
        PLAYER_WINS, PLAYER_LOOSE, GAME_ABORTED;
    }
}

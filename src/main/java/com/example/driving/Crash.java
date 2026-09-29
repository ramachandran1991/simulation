package com.example.driving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Crash implements CarOutcome{

        private final String carName;
        private final List<String> otherCars;
        private final Position position;
        private final int step;

        public Crash(String carName, List<String> otherCars, Position position, int step) {
        this.carName = carName;
        this.otherCars = Collections.unmodifiableList(new ArrayList<>(otherCars));
        this.position = position;
        this.step = step;
    }

        @Override
        public String carName() {
        return carName;
    }

        public List<String> otherCars() {
        return otherCars;
    }

        public Position position() {
        return position;
    }

        public int step() {
        return step;
    }

        @Override
        public String toString() {
        return carName + ", crased with " + String.join(", ", otherCars)
                + " at " + position + " at step " + step;
    }

}

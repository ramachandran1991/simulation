package com.example.driving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface CarOutcome {

    String carName();

    final class Finished implements CarOutcome {
        private final String carName;
        private final Position position;
        private final Direction facing;

        public Finished(String carName, Position position, Direction facing) {
            this.carName = carName;
            this.position = position;
            this.facing = facing;
        }

        @Override
        public String carName() {
            return carName;
        }

        public Position position() {
            return position;
        }

        public Direction facing() {
            return facing;
        }

        @Override
        public String toString() {
            return carName + ", " + position + " " + facing;
        }
    }

}

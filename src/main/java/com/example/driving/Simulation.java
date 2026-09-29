package com.example.driving;

import java.util.ArrayList;
import java.util.List;

// Crashed cars stop but stay on the board.
public final class Simulation {

    private static final class State {
        final Car car;
        Position position;
        Direction facing;
        Crash collision;

        State(Car car) {
            this.car = car;
            this.position = car.start();
            this.facing = car.facing();
        }

        boolean crashed() {
            return collision != null;
        }
    }

    public List<CarOutcome> run(Field field, List<Car> cars) {
        List<State> states = new ArrayList<>();
        int maxSteps = 0;
        for (Car car : cars) {
            if (!field.contains(car.start())) {
                throw new ValidationException("Car " + car.name() + " starts outside the field.");
            }
            states.add(new State(car));
            maxSteps = Math.max(maxSteps, car.commands().size());
        }

        for (int step = 1; step <= maxSteps; step++) {
            for (State s : states) {
                if (!s.crashed() && step <= s.car.commands().size()) {
                    apply(field, s, s.car.commands().get(step - 1));
                }
            }
            detectCollisions(states, step);
        }

        List<CarOutcome> outcomes = new ArrayList<>();
        for (State s : states) {
            outcomes.add(s.crashed() ? s.collision : new CarOutcome.Finished(s.car.name(), s.position, s.facing));
        }
        return outcomes;
    }

    private static void apply(Field field, State s, Command command) {
        switch (command) {
            case L:
                s.facing = s.facing.turnLeft();
                break;
            case R:
                s.facing = s.facing.turnRight();
                break;
            case F:
                Position next = s.position.move(s.facing);
                if (field.contains(next)) {
                    s.position = next;
                }
                break;
            default:
                throw new IllegalStateException("Unhandled command " + command);
        }
    }

    private static void detectCollisions(List<State> states, int step) {
        // work out who crashed first, then record it, otherwise marking one car changes the answer for the next
        List<State> newlyCrashed = new ArrayList<>();
        for (State s : states) {
            if (!s.crashed() && !othersAt(states, s).isEmpty()) {
                newlyCrashed.add(s);
            }
        }
        for (State s : newlyCrashed) {
            s.collision = new Crash(s.car.name(), othersAt(states, s), s.position, step);
        }
    }

    private static List<String> othersAt(List<State> states, State s) {
        List<String> names = new ArrayList<>();
        for (State o : states) {
            if (o != s && o.position.equals(s.position)) {
                names.add(o.car.name());
            }
        }
        return names;
    }
}

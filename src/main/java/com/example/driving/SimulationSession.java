package com.example.driving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// The field plus the cars added so far.
public final class SimulationSession {

    private final Field field;
    private final List<Car> cars = new ArrayList<>();

    public SimulationSession(Field field) {
        this.field = field;
    }

    public Field field() {
        return field;
    }

    public List<Car> cars() {
        return Collections.unmodifiableList(cars);
    }

    public void checkName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Car name must not be empty.");
        }
        for (Car c : cars) {
            if (c.name().equals(name)) {
                throw new ValidationException("A car named '" + name + "' already exists.");
            }
        }
    }

    public void checkStartPosition(Position position) {
        if (!field.contains(position)) {
            throw new ValidationException("Position " + position + " is outside the "
                    + field.width() + " x " + field.height() + " field.");
        }
        for (Car c : cars) {
            if (c.start().equals(position)) {
                throw new ValidationException("Position " + position + " is already occupied by car "
                        + c.name() + ".");
            }
        }
    }

    public void addCar(Car car) {
        checkName(car.name());
        checkStartPosition(car.start());
        cars.add(car);
    }

    public List<CarOutcome> run() {
        if (cars.isEmpty()) {
            throw new ValidationException("Add at least one car before running the simulation.");
        }
        return new Simulation().run(field, cars);
    }
}

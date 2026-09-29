package com.example.driving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Car {

    private final String name;
    private final Position start;
    private final Direction facing;
    private final List<Command> commands;

    public Car(String name, Position start, Direction facing, List<Command> commands) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Car name must not be empty or null.");
        }
        this.name = name;
        this.start = start;
        this.facing = facing;
        this.commands = Collections.unmodifiableList(new ArrayList<>(commands));
    }

    public String name() {
        return name;
    }

    public Position start() {
        return start;
    }

    public Direction facing() {
        return facing;
    }

    public List<Command> commands() {
        return commands;
    }

    public String commandString() {
        StringBuilder sb = new StringBuilder();
        for (Command c : commands) {
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return name + ", " + start + " " + facing + ", " + commandString();
    }
}

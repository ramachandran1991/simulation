package com.example.driving;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public final class Cli {

    private static final class EndOfInput extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private final BufferedReader in;
    private final PrintStream out;

    public Cli(BufferedReader in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public void run() {
        out.println("Welcome to Car Crash!");
        try {
            boolean again = true;
            while (again) {
                SimulationSession session = new SimulationSession(promptField());
                runSession(session);
                again = promptStartOverOrExit();
            }
        } catch (EndOfInput e) {
            out.println();
        }
        out.println("Thank you for running the simulation. Goodbye!");
    }

    private Field promptField() {
        out.println();
        out.println("Please enter the width and height of the simulation field in x y format:");
        Field field = readValid(line -> {
            String[] parts = splitTokens(line, 2, "Expected two numbers, e.g. '10 10'.");
            return new Field(parseInt(parts[0]), parseInt(parts[1]));
        });
        out.println();
        out.println("You have created a field of " + field.width() + " x " + field.height() + ".");
        return field;
    }

    private void runSession(SimulationSession session) {
        while (true) {
            out.println();
            out.println("Please choose from the following options to proceed:");
            out.println("[1] Add a car to field");
            out.println("[2] Run simulation");
            String choice = readLine().trim();
            if (choice.equals("1")) {
                addCar(session);
            } else if (choice.equals("2")) {
                if (runSimulation(session)) {
                    return;
                }
            } else {
                out.println("Invalid option '" + choice + "'. Please enter 1 or 2.");
            }
        }
    }

    private void addCar(SimulationSession session) {
        out.println();
        out.println("Please enter the name of the car:");
        final String name = readValid(line -> {
            String trimmed = line.trim();
            session.checkName(trimmed);
            return trimmed;
        });

        out.println();
        out.println("Please enter initial position of car " + name + " in x y Direction format:");
        // check the position now so a typo doesn't cost them the commands they typed
        Car placement = readValid(line -> {
            String[] parts = splitTokens(line, 3, "Expected 'x y Direction', e.g. '1 2 N'.");
            Position start = new Position(parseInt(parts[0]), parseInt(parts[1]));
            Direction facing = Direction.parse(parts[2]);
            session.checkStartPosition(start);
            return new Car(name, start, facing, Collections.<Command>emptyList());
        });

        out.println();
        out.println("Please enter the commands for car " + name + ":");
        List<Command> commands = readValid(Command::parseAll);

        session.addCar(new Car(name, placement.start(), placement.facing(), commands));
        printCars(session);
    }

    private boolean runSimulation(SimulationSession session) {
        List<CarOutcome> outcomes;
        try {
            outcomes = session.run();
        } catch (ValidationException e) {
            out.println(e.getMessage());
            return false;
        }
        printCars(session);
        out.println();
        out.println("After simulation, the result is:");
        for (CarOutcome outcome : outcomes) {
            out.println("- " + outcome);
        }
        return true;
    }

    private boolean promptStartOverOrExit() {
        while (true) {
            out.println();
            out.println("Please choose from the following options:");
            out.println("[1] Start over");
            out.println("[2] Exit");
            String choice = readLine().trim();
            if (choice.equals("1")) {
                return true;
            }
            if (choice.equals("2")) {
                out.println();
                return false;
            }
            out.println("Invalid option '" + choice + "'. Please enter 1 or 2.");
        }
    }

    private void printCars(SimulationSession session) {
        out.println();
        out.println("Your current list of cars are:");
        for (Car car : session.cars()) {
            out.println("- " + car);
        }
    }

    private <T> T readValid(Function<String, T> parser) {
        while (true) {
            try {
                return parser.apply(readLine());
            } catch (ValidationException e) {
                out.println(e.getMessage() + " Please try again:");
            }
        }
    }

    private String readLine() {
        try {
            String line = in.readLine();
            if (line == null) {
                throw new EndOfInput();
            }
            return line;
        } catch (IOException e) {
            throw new EndOfInput();
        }
    }

    private static String[] splitTokens(String line, int expected, String usage) {
        String trimmed = line.trim();
        String[] parts = trimmed.isEmpty() ? new String[0] : trimmed.split("\\s+");
        if (parts.length != expected) {
            throw new ValidationException(usage);
        }
        return parts;
    }

    private static int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new ValidationException("'" + text + "' is not a valid whole number.");
        }
    }
}

package com.example.driving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Command {
    L, R, F;

    public static Command parse(char c) {
        return switch (Character.toUpperCase(c)) {
            case 'L' -> L;
            case 'R' -> R;
            case 'F' -> F;
            default -> throw new ValidationException("Invalid command '" + c + "'. Only L, R and F are allowed.");
        };
    }

    public static List<Command> parseAll(String text) {
        List<Command> commands = new ArrayList<>();
        for (char c : text.trim().toCharArray()) {
            commands.add(parse(c));
        }
        return Collections.unmodifiableList(commands);
    }
}

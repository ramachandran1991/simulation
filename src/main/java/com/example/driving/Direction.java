package com.example.driving;

public enum Direction {
    N(0, 1), E(1, 0), S(0, -1), W(-1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int dx() {
        return dx;
    }

    public int dy() {
        return dy;
    }

    public Direction turnLeft() {
        return values()[(ordinal() + 3) % 4];
    }

    public Direction turnRight() {
        return values()[(ordinal() + 1) % 4];
    }

    public static Direction parse(String text) {
        try {
            return valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown direction '" + text + "'. Use one of these N, E, S, W.");
        }
    }
}

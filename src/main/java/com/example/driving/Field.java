package com.example.driving;

public final class Field {

    private final int width;
    private final int height;

    public Field(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new ValidationException("Field width and height must both be positive.");
        }
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean contains(Position p) {
        return p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
    }
}

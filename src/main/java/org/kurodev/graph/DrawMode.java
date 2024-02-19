package org.kurodev.graph;

public enum DrawMode {
    LINES(false, true),
    POINTS(true, false),
    POINTS_AND_LINES(true, true);
    private boolean drawPoints;
    private boolean drawLines;

    DrawMode(boolean points, boolean lines) {
        drawPoints = points;
        drawLines = lines;
    }

    @Override
    public String toString() {
        return name().replace("_", " ").toLowerCase();
    }

    public boolean drawPoints() {
        return drawPoints;
    }

    public boolean drawLines() {
        return drawLines;
    }
}

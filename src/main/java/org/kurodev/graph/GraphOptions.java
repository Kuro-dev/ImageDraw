package org.kurodev.graph;

import java.util.Objects;

public final class GraphOptions {
    private DrawMode mode;
    private int imageWidth;
    private int imageHeight;
    private int graphPoints;
    private double graphSteps;

    public GraphOptions(int imageWidth, int imageHeight, int graphPoints, double graphSteps, DrawMode mode) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.graphPoints = graphPoints;
        this.graphSteps = graphSteps;
        this.mode = mode;
    }

    public static GraphOptions defaultOptions() {
        return new GraphOptions(500, 500, 10, 1d, DrawMode.POINTS_AND_LINES);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GraphOptions) obj;
        return this.imageWidth == that.imageWidth && this.imageHeight == that.imageHeight && this.graphPoints == that.graphPoints && Double.doubleToLongBits(this.graphSteps) == Double.doubleToLongBits(that.graphSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageWidth, imageHeight, graphPoints, graphSteps);
    }

    @Override
    public String toString() {
        return "GraphOptions[" + "imageWidth=" + imageWidth + ", " + "imageHeight=" + imageHeight + ", " + "graphPoints=" + graphPoints + ", " + "graphSteps=" + graphSteps + ']';
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public GraphOptions setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        return this;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public GraphOptions setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        return this;
    }

    public int getGraphPoints() {
        return graphPoints;
    }

    public GraphOptions setGraphPoints(int graphPoints) {
        this.graphPoints = graphPoints;
        return this;
    }

    public double getGraphSteps() {
        return graphSteps;
    }

    public GraphOptions setGraphSteps(double graphSteps) {
        this.graphSteps = graphSteps;
        return this;
    }

    public DrawMode getMode() {
        return mode;
    }

    public GraphOptions setMode(DrawMode mode) {
        this.mode = mode;
        return this;
    }
}

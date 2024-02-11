package org.kurodev.graph;

import java.util.Objects;

public final class GraphOptions {
    private int imageWidth;
    private int imageHeight;
    private int graphPoints;
    private double graphSteps;

    public GraphOptions(int imageWidth, int imageHeight, int graphPoints, double graphSteps) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.graphPoints = graphPoints;
        this.graphSteps = graphSteps;
    }

    public static GraphOptions defaultOptions() {
        return new GraphOptions(500, 500, 10, 1d);
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public void setGraphPoints(int graphPoints) {
        this.graphPoints = graphPoints;
    }

    public void setGraphSteps(double graphSteps) {
        this.graphSteps = graphSteps;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getGraphPoints() {
        return graphPoints;
    }

    public double getGraphSteps() {
        return graphSteps;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GraphOptions) obj;
        return this.imageWidth == that.imageWidth &&
                this.imageHeight == that.imageHeight &&
                this.graphPoints == that.graphPoints &&
                Double.doubleToLongBits(this.graphSteps) == Double.doubleToLongBits(that.graphSteps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageWidth, imageHeight, graphPoints, graphSteps);
    }

    @Override
    public String toString() {
        return "GraphOptions[" +
                "imageWidth=" + imageWidth + ", " +
                "imageHeight=" + imageHeight + ", " +
                "graphPoints=" + graphPoints + ", " +
                "graphSteps=" + graphSteps + ']';
    }

}

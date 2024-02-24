package org.kurodev.graph;

import org.kurodev.graph.calculator.Calculator;
import org.kurodev.graph.kimage.kimage.draw.DrawableImage;
import org.kurodev.graph.kimage.kimage.img.PixelColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KGraph {
    private static final Logger logger = LoggerFactory.getLogger(KGraph.class);
    private final GraphOptions options;

    public KGraph() {
        options = GraphOptions.defaultOptions();
    }

    public GraphOptions getOptions() {
        return options;
    }

    public List<GraphPoint> calculatePoints(String formula) {
        double points = options.getGraphPoints();
        double step = options.getGraphSteps();
        List<GraphPoint> calculatedPoints = new ArrayList<>((int) ((points * 2) / step));
        Calculator calculator = new Calculator();
        try {
            for (double x = -points; x <= points; x += step) {
                calculator.getVariables().put("x", BigDecimal.valueOf(x));
                BigDecimal calc = calculator.evaluate(formula);
                calculatedPoints.add(new GraphPoint(x, calc.doubleValue()));
            }
        } catch (Exception e) {
            logger.error("Error when trying to calculate", e);
            throw new RuntimeException("Invalid expression: " + formula);
        }

        return calculatedPoints;
    }

    public DrawableImage createGraph(String equation) {
        var points = calculatePoints(equation);
        return createGraph(points);
    }

    public DrawableImage createGraph(List<GraphPoint> points) {
        logger.debug("Drawing graph for {} points", points.size());
        int width = options.getImageWidth();
        int height = options.getImageHeight();
        DrawableImage img = new DrawableImage(width, height);
        //create a blank canvas
        img.fill(PixelColor.WHITE);

        //approx center of image
        int imageCenterX = width >> 1;
        int imageCenterY = height >> 1;
        //image positions, to make the graph have a little border
        int startX = (int) (width * 0.05);
        int endX = width - startX;
        //image positions, to make the graph have a little border
        int startY = (int) (height * 0.05);
        int endY = height - startY;

        img.drawLine(startX, imageCenterY, endX, imageCenterY, PixelColor.BLACK);
        img.drawLine(imageCenterX, startY, imageCenterX, endY, PixelColor.BLACK);

        //---- DRAW INTERVAL MARKERS ----

        // Total number of intervals across the entire axis
        double totalIntervalsX = Math.round(points.stream()
                .map(GraphPoint::x)
                .max(Double::compareTo).orElse(options.getGraphSteps()));
        double totalIntervalsY = Math.round(points.stream()
                .map(GraphPoint::y)
                .max(Double::compareTo).orElse(options.getGraphSteps()));

        // Calculate the width of each interval across the full width of the axis
        double intervalPixelsX = ((double) imageCenterX - startX) / totalIntervalsX;
        for (int i = 1; i <= totalIntervalsX; i++) {
            int xNegative = (int) Math.round(imageCenterX - (i * intervalPixelsX));
            int xPositive = (int) Math.round(imageCenterX + (i * intervalPixelsX));
            img.drawLine(xNegative, imageCenterY, xNegative, (int) (imageCenterY * 1.03), PixelColor.BLACK);
            img.drawLine(xPositive, imageCenterY, xPositive, (int) (imageCenterY * 1.03), PixelColor.BLACK);
        }

        double intervalPixelsY = ((double) imageCenterY - startY) / totalIntervalsY;
        for (int i = 1; i <= totalIntervalsY; i++) {
            int yPositive = (int) Math.round(imageCenterY - (i * intervalPixelsY));
            int yNegative = (int) Math.round(imageCenterY + (i * intervalPixelsY));
            int offset = (int) (imageCenterX * (0.03 / 2));
            double xPos1 = (imageCenterX + offset);
            double xPos2 = (imageCenterX - offset - 1);
            img.drawLine((int) xPos1, yPositive, (int) (xPos2), yPositive, PixelColor.BLACK);
            img.drawLine((int) xPos1, yNegative, (int) (xPos2), yNegative, PixelColor.BLACK);
        }

        int lineSize = 2;
        Integer lastX = null;
        Integer lastY = null;

        boolean drawLines = options.getMode().drawLines();
        boolean drawPoints = options.getMode().drawPoints();
        for (GraphPoint graphPoint : points) {
            int imgX = (int) Math.round(imageCenterX + (graphPoint.x() * intervalPixelsX));
            int imgY = (int) Math.round(imageCenterY - (graphPoint.y() * intervalPixelsY));
            if (drawLines) {
                if (lastX != null) {
                    img.drawLine(lastX, lastY, imgX, imgY, PixelColor.BLACK);
                }
                lastX = imgX;
                lastY = imgY;
            }
            if (drawPoints) {
                img.drawLine(imgX + lineSize, imgY, Math.max(imgX - lineSize - 1, 0), imgY, PixelColor.BLACK);
                img.drawLine(imgX, imgY + lineSize, imgX, Math.max(imgY - lineSize - 1, 0), PixelColor.BLACK);
            }
        }

        return img;
    }

    /**
     * Create an Empty Graph.
     *
     * @return A Drawable Image with an Empty Graph
     */
    public DrawableImage createGraph() {
        return createGraph(Collections.emptyList());
    }

}

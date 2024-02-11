package org.kurodev.graph;

import org.kurodev.calculator.maths.Calculation;
import org.kurodev.calculator.maths.FormulaParser;
import org.kurodev.kimage.draw.DrawableImage;
import org.kurodev.kimage.img.PixelColor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class KGraph {
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
        FormulaParser parser = new FormulaParser();
        for (double x = -points; x <= points; x = +step) {
            parser.getVariables().put("x", BigDecimal.valueOf(x));
            Calculation calc = parser.calculate(formula);
            if (calc.isNumber()) {
                calculatedPoints.add(new GraphPoint(x, calc.getResult().doubleValue()));
            } else {
                throw new RuntimeException(calc.toString());
            }
        }
        return calculatedPoints;
    }

    public DrawableImage draw(List<GraphPoint> points) {
        return null;
    }

    /**
     * Create an Empty Graph.
     *
     * @return A Drawable Image with an Empty Graph
     */
    public DrawableImage createGraph() {
        int width = options.getImageWidth();
        int height = options.getImageHeight();
        DrawableImage img = new DrawableImage(width, height);
        //create a blank canvas
        img.fill(PixelColor.WHITE);

        //approx center of image
        int imageCenterX = width >> 1;
        int imageCenterY = width >> 1;
        //image positions, to make the graph have a little border
        int startX = (int) (width * 0.05);
        int endX = width - startX;
        //image positions, to make the graph have a little border
        int startY = (int) (height * 0.05);
        int endY = height - startY;

        img.drawLine(startX, imageCenterY, endX, imageCenterY, PixelColor.BLACK);
        img.drawLine(imageCenterX, startY, imageCenterX, endY, PixelColor.BLACK);

        int innerWidth = endX - startX;

        //---- DRAW INTERVAL MARKERS ----


        int totalIntervals = options.getGraphPoints() * 2 + 1; // Total number of intervals across the entire axis
        // Calculate the width of each interval across the full width of the axis
        int intervalPixels = innerWidth / totalIntervals;
        for (int i = 0; i < totalIntervals; i++) {

            // Draw point lines across the entire axis
            int xPos = (startX + (i * intervalPixels));
            if (i == options.getGraphPoints() + 1) {
                if (xPos == imageCenterX) {
                    System.err.println("I MET THE CENTER!");
                } else {
                    System.out.printf("This should be the center at %d but was at %d%n", imageCenterX, xPos);
                }
            }

            img.drawLine(xPos, imageCenterY, xPos, (int) (imageCenterY * 1.03), PixelColor.BLACK);
        }

        return img;
    }

}

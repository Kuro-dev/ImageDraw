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

        // Total number of intervals across the entire axis
        int totalIntervals = options.getGraphPoints();

        // Calculate the width of each interval across the full width of the axis
        double intervalPixelsX = ((double) imageCenterX - startX) / totalIntervals;
        for (int i = 1; i <= totalIntervals; i++) {
            int xNegative = (int) Math.round(imageCenterX - (i * intervalPixelsX));
            int xPositive = (int) Math.round(imageCenterX + (i * intervalPixelsX));
            img.drawLine(xNegative, imageCenterY, xNegative, (int) (imageCenterY * 1.03), PixelColor.BLACK);
            img.drawLine(xPositive, imageCenterY, xPositive, (int) (imageCenterY * 1.03), PixelColor.BLACK);
        }

        double intervalPixelsY = ((double) imageCenterY - startY) / totalIntervals;
        for (int i = 1; i <= totalIntervals; i++) {
            int yPositive = (int) Math.round(imageCenterY - (i * intervalPixelsY));
            int yNegative = (int) Math.round(imageCenterY + (i * intervalPixelsY));
            int offset = (int) (imageCenterX * (0.03 / 2));
            double xPos1 = (imageCenterX + offset);
            double xPos2 = (imageCenterX - offset - 1);
            img.drawLine((int) xPos1, yPositive, (int) (xPos2), yPositive, PixelColor.BLACK);
            img.drawLine((int) xPos1, yNegative, (int) (xPos2), yNegative, PixelColor.BLACK);
        }

        return img;
    }

}

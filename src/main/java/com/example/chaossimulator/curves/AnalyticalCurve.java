package com.example.chaossimulator.curves;

import javafx.scene.canvas.GraphicsContext;

public interface AnalyticalCurve {

    /**
     * Draw an analytical curve on a GraphicsContext
     * @param curve The AnalyticalCurve object we want to display
     * @param gc The GraphicsContext support to draw on
     */
    static void drawCurve(AnalyticalCurve curve, GraphicsContext gc) {

        // Previous point calculated (i - 1 iteration)
        double previousX = 0;
        double previousY = curve.computeY(0);

        // Actual point calculated (i iteration)
        double actualX;
        double actualY;

        // For every possible point, stroke a line between it and the previous one
        for(double x = 0; x <= gc.getCanvas().getWidth(); ++x) {
            actualX = x;
            actualY = curve.computeY(x);

            gc.strokeLine(previousX, previousY, actualX, actualY);

            previousX = actualX;
            previousY = actualY;
        }
    }

    /**
     * Calculate the y coordinate of the analytical curve given the x coordinate.
     * @param x the X coordinate of the point
     * @return the y coordinate of the point
     */
    double computeY(double x);

}

package com.example.chaossimulator.curves;

import javafx.scene.canvas.GraphicsContext;

/*
TO DO

- cambia le costanti RIGHT SIDE X e LEFT SIDE X con delle enum
 */

public interface AnalyticalCurve {

    /**
     * Since the analytical curves are often non-invertible functions, when trying
     * to determine the x coordinate given the y the equation return
     * two points: a right_side point and a left-side point.
     * This constant is used to catch the right-side point of the
     * result when using the computeX() function.
     */
    public static final boolean RIGHT_SIDE_X = true;


    /**
     * Since the analytical curves are often non-invertible functions, when trying
     * to determine the x coordinate given the y the equation return
     * two points: a right_side point and a left-side point.
     * This constant is used to catch the left-side point of the
     * result when using the computeX() function.
     */
    public static final boolean LEFT_SIDE_X = false;

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
     * Checks if the 2D point passed is contained in the respective analytical curve
     * @param x the X coordinate of the point to check
     * @param y the Y coordinate of the point to check
     * @return a boolean type, true if the curve contains the point, false if not.
     */
    boolean containsPoint(double x, double y);

    /**
     * Calculate the y coordinate of the analytical curve given the x coordinate.
     * @param x the X coordinate of the point
     * @return the y coordinate of the point
     */
    double computeY(double x);


    /**
     * Calculate the x coordinate of the analytical curve given the y coordinate.
     * Remember that if the function is non-invertible, you should choose the
     * x coordinate by passing a boolean to this function
     * @param y the Y coordinate of the point
     * @param side value used to specify one of the two possible x result coordinates.
     *        Consider the constants RIGHT_SIDE_X and LEFT_SIDE_X to choose
     * @return the x coordinate of the point
     */
    //double computeX(double y, boolean side);


    /**
     * Decides which x coordinate to choose when using the computeX function.
     * The function takes an x coordinate and use it to decide the right side
     * @param x coordinate used to define a side to be chosen
     * @return a boolean representing the right side or the left side, according
     *              to the RIGHT_SIDE_X and LEFT_SIDE_X constants notation
     */
    //boolean chooseXCoordinate(double x);
}

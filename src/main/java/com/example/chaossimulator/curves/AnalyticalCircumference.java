package com.example.chaossimulator.curves;

import javafx.scene.canvas.GraphicsContext;

/**
 * Class extending the AnalyticalCurve interface and
 * representing a Circumference with equation (x + xDrag)^2 / a^ 2  +  (y - c)^2 / b^2  =  1
 */
public class AnalyticalCircumference implements AnalyticalCurve {

    // (x + xDrag)^2 / a^ 2  +  (y - c)^2 / b^2  =  1

    private double a;
    private double b;
    private double c;
    private double xDrag;


    /**
     * Constructs a circumference given its coefficients a, b, c and xDrag and a
     * GraphicsContext as an absolute frame of reference
     * @param a coefficient of the x coordinate
     * @param b coefficient of the y coordinate
     * @param c drag-y coefficient
     * @param xDrag drag-x coefficient
     * @param gc frame of reference for the circumference
     */
    public AnalyticalCircumference(double a, double b, double c, double xDrag, GraphicsContext gc) {
        this.a = a;
        this.b = -b;
        this.c = gc.getCanvas().getHeight() - c;
        this.xDrag = xDrag;
    }


    /**
     * Constructs a standard circumference given a GraphicsContext as
     * an absolute frame of reference
     * @param gc frame of reference for the circumference
     */
    public AnalyticalCircumference(GraphicsContext gc) {

        // Remember that the y simulation axe is inverted

        // Define the properties
        this.a = gc.getCanvas().getWidth()/2;
        this.b = -gc.getCanvas().getWidth()/2;
        this.c = gc.getCanvas().getHeight() - gc.getCanvas().getWidth()/2 - 100;
        this.xDrag = -gc.getCanvas().getWidth()/2;

    }

    @Override
    public double computeY(double x) { return -b * Math.sqrt(-Math.pow((x + xDrag)/a, 2) + 1)  +  c; }

}

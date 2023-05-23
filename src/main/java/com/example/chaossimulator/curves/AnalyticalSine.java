package com.example.chaossimulator.curves;

import javafx.scene.canvas.GraphicsContext;


/**
 * Class extending the AnalyticalCurve interface and
 * representing a sine function with equation y = b*sin(a*(x + xDrag)) + c
 */
public class AnalyticalSine implements AnalyticalCurve{

    // y = b*sin(a*(x + xDrag)) + c
    private double a;
    private double b;
    private double c;
    private double xDrag;



    /**
     * Constructs a sine function (y = a sin(bx + xDrag) + c)
     * given its coefficients a, b, c and xDrag and a
     * GraphicsContext as an absolute frame of reference
     * @param a coefficient of the x coordinate
     * @param b coefficient of the y coordinate
     * @param c drag-y coefficient
     * @param xDrag drag-x coefficient
     * @param gc frame of reference for the sine function
     */
    public AnalyticalSine(double a, double b, double c, double xDrag, GraphicsContext gc) {

        // Remember that the y simulation axe is inverted

        // Define the properties
        this.a = -a;
        this.b = b;
        this.c = gc.getCanvas().getHeight() - c;
        this.xDrag = xDrag;

    }


    /**
     * Constructs a standard sine function given a GraphicsContext
     * as an absolute frame of reference
     * @param gc frame of reference for the sine function
     */
    public AnalyticalSine(GraphicsContext gc) {

        // Define the properties
        this.a = 1.0/200;
        this.b = -250;
        this.c = gc.getCanvas().getHeight() - 300;
        this.xDrag = 3*Math.PI / (2*a) - gc.getCanvas().getWidth()/2;

    }

    @Override
    public double computeY(double x) { return b*Math.sin(a*(x + xDrag))  +  c; }

}

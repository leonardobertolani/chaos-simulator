package com.example.chaossimulator.curves;

import javafx.scene.canvas.GraphicsContext;

public class AnalyticalCircumference implements AnalyticalCurve {

    // x^2 / a^ 2  +  y^2 / b^2  =  1

    private double a;
    private double b;
    private double c;
    private double xDrag;

    public AnalyticalCircumference(double a, double b, double c, double xDrag, GraphicsContext gc) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.xDrag = xDrag;
    }


    public AnalyticalCircumference(GraphicsContext gc) {

        // Remember that the y simulation axe is inverted

        // Define the properties
        this.a = gc.getCanvas().getWidth()/2;
        this.b = -gc.getCanvas().getWidth()/2;
        this.c = gc.getCanvas().getHeight() - gc.getCanvas().getWidth()/2 - 100;
        this.xDrag = -gc.getCanvas().getWidth()/2;

    }

    @Override
    public boolean containsPoint(double x, double y) {
        return computeY(x) == y;
    }

    @Override
    public double computeY(double x) {
        //return Math.sqrt(Math.pow(b / a * (x + xDrag), 2)  +  (b*b));
        return -b * Math.sqrt(-Math.pow((x + xDrag)/a, 2) + 1)  +  c;
    }
}

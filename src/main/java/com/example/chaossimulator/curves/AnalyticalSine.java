package com.example.chaossimulator.curves;

import com.example.chaossimulator.objects.PVector;
import javafx.scene.canvas.GraphicsContext;

/*
TO DO

- Metti a posto documentazione (soprattutto costruttori)


#041b1e
#031517
 */

public class AnalyticalSine implements AnalyticalCurve{

    // y = a sin(bx + xDrag) + c
    private double a;
    private double b;
    private double c;
    private double xDrag;



    /**
     * Constructs a sine function (y = a sin(bx + xDrag) + c)
     * given its coefficients a, b, c and xDrag and a
     * GraphicsContext as an absolute frame of reference
     * @param a
     * @param b
     * @param c drag-y coefficient
     * @param xDrag drag-x coefficient
     * @param gc frame of reference for the parabola
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
     * @param gc frame of reference for the parabola
     */
    public AnalyticalSine(GraphicsContext gc) {

        // Define the properties
        this.a = -250;
        this.b = 1.0/200;
        this.c = gc.getCanvas().getHeight() - 300;
        this.xDrag = -gc.getCanvas().getWidth()/2 + 100;

    }


    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getxDrag() {
        return xDrag;
    }

    public void setxDrag(double xDrag) {
        this.xDrag = xDrag;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return computeY(x) == y;
    }

    @Override
    public double computeY(double x) {
        return a*Math.sin(b*x + xDrag)  +  c;
    }

    /*
    @Override
    public double computeX(double y, boolean mode) {
        if(mode) {
            return ( -b - Math.sqrt(Math.pow(b, 2) - 4*a*(c - y)) ) / ( 2*a )  -  xDrag;
        }

        return ( -b + Math.sqrt(Math.pow(b, 2) - 4*a*(c - y)) ) / ( 2*a )  -  xDrag;
    }

    @Override
    public boolean chooseXCoordinate(double x) {
        return true;
    }

     */
}

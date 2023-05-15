package com.example.chaossimulator.curves;

import javafx.scene.canvas.GraphicsContext;
import com.example.chaossimulator.objects.PVector;


/*

TO DO:

cancellare i setter, praticamente non li uso (anzi non li uso proprio!!)
 */

public class AnalyticalParabola implements AnalyticalCurve {

    // y = ax^2 + bx + c

    private double a;
    private double b;
    private double c;
    private double xDrag;

    private PVector vertex;



    /**
     * Constructs a parabola given its main known coefficients a, b and c and a
     * GraphicsContext as an absolute frame of reference
     * @param a coefficient of the squared term
     * @param b coefficient of the linear term
     * @param c drag-y coefficient
     * @param xDrag drag-x coefficient
     * @param gc frame of reference for the parabola
     */
    public AnalyticalParabola(double a, double b, double c, double xDrag, GraphicsContext gc) {

        // Remember that the y simulation axe is inverted

        // Define the properties
        this.a = -a;
        this.b = b;
        this.c = gc.getCanvas().getHeight() - c;
        this.xDrag = xDrag;

        // Define the vertex
        vertex = new PVector();
        this.vertex.x = ( (-b) / (2*a) )  +  ( this.xDrag );
        this.vertex.y = computeY(this.vertex.x);
    }




    /**
     * Constructs a standard parabola given a GraphicsContext as an absolute
     * frame of reference
     * @param gc frame of reference for the parabola
     */
    public AnalyticalParabola(GraphicsContext gc) {

        // Define the properties
        this.a = -1.0/500; // DEFAULT: -1.0/300
        this.b = 0;
        this.c = gc.getCanvas().getHeight() - 30;
        this.xDrag = -gc.getCanvas().getWidth()/2;

        // Define the vertex
        vertex = new PVector();
        this.vertex.x = 500;
        this.vertex.y = computeY(this.vertex.x);
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

    public PVector getVertex() {
        return vertex;
    }

    @Override
    public boolean containsPoint(double x, double y) {
        return computeY(x) == y;
    }

    @Override
    public double computeY(double x) {
        return a*Math.pow(x + xDrag, 2)  +  b*(x + xDrag)  +  c;
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
        System.out.println(vertex); return x > vertex.x;
    }


     */

}

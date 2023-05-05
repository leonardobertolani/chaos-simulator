package com.example.chaossimulator.curves;

import javafx.scene.canvas.GraphicsContext;
import com.example.chaossimulator.objects.PVector;


/*

TO DO:

cancellare i setter, praticamente non li uso (anzi non li uso proprio!!)
 */

public class AnalyticalParabola implements AnalyticalCurve {

    /**
     * Since the parabola is a non-invertible function, when trying
     * to determine the x coordinate given the y the equation return
     * two points: a right_side point and a left-side point.
     * This constant is used to decide the right-side point of the
     * result when using the computeX() function.
     */
    public static final boolean RIGHT_SIDE_X = true;


    /**
     * Since the parabola is a non-invertible function, when trying
     * to determine the x coordinate given the y the equation return
     * two points: a right_side point and a left-side point.
     * This constant is used to decide the left-side point of the
     * result when using the computeX() function.
     */
    public static final boolean LEFT_SIDE_X = false;

    private double a;
    private double b;
    private double c;
    private double xDrag;

    private PVector vertex;



    /**
     * Construct a parabola given its main known coefficients a, b and c and a
     * GraphicsContext as an absolute frame of reference
     * @param a coefficient of the squared term
     * @param b coefficient of the linear term
     * @param c drag-y coefficient
     * @param xDrag drag-x coefficient
     * @param gc frame of reference for the parabola
     */
    public AnalyticalParabola(double a, double b, double c, double xDrag, GraphicsContext gc) {

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
     * Construct a standard parabola given a GraphicsContext as an absolute
     * frame of reference
     * @param gc frame of reference for the parabola
     */
    public AnalyticalParabola(GraphicsContext gc) {

        // Define the properties
        this.a = -1.0/300;
        this.b = 0;
        this.c = gc.getCanvas().getHeight() - 30;
        this.xDrag = -400;

        // Define the vertex
        vertex = new PVector();
        this.vertex.x = 400;
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


}

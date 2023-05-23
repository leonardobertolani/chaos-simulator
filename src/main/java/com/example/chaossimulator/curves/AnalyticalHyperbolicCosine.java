package com.example.chaossimulator.curves;

import com.example.chaossimulator.objects.PVector;
import javafx.scene.canvas.GraphicsContext;

/**
 * Class extending the AnalyticalCurve interface and
 * representing an hyperbolic cosine with equation y = ( b * ( e^a(x + xDrag)  +  e^-a(x + xDrag) )  /  2 ) + c
 */
public class AnalyticalHyperbolicCosine implements AnalyticalCurve {

    // y = ( b * ( e^a(x + xDrag)  +  e^-a(x + xDrag) )  /  2 ) + c

    private double a;
    private double b;
    private double c;
    private double xDrag;

    private PVector vertex;



    /**
     * Constructs a hyperbolic cosine y = ( b * ( e^a(x + xDrag)  +  e^-a(x + xDrag) )  /  2 ) + c
     * given its coefficients a, b, c and xDrag and a
     * GraphicsContext as an absolute frame of reference
     * @param a coefficient of the x coordinate
     * @param b coefficient of the y coordinate
     * @param c drag-y coefficient
     * @param xDrag drag-x coefficient
     * @param gc frame of reference for the hyperbolic cosine
     */
    public AnalyticalHyperbolicCosine(double a, double b, double c, double xDrag, GraphicsContext gc) {

        // Remember that the y simulation axe is inverted

        // Define the properties
        this.a = a;
        this.b = -b;
        this.c = gc.getCanvas().getHeight() - c;
        this.xDrag = xDrag;

        // Define the vertex
        vertex = new PVector();
        this.vertex.x = ( (-b) / (2*a) )  +  ( this.xDrag );
        this.vertex.y = computeY(this.vertex.x);
    }


    /**
     * Constructs a standard hyperbolic cosine given a GraphicsContext
     * as an absolute frame of reference
     * @param gc frame of reference for the hyperbolic cosine
     */
    public AnalyticalHyperbolicCosine(GraphicsContext gc) {

        // Define the properties
        this.a = 1.0/270;
        this.b = -200;
        this.c = gc.getCanvas().getHeight() + 100;
        this.xDrag = -gc.getCanvas().getWidth()/2;

        // Define the vertex
        vertex = new PVector();
        this.vertex.x = 500;
        this.vertex.y = computeY(this.vertex.x);
    }

    @Override
    public double computeY(double x) { return ( b * ( Math.exp(a*(x + xDrag))  +  Math.exp(-a*(x + xDrag)) )  /  2 )  +  c; }
}

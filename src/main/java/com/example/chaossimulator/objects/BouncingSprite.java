package com.example.chaossimulator.objects;

import com.example.chaossimulator.curves.AnalyticalCurve;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


public class BouncingSprite extends Sprite {

    /**
     * Constant used to apply very small increments during the operations
     * on the coordinates.
     */
    final double dX_INCREMENT = 0.0005;


    /**
     * Constructs a new BouncingSprite object given a color, a location and a velocity.
     * By default, a BouncingSprite object is a Sprite object with circular shape, fixed radius
     * and fixed acceleration (managed by BallsSettings class).
     * @param color the color to be applied to the new BouncingSprite object
     * @param location the location for the new BouncingSprite object
     * @param velocity the velocity for the new BouncingSprite object
     */
    public BouncingSprite(Color color, PVector location, PVector velocity) {
        super(  "default",
                new Circle(BallsSettings.SPRITE_RADIUS, color),
                location,
                velocity,
                BallsSettings.ACCELERATION);
    }

    /**
     * Constructs a new BouncingSprite object from the one given as parameter.
     * @param bs the BouncingSprite object to copy
     */
    public BouncingSprite(BouncingSprite bs) {
        super(  bs.getId(),
                new Circle(((Circle)bs.getView()).getRadius(), ((Shape)bs.getView()).getFill()),
                new PVector(bs.location.x, bs.location.y),
                new PVector(bs.velocity.x, bs.velocity.y),
                BallsSettings.ACCELERATION);
    }

    /**
     * Returns the only Node associated with the BouncingSprite object
     */
    public Node getView() {
        return getChildren().get(0);
    }

    /**
     * Displays the BouncingSprite object on the screen based on its current position
     */
    public void display() {
        setTranslateX(location.x);
        setTranslateY(location.y);
    }

    /**
     * Decides whether the BouncingSprite object is colliding with an AnalyticalCurve object
     * @param curve the AnalyticalCurve object to check for
     * @return a boolean value representing the collision (true) or not (false)
     */
    public boolean isBouncing(AnalyticalCurve curve) {

        return location.y + velocity.y + BallsSettings.SPRITE_RADIUS > curve.computeY(location.x + velocity.x);
    }


    /**
     * Apply the bounce on the object given a AnalyticalCurve object to bounce on.
     * Often used with the isBouncing() function.
     * @param curve the AnalyticalCurve object on which to apply the bounce
     */
    public void applyBounce(AnalyticalCurve curve) {

        // First of all, I invert the y coordinate of the velocity just to work with the vectors better
        // (at the end I'll reinvert it)
        velocity.y *= -1;

        // Then, I determine the ortogonal base system of the curve slope

        // The first versor is given from the slope of the curve
        PVector xBaseVector = new PVector(
                2*dX_INCREMENT,
                -(curve.computeY(location.x + dX_INCREMENT) - curve.computeY(location.x - dX_INCREMENT))
        );
        xBaseVector = xBaseVector.normalize();


        // Given a vector v = (a, b), the class of the ortogonal vectors to v
        // are multiple of u = (-b, a).
        // In this case, yBaseVector represents the second vector for the ortogonal base system
        PVector yBaseVector = new PVector(-xBaseVector.y, xBaseVector.x);

        double magnitude = velocity.magnitude();
        double xBaseVelocityAngle = Math.acos(velocity.dot(xBaseVector) / magnitude);

        // Getting the x and y components of the vector respecting of the (xBaseVector, yBaseVector) base system
        velocity.x = magnitude*Math.cos(xBaseVelocityAngle);
        velocity.y = magnitude*Math.sin(2*Math.PI - xBaseVelocityAngle);

        // Applying the bounce by inverting his y component
        velocity.y *= -1;

        // Resetting the vectors using its altered x and y components
        velocity = PVector.add(PVector.multiply(xBaseVector, velocity.x), PVector.multiply(yBaseVector, velocity.y));

        // Reconverting the y coordinate
        velocity.y *= -1;
        velocity = velocity.add(PVector.multiply(acceleration, -1));

    }



}

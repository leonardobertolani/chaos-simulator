package com.example.chaossimulator.objects;

import com.example.chaossimulator.curves.AnalyticalCurve;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/*

TO DO

- aggiusta o cancella adjustCollision
 */

public class BouncingSprite extends Sprite {

    /**
     * Constant used to apply very small increments during the operations
     * on the coordinates.
     */
    final double dX_INCREMENT = 0.05;

    public BouncingSprite(Node view) {
        super(view);
    }

    public BouncingSprite(Node view, PVector location) {
        super(view, location);
    }

    public BouncingSprite(Node view, PVector location, PVector velocity) {
        super(view, location, velocity);
    }

    public BouncingSprite(Node view, PVector location, PVector velocity, PVector acceleration) {
        super(view, location, velocity, acceleration);
    }

    public BouncingSprite(Node view, PVector location, PVector velocity, PVector acceleration, double mass) {
        super(view, location, velocity, acceleration, mass);
    }

    public boolean isBouncing(AnalyticalCurve curve, AnchorPane pane) {

        if (location.y + velocity.y + BallsSettings.SPRITE_RADIUS > curve.computeY(location.x + velocity.x)) {
            // is bouncing

            // now I adjust the position of the ball so that it shows really close to the curve
            //adjustCollision(curve);
            return true;
        }

        return false;
    }


    private void adjustCollision(AnalyticalCurve curve) {

        if (velocity.x != 0) {
            double xIncrement = Math.signum(velocity.x)* velocity.x / 100;
            location.y = -(-velocity.y / velocity.x) * (location.x + xIncrement) - (-location.x * (-velocity.y) - location.y * velocity.x) / velocity.x;
            location.x += xIncrement;

            while (location.y < (curve.computeY(location.x) - BallsSettings.SPRITE_RADIUS)) {

                location.y = -(-velocity.y / velocity.x) * (location.x + xIncrement) - (-location.x * (-velocity.y) - location.y * velocity.x) / velocity.x;
                location.x += xIncrement;
            }


        } else {

            double yIncrement = Math.signum(velocity.y)*velocity.y / 100;
            location.y += yIncrement;

            while (location.y < (curve.computeY(location.x) - BallsSettings.SPRITE_RADIUS)) {

                location.y += yIncrement;
            }


        }
    }



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

package com.example.chaossimulator.objects;

import com.example.chaossimulator.curves.AnalyticalCurve;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sprite extends Region {
    PVector location;
    PVector velocity;
    PVector acceleration;
    double mass = BallsSettings.SPRITE_DEFAULT_MASS;
    Node view;

    public Sprite(Node view) {
        this.view = view;
        this.location = new PVector(0, 0);
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location) {
        this.view = view;
        this.location = location;
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location, PVector velocity) {
        this.view = view;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = new PVector(0, 0);
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location, PVector velocity, PVector acceleration) {
        this.view = view;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        getChildren().add(view);
    }

    public Sprite(Node view, PVector location, PVector velocity, PVector acceleration, double mass) {
        this.view = view;
        this.location = location;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
        getChildren().add(view);
    }

    public PVector getLocation() {
        return location;
    }

    public void setLocation(PVector location) {
        this.location = location;
    }
    public void setXLocation(double x) { this.location.x = x; }
    public void setYLocation(double y) { this.location.y = y; }
    public void setZLocation(double z) { this.location.z = z; }


    public PVector getVelocity() {
        return velocity;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }
    public void setXVelocity(double x) { this.velocity.x = x; }
    public void setYVelocity(double y) { this.velocity.y = y; }
    public void setZVelocity(double z) { this.velocity.z = z; }

    public PVector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }
    public void setXAcceleration(double x) { this.acceleration.x = x; }
    public void setYAcceleration(double y) { this.acceleration.y = y; }
    public void setZAcceleration(double z) { this.acceleration.z = z; }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Node getView() {
        return view;
    }

    public void setView(Node view) {
        this.view = view;
    }

    public void applyImpulseForce(PVector force) {
        velocity = velocity.add(force.multiply(1 / mass));
    }

    public void update() {
        velocity = velocity.add(acceleration);
        velocity = velocity.limit(BallsSettings.SPRITE_MAX_SPEED);
        location = location.add(velocity);
    }

    public boolean intersects(Sprite other) {
        return getBoundsInParent().intersects(other.getBoundsInParent());
    }

    public boolean contains(Point2D point) {
        return getBoundsInParent().contains(point);
    }

    public void display() {
        setTranslateX(location.x);
        setTranslateY(location.y);
    }

    public boolean isBouncing(AnalyticalCurve curve, AnchorPane pane) {

        if(location.y + velocity.y > curve.computeY(location.x + velocity.x)) {
            // is bouncing

            // now I adjust the position of the ball so that it shows really close to the curve
            if(velocity.x != 0) {
                double xIncrement = velocity.x/100;
                location.y = -( -velocity.y/velocity.x )*(location.x + xIncrement)  -  (-location.x*(-velocity.y) - location.y* velocity.x)/velocity.x;
                location.x += xIncrement;

                while(location.y < (curve.computeY(location.x) - BallsSettings.SPRITE_RADIUS) ) {

                    location.y = -( -velocity.y/velocity.x )*(location.x + xIncrement)  -  (-location.x*(-velocity.y) - location.y* velocity.x)/velocity.x;
                    location.x += xIncrement;
                /*
                PVector p = new PVector(0, 0);
                Sprite scia = new Sprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.RED), new PVector(location.x, location.y), p, p);
                pane.getChildren().add(scia);
                scia.display();

                 */
                }


            } else {

                double yIncrement = velocity.y/100;
                location.y += yIncrement;

                while(location.y < (curve.computeY(location.x) - BallsSettings.SPRITE_RADIUS) ) {

                    location.y += yIncrement;
                /*
                PVector p = new PVector(0, 0);
                Sprite scia = new Sprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.RED), new PVector(location.x, location.y), p, p);
                pane.getChildren().add(scia);
                scia.display();

                 */
                }


            }



            return true;


        }

        return false;


        /*
        TENTATIVO 2


        System.out.println(location.y);
        System.out.println(curve.computeY(location.x));

        if(location.y - curve.computeY(location.x) > BallsSettings.SPRITE_RADIUS) {
            // is bouncing
            return true;
        }

        return false;

         */



        /*
        TENTATIVO 1

        // Set A point at the same y of the Sprite but on the curve
        PVector A = new PVector(
                curve.computeX(location.y, curve.chooseXCoordinate(location.x)),
                location.y);



        System.out.println(location.x);
        System.out.println(curve.chooseXCoordinate(location.x));
        Sprite Apoint = new Sprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.RED), A);
        pane.getChildren().add(Apoint);
        Apoint.display();

        // Set B point at the same x of the Sprite but on the curve
        PVector B = new PVector(location.x, curve.computeY(location.x));

        Sprite Bpoint = new Sprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.RED), B);
        pane.getChildren().add(Bpoint);
        Bpoint.display();

        // For a line of equation ax + by + c = 0 and two points, A and B, we have that
        // v = ( -b, a ) = (  ( A.x - B.x ) , ( A.y - B.y ) )
        // so a = ( A.y - B.y ) and b = -( A.x - B.x )

        // At this point we know that, given a point P = (Xp, Yp),
        // | aXp + bYp + c | = | ( -( A.y - B.y ), ( A.x - B.x ) ) * ( xp - A.x, yp - A.y )
        // that is the numerator of the distance line-point formula.
        PVector directorCoefficient = new PVector( ( A.x - B.x ) , ( A.y - B.y ) );
        PVector lineCoefficient = new PVector( -( A.y - B.y ), ( A.x - B.x ) );

        double numerator = Math.abs(PVector.dot(
                        lineCoefficient,
                        new PVector(location.x - A.x, location.y - B.y)));

        // The denominator is simply the magnitude of the line coefficient
        double denominator = directorCoefficient.magnitude();

        // distance line-point = | aXp + bYp + c | / sqrt(directorCoefficient.magnitude)
        double distance = numerator / denominator;

        return distance <= BallsSettings.SPRITE_RADIUS;

         */
    }

    @Override
    public String toString() {
        return "Sprite{" +
                "location=" + location +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", mass=" + mass +
                ", view=" + view +
                '}';
    }
}

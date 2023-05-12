package com.example.chaossimulator;

import com.example.chaossimulator.curves.*;
import com.example.chaossimulator.objects.BallsSettings;
import com.example.chaossimulator.objects.BouncingSprite;
import com.example.chaossimulator.objects.PVector;
import com.example.chaossimulator.objects.Sprite;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/*

TO DO

- nella finestra di generazione di una pallina aggiungi il tasto annulla e apply
- scrivi la funzione per la generazione sfasata e usala anche per add series
 */

public class SimulatorWindowController {
    @FXML private AnchorPane simulationPane;
    @FXML private Canvas curveCanvas;
    @FXML private ChoiceBox<String> chCurve;
    @FXML private Button simulationButton;




    List<BouncingSprite> physicalObjects;
    AnimationTimer timer;
    boolean isSimulating;
    AnalyticalCurve curve;
    public static final String[] curveTypes = {
            "com.example.chaossimulator.curves.AnalyticalParabola",
            "com.example.chaossimulator.curves.AnalyticalHyperbole",
            "com.example.chaossimulator.curves.AnalyticalEllipse",
            "com.example.chaossimulator.curves.AnalyticalSine",
            "com.example.chaossimulator.curves.AnalyticalHyperbolicCosine",
            "com.example.chaossimulator.curves.AnalyticalCircumference"
    };
    public static final Color[] OBJECT_COLORS = { Color.RED, Color.ORANGE, Color.YELLOW, Color.YELLOWGREEN, Color.GREEN, Color.CYAN, Color.BLUE, Color.BLUEVIOLET, Color.PURPLE, Color.BROWN };


    /**
     * First method of the simulation program. It is used to
     * set up all the objects.
     */
    void onStart() {
        this.isSimulating = false;
        simulationButton.setText("Simulate");

        initializeTimer();
        initializeSimulation();
    }


    /**
     * Method for create a new timer and linking it
     * to the mainLoop() function.
     */
    void initializeTimer() {

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                mainLoop();
            }
        };
    }

    /**
     * Method specifically written to set up all the stuff
     * regarding the simulation. It's been called in the onStart() method.
     */
    @FXML
    public void initializeSimulation() {

        double h = simulationPane.getHeight();
        double w = simulationPane.getWidth();

        physicalObjects = new ArrayList<>();

        GraphicsContext gc = curveCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);


        //physicalObjects.add(new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.WHITE), new PVector(300, 50), new PVector(0, 2), new PVector(0, 0.1)));

        /*
        physicalObjects.add(new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.BLUE), new PVector(w, 0), new PVector(0, 0), new PVector(0, 0.1)));
        physicalObjects.add(new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.RED), new PVector(w + 0.005, 0), new PVector(0, 0), new PVector(0, 0.1)));
        physicalObjects.add(new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.RED), new PVector(w + 0.01, 0), new PVector(0, 0), new PVector(0, 0.1)));
        physicalObjects.add(new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.RED), new PVector(w + 0.015, 0), new PVector(0, 0), new PVector(0, 0.1)));
        physicalObjects.add(new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.YELLOW), new PVector(w + 0.02, 0), new PVector(0, 0), new PVector(0, 0.1)));
        physicalObjects.add(new BouncingSprite(new Circle(BallsSettings.SPRITE_RADIUS, Color.GREEN), new PVector(w + 0.025, 0), new PVector(0, 0), new PVector(0, 0.1)));


         */

        for(double n = 1; n <= BallsSettings.SPRITE_COUNT; ++n) {
            physicalObjects.add(new BouncingSprite(
                    new Circle(BallsSettings.SPRITE_RADIUS, OBJECT_COLORS[(int)((n-1)/(BallsSettings.SPRITE_COUNT/10.0))]),
                    new PVector(700 + n/1000, 200),
                    new PVector(0, 0),
                    new PVector(0, 0.3)));
        }

        simulationPane.getChildren().addAll(physicalObjects);
        physicalObjects.forEach(Sprite::display);


        chCurve.getItems().addAll(curveTypes);
        chCurve.setOnAction((event) -> {
            try {
                // Try generating a new AnalyticalCurve (the class may not exist)
                curve = (AnalyticalCurve) Class.forName(chCurve.getValue()).getDeclaredConstructor(GraphicsContext.class).newInstance(gc);
            } catch (Exception e) {
                e.printStackTrace();
            }

            restartSimulation();
            gc.clearRect(0, 0, curveCanvas.getWidth(), curveCanvas.getHeight()); // Clearing the canvas
            AnalyticalCurve.drawCurve(curve, gc); // Drawing the new curve
        });
        chCurve.getSelectionModel().select(0);

    }


    private void mainLoop() {
        // Do simulation stuff
        //for(Iterator<BouncingSprite> iterator = physicalObjects.iterator(); iterator.hasNext(); ) {

        physicalObjects.forEach(s -> {
            s.update();
            s.display();
            checkBallBounds(s, curve);
            //System.out.println("energia totale: " + (s.getVelocity().y*s.getVelocity().y + simulationPane.getHeight() - s.getLocation().y));
        });


    }

    private void checkBallBounds(BouncingSprite s, AnalyticalCurve curve) {
        double radius = ((Circle) (s.getView())).getRadius();
        // upper horizontal wall collision
        if (s.getLocation().y < radius) {
            s.getLocation().y = radius;
            s.getVelocity().y *= -1;
        }
        // lower horizontal wall collision
        if (s.getLocation().y + radius > simulationPane.getHeight()) {
            s.getLocation().y = simulationPane.getHeight() - radius;
            s.getVelocity().y *= -1;
        }
        // left vertical wall collision
        if (s.getLocation().x < radius) {
            s.getLocation().x = radius;
            s.getVelocity().x *= -1;
        }
        // right vertical wall collision
        if (s.getLocation().x + radius > simulationPane.getWidth()) {
            s.getLocation().x = simulationPane.getWidth() - radius;
            s.getVelocity().x *= -1;
        }
        // analytical curve collision
        if(s.isBouncing(curve)) {
            s.applyBounce(curve);
        }

    }

    void stopSimulation() {
        this.isSimulating = false;
        simulationButton.setText("Simulate");
        timer.stop();
    }

    void resumeSimulation() {
        this.isSimulating = true;
        simulationButton.setText("Stop");
        timer.start();
    }

    @FXML
    void restartSimulation() {
        stopSimulation();
        physicalObjects.forEach(sprite -> {
            sprite.getLocation().set(new PVector(curveCanvas.getWidth()/2 + 100, 150));
            sprite.getVelocity().set(new PVector(0, 0));
            sprite.display();
        });

    }

    @FXML
    void onSimulate() {

        if (this.isSimulating) {
            stopSimulation();
        } else {
            resumeSimulation();
        }

    }


    @FXML
    public void onAddNewObject() {

        try {
            stopSimulation();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add-object-view.fxml"));
            DialogPane view = loader.load();
            AddObjectController controller = loader.getController();

            // Set an empty person into the controller
            controller.initialize(curveCanvas);

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Object");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            // Add new object to the Lists
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                BouncingSprite.generateDefaultPhysicalObject(simulationPane, physicalObjects, controller.getNewObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
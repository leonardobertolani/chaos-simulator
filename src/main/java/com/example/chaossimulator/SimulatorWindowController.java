package com.example.chaossimulator;

import com.example.chaossimulator.curves.AnalyticalCurve;
import com.example.chaossimulator.curves.AnalyticalParabola;
import com.example.chaossimulator.objects.BallsSettings;
import com.example.chaossimulator.objects.BouncingSprite;
import com.example.chaossimulator.objects.PVector;
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

- quando apri la finestra per creare una nuova pallina, ferma tutto e ricomincia al premere di ok o annulla
 */

public class SimulatorWindowController {
    @FXML private AnchorPane simulationPane;
    @FXML private Button simulationButton;
    @FXML private TextField velocityInputText;
    @FXML private Slider velocitySlider;
    @FXML private Canvas curveCanvas;


    /**
     * A List of BouncingSprite objects used in the simulation
     */
    List<BouncingSprite> physicalObjects;
    AnimationTimer timer;
    boolean isSimulating;
    AnalyticalCurve curve;

    @FXML
    public void initializeSimulation() {

        // Initialization
        double h = simulationPane.getWidth();
        double w = simulationPane.getHeight();
        isSimulating = false;
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

        Color COLORS[] = {Color.RED, Color.ORANGE, Color.YELLOW, Color.YELLOWGREEN, Color.GREEN, Color.CYAN, Color.BLUE, Color.BLUEVIOLET, Color.PURPLE, Color.BROWN};
        for(double n = 1; n <= BallsSettings.SPRITE_COUNT; ++n) {
            physicalObjects.add(new BouncingSprite(
                    new Circle(BallsSettings.SPRITE_RADIUS, COLORS[(int)((n-1)/(BallsSettings.SPRITE_COUNT/10.0))]),
                    new PVector(500 + n/1000, 200),
                    new PVector(0, 0),
                    new PVector(0, 0.07)));
        }

        simulationPane.getChildren().addAll(physicalObjects);
        physicalObjects.forEach(s -> s.display());



        curve = new AnalyticalParabola(gc);
        AnalyticalCurve.drawCurve(curve, gc);

    }

    void initializeTimer() {

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                mainLoop();
            }
        };
        timer.start();
    }

    private void mainLoop() {
        if(isSimulating) {
            // Do simulation stuff
            //for(Iterator<BouncingSprite> iterator = physicalObjects.iterator(); iterator.hasNext(); ) {
            physicalObjects.forEach(s -> {
                s.update();
                s.display();
                checkBallBounds(s, curve);
                //System.out.println("energia totale: " + (s.getVelocity().y*s.getVelocity().y + simulationPane.getHeight() - s.getLocation().y));
            });
        }
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
        if(s.isBouncing(curve, simulationPane)) {
            s.applyBounce(curve);
        }

    }

    @FXML
    void onSimulate() {
        this.isSimulating = !this.isSimulating;

        if(this.isSimulating) {
            simulationButton.setText("Stop");
        } else {
            simulationButton.setText("Simulate");
        }
    }


    @FXML
    public void onAddNewObject() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("add-object-view.fxml"));
            DialogPane view = loader.load();
            AddObjectController controller = loader.getController();

            // Set an empty person into the controller
            controller.setPhysicalObject();

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Object");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();

            // Add new object to the Lists
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                physicalObjects.add(controller.getNewObject());
                simulationPane.getChildren().add(controller.getNewObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
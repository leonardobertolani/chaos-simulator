package com.example.chaossimulator;

import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimulatorWindowController {
    @FXML private AnchorPane simulationPane;
    @FXML private Button simulationButton;
    @FXML private TextField velocityInputText;
    @FXML private Slider velocitySlider;


    /**
     * A List of Sprite objects used in the simulation
     */
    List<Sprite> physicalObjects;

    /**
     * A List of Sprite objects containing the startup configurations.
     * This List is used to store initial information about the simulation.
     */
    List<Sprite> initialPhysicalObjects;
    AnimationTimer timer;
    boolean isSimulating;

    @FXML
    public void initializeSimulation() {
        // Initialize the person table with the two columns.
        double h = simulationPane.getWidth();
        double w = simulationPane.getHeight();
        isSimulating = false;

        initialPhysicalObjects = new ArrayList<>();
        physicalObjects = new ArrayList<>();
        physicalObjects.add(new Sprite(new Circle(10, Color.WHITE), new PVector(w / 4, h / 2), new PVector(4, 0), new PVector(0, 1)));
        physicalObjects.add(new Sprite(new Circle(10, Color.BLUE), new PVector(w / 2, h / 3), new PVector(0, 5), new PVector(0, 1)));
        physicalObjects.add(new Sprite(new Circle(10, Color.RED), new PVector(3 * w / 4, 4 * h / 7), new PVector(3, 2), new PVector(0, 1)));

        simulationPane.getChildren().addAll(physicalObjects);


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
            for(Sprite s : physicalObjects) {
                s.update();
                checkBallBounds(s);
                s.display();
            }
        }
    }

    private void checkBallBounds(Sprite s) {
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
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                initialPhysicalObjects.add(controller.getNewObject());
                physicalObjects.add(controller.getNewObject());
                System.out.println(physicalObjects);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
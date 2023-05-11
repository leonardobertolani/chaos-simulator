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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/*

TO DO

- quando apri la finestra per creare una nuova pallina, ferma tutto e ricomincia al premere di ok o annulla
 */

public class SimulatorWindowController {
    @FXML private AnchorPane simulationPane;
    @FXML private Canvas curveCanvas;
    @FXML private ChoiceBox<String> chCurve;

    public static final String[] curveTypes = {
            "com.example.chaossimulator.curves.AnalyticalParabola",
            "com.example.chaossimulator.curves.AnalyticalHyperbole",
            "com.example.chaossimulator.curves.AnalyticalEllipse",
            "com.example.chaossimulator.curves.AnalyticalSine",
            "com.example.chaossimulator.curves.AnalyticalHyperbolicCosine",
            "com.example.chaossimulator.curves.AnalyticalCircumference"
    };
    @FXML private Button simulationButton;



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
        double h = simulationPane.getHeight();
        double w = simulationPane.getWidth();
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
                    new PVector(700 + n/1000, 200),
                    new PVector(0, 0),
                    new PVector(0, 0.5)));
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

            //restartSimulation();
            gc.clearRect(0, 0, curveCanvas.getWidth(), curveCanvas.getHeight()); // Clearing the canvas
            AnalyticalCurve.drawCurve(curve, gc); // Drawing the new curve
        });
        chCurve.getSelectionModel().select(0);

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
        if(s.isBouncing(curve, simulationPane)) {
            s.applyBounce(curve);
        }

    }

    void stopSimulation() {
        simulationButton.setText("Stop");
        timer.stop();
    }

    void resumeSimulation() {
        simulationButton.setText("Simulate");
        timer.start();
    }

    void restartSimulation() {
        physicalObjects.forEach(sprite -> {
            sprite.getLocation().set(new PVector(curveCanvas.getWidth()/2 + 100, 150));
            sprite.getVelocity().set(new PVector(0, 0));
        });
        simulationButton.setText("Simulate");
        timer.start();
    }

    @FXML
    void onSimulate() {
        this.isSimulating = !this.isSimulating;

        if(this.isSimulating) {
            stopSimulation();
        } else {
            resumeSimulation();
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
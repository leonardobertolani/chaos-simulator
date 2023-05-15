package com.example.chaossimulator;

import com.example.chaossimulator.objects.BouncingSprite;
import com.example.chaossimulator.objects.PVector;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class AddSeriesController {

    @FXML private TextField ObjectDistanceField;
    @FXML private TextField ObjectNumberField;
    @FXML private TextField xPositionField;
    @FXML private TextField xVelocityField;
    @FXML private TextField yPositionField;
    @FXML private TextField yVelocityField;

    double xLocation;
    double yLocation;
    double xVelocity;
    double yVelocity;
    int objectNumber;
    double objectDistance;


    public void initialize(Canvas canvas) {

        xPositionField.textProperty().addListener(((observable, oldValue, newValue) -> xLocation = Double.parseDouble(xPositionField.getText())));
        xPositionField.setText("600");

        yPositionField.textProperty().addListener(((observable, oldValue, newValue) -> yLocation = canvas.getHeight() - Double.parseDouble(yPositionField.getText())));
        yPositionField.setText("500");

        xVelocityField.textProperty().addListener((observable, oldValue, newValue) -> xVelocity = Double.parseDouble(xVelocityField.getText()));
        xVelocityField.setText("0");

        yVelocityField.textProperty().addListener(((observable, oldValue, newValue) -> yVelocity = Double.parseDouble(yVelocityField.getText())));
        yVelocityField.setText("0");

        ObjectNumberField.textProperty().addListener((observable, oldValue, newValue) -> objectNumber = Integer.parseInt(ObjectNumberField.getText()));
        ObjectNumberField.setText("100");

        ObjectDistanceField.textProperty().addListener((observable, oldValue, newValue) -> objectDistance = Double.parseDouble(ObjectDistanceField.getText()));
        ObjectDistanceField.setText("0.00001");

    }

    public List<BouncingSprite> getNewSeries() {
        List<BouncingSprite> list = new ArrayList<>();

        for(int i = 0; i < objectNumber; ++i) {
            list.add(new BouncingSprite(Color.hsb((360.0/objectNumber)*i, 1.0, 1.0), new PVector(xLocation + i*objectDistance, yLocation), new PVector(xVelocity, yVelocity)));
        }

        return list;
    }
}

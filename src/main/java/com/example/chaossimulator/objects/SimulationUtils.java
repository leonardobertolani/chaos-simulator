package com.example.chaossimulator.objects;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SimulationUtils {

    public static List<Color> colors;

    public static void initializeColorsArray() {
        colors = new ArrayList<Color>();

        for (int r=0; r<100; r++) colors.add(Color.color(r*255/100,       255,         0));
        for (int g=100; g>0; g--) colors.add(Color.color(      255, g*255/100,         0));
        for (int b=0; b<100; b++) colors.add(Color.color(      255,         0, b*255/100));
        for (int r=100; r>0; r--) colors.add(Color.color(r*255/100,         0,       255));
        for (int g=0; g<100; g++) colors.add(Color.color(        0, g*255/100,       255));
        for (int b=100; b>0; b--) colors.add(Color.color(        0,       255, b*255/100));
        colors.add(Color.color(        0,       255,         0));
    }




}

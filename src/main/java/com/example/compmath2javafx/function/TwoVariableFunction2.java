package com.example.compmath2javafx.function;

import java.util.ArrayList;
import java.util.List;

public class TwoVariableFunction2 extends TwoVariableFunction {

    @Override
    public double compute(double x, double y) {
        return x + Math.cos(y - 1) - 0.7;
    }

    @Override
    public List<List<Double>> computePoints() {
        List<Double> xPoints = new ArrayList<>();
        List<Double> yPoints = new ArrayList<>();

        for (double y = -5; y <= 5; y += 0.1) {
            xPoints.add(0.7 - Math.cos(y - 1));
            yPoints.add(y);
        }

        return List.of(xPoints, yPoints);
    }

    @Override
    public double computeDerivativeX(double x, double y) {
        return 1;
    }

    @Override
    public double computeDerivativeY(double x, double y) {
        return -Math.sin(y - 1);
    }

    @Override
    public String toString() {
        return "x + cos(y - 1) = 0.7";
    }
}


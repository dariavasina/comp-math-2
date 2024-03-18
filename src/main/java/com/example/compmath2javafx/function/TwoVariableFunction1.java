package com.example.compmath2javafx.function;


import java.util.ArrayList;
import java.util.List;

// function sinx + 2y = 2
public class TwoVariableFunction1 extends TwoVariableFunction {

    @Override
    public double compute(double x, double y) {
        return Math.sin(x) + 2 * y - 2;
    }

    @Override
    public List<List<Double>> computePoints() {
        List<Double> xPoints = new ArrayList<>();
        List<Double> yPoints = new ArrayList<>();

        for (double x = -5; x <= 5; x += 0.1) {
            xPoints.add(x);
            yPoints.add((2 - Math.sin(x)) / 2);
        }

        return List.of(xPoints, yPoints);
    }

    @Override
    public double computeDerivativeX(double x, double y) {
        return Math.cos(x);
    }

    @Override
    public double computeDerivativeY(double x, double y) {
        return 2;
    }

    @Override
    public String toString() {
        return "sinx + 2y = 2";
    }
}
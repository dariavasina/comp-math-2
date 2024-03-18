package com.example.compmath2javafx.function;

import java.util.List;

public abstract class TwoVariableFunction {

    public abstract double compute(double x, double y);

    public abstract List<List<Double>> computePoints();

    public abstract double computeDerivativeX(double x, double y);

    public abstract double computeDerivativeY(double x, double y);

}
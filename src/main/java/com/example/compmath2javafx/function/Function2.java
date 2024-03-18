package com.example.compmath2javafx.function;

public class Function2 extends Function{
    public Function2() {
        super(new Solution[] {
                new Solution(-0.1532d, 0, 0.5d, false, true),
                new Solution(1.936d, 1, 0, true, false)
        });
    }

    @Override
    public double compute(double x) {
        return Math.sin(Math.pow(x, 3) - 7) + 2 * x * x - 4 * x;
    }

    @Override
    public double computeDerivative(double x) {
        return 3 * Math.pow(x, 2) * Math.cos(Math.pow(x, 3) - 7) + 4 * x - 4;
    }

    @Override
    public String toString() {
        return "sin(x^3 - 7) + 2x^2 - 4x";
    }
}

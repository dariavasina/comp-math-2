package com.example.compmath2javafx.function;

public class Function3 extends Function{
    public Function3() {
        super(new Solution[] {
                new Solution(-1.481d, 0, -1d, false, true),
                new Solution(-0.25d, -0.7, 0, true, true),
                new Solution(0.25d, 0, 0, true, false)
        });
    }

    @Override
    public double compute(double x) {
        return Math.exp(-Math.pow(x, 5) / 2) - 16 * x * x;
    }

    @Override
    public double computeDerivative(double x) {
        return -2.5 * Math.exp(- Math.pow(x, 5) / 2) * Math.pow(x, 4) - 32 * x;
    }

    @Override
    public String toString() {
        return "e^(-x^5 / 2) - 16x^2";
    }
}

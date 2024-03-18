package com.example.compmath2javafx.function;

public class Function1 extends Function{

    public Function1() {
        super(new Solution[]{
                new Solution(-4.448, 0, -3d, false, true),
                new Solution(0.127d, -1d, 2d, true, true),
                new Solution(2.616d, 1.5d, 0, true, false)
        });
    }

    public double compute(double x) {
        return 2 * Math.pow(x, 3) + 3.41 * Math.pow(x, 2) - 23.64 * x + 2.95;
    }

    public double computeDerivative(double x) {
        return 6 * Math.pow(x, 2) + 6.82 * x - 23.74;
    }

    @Override
    public String toString() {
        return "2x^3 + 3.41x^2 - 23.64x + 2.95 = 0";
    }
}

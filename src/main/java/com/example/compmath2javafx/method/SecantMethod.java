package com.example.compmath2javafx.method;

import com.example.compmath2javafx.function.Function;
import java.util.ArrayList;
import java.util.List;

public class SecantMethod implements Method {

    public SecantMethod() {
    }

    public Result compute(Function function, double left, double right, double accuracy, int digitsAfterComma) {
        double x0 = left;
        double x1 = right;
        double fx0 = function.compute(x0);
        double fx1 = function.compute(x1);

        List<String> headers = List.of("№ шага", "x0", "x1", "x", "f(x)", "|x - x0|");
        List<List<String>> data = new ArrayList<>();

        List<String> row;
        int counter = 0;
        while (true) {
            row = new ArrayList<>();
            counter++;
            double x = computeX(x0, fx0, x1, fx1);
            double fx = function.compute(x);

            row.add(String.format("%d", counter));
            row.add(String.format("%." + digitsAfterComma + "f", x0));
            row.add(String.format("%." + digitsAfterComma + "f", x1));
            row.add(String.format("%." + digitsAfterComma + "f", x));
            row.add(String.format("%." + digitsAfterComma + "f", fx));
            row.add(String.format("%." + digitsAfterComma + "f", Math.abs(x - x0)));
            data.add(row);

            if (Math.abs(fx) <= accuracy)
                break;

            x0 = x1;
            fx0 = fx1;
            x1 = x;
            fx1 = fx;
        }

        return new Result(headers, data);
    }

    private double computeX(double x0, double fx0, double x1, double fx1) {
        return x1 - (fx1 * (x1 - x0)) / (fx1 - fx0);
    }

    @Override
    public String toString() {
        return "Метод секущих";
    }
}


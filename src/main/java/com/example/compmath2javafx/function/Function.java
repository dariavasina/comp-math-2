package com.example.compmath2javafx.function;

import com.example.compmath2javafx.exception.InputException;

public abstract class Function {
    private final Solution[] solutions;

    public Function(Solution[] solutions) {
        this.solutions = solutions;
    }

    public abstract double compute(double x);

    public abstract double computeDerivative(double x);

    public boolean checkIsolationInterval(double left, double right) throws InputException {

        if (left >= right) {
            throw new InputException("Левая граница должна стоять до правой границы и не равняться ей!");
        }

        for (Solution solution : solutions) {
            if (solution.isHasLeft() && solution.isHasRight()) {
                if (solution.getLeft() <= left && left < solution.getValue() && solution.getRight() > right && right >= solution.getValue() && computeDerivative(left) * computeDerivative(right) > 0)
                    return true;
            }
            if (solution.isHasLeft() && solution.getLeft() <= left && left < solution.getValue() && right > solution.getValue())
                return true;
            if (solution.isHasRight() && solution.getRight() >= right && right > solution.getValue() && left < solution.getValue())
                return true;
            if (!solution.isHasLeft() && !solution.isHasRight() && left <= solution.getValue() && right >= solution.getValue())
                return true;
        }

        throw new InputException("""
                Интервал изоляции корня введен некорректно.
                Проверьте, что:
                1) Функция на концах введенного интервала принимает значения разного знака
                2) Производная функции одного знака на всем введенном интервале""");
    }

    public double computeEquivalent(double x, double left, double right) {
        double lambda;

        double dLeft = computeDerivative(left);
        double dRight = computeDerivative(right);

        if (Math.abs(dLeft) > Math.abs(dRight))
            lambda = -1 / Math.abs(dLeft);
        else
            lambda = -1 / Math.abs(dRight);

        return x + lambda * compute(x);
    }

}

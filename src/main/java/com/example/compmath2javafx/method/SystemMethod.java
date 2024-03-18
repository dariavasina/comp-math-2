package com.example.compmath2javafx.method;

import com.example.compmath2javafx.function.TwoVariableFunction;
import com.example.compmath2javafx.system.SystemOfEquations;

public interface SystemMethod {

    Result compute(SystemOfEquations system, double x0, double y0, double accuracy, int digitsAfterComma);

}
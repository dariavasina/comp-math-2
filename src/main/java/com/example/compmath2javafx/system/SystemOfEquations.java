package com.example.compmath2javafx.system;

import com.example.compmath2javafx.function.TwoVariableFunction;

public class SystemOfEquations {

    private final TwoVariableFunction[] functions;

    public SystemOfEquations(TwoVariableFunction[] functions) {
        this.functions = functions;
    }

    public TwoVariableFunction[] getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        return "  _\n | " + functions[0].toString() + "\n-|\n | " + functions[1].toString() + "\n  ‾";
    }
}
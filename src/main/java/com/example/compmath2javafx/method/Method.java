package com.example.compmath2javafx.method;


import com.example.compmath2javafx.function.Function;

public interface Method {

    Result compute(Function function, double left, double right, double accuracy, int digitsAfterComma);

}
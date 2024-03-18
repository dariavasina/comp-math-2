package com.example.compmath2javafx;

import com.example.compmath2javafx.exception.InputException;
import com.example.compmath2javafx.function.*;
import com.example.compmath2javafx.method.*;
import com.example.compmath2javafx.system.SystemOfEquations;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {

    private final Map<String, Method> methodMap = new HashMap<>();
    private final Map<String, Function> functionMap = new HashMap<>();
    private final Map<String, SystemOfEquations> systemMap = new HashMap<>();
    private final Map<String, SystemMethod> systemMethodMap = new HashMap<>();

    private static final SystemOfEquations[] systemArray = new SystemOfEquations[] {
            new SystemOfEquations(
                    new TwoVariableFunction[]{
                            new TwoVariableFunction1(),
                            new TwoVariableFunction2()
                    }
            ),
//            new SystemOfNonlinearEquations(
//                    new Function[]{
//                            new Function3(),
//                            new Function4()
//                    }
//            )
    };

    public void initialize() {
        methodMap.put("Chord Method", new ChordMethod());
        methodMap.put("Secant Method", new SecantMethod());
        methodMap.put("Simple Iteration Method", new SimpleIterationMethod());
        MethodComboBox.getItems().addAll(methodMap.keySet());

        functionMap.put("Function 1", new Function1());
        functionMap.put("Function 2", new Function2());
        functionMap.put("Function 3", new Function3());

        systemMap.put("System 1", systemArray[0]);
        //systemMap.put("System 2", new System2());

        systemMethodMap.put("Newton's Method", new NewtonsMethod());
        SystemMethodCombobox.getItems().addAll(systemMethodMap.keySet());

    }

    @FXML
    private LineChart<Double, Double> chart;


    @FXML
    private TextField AccuracyField;

    @FXML
    private TextField LeftBoundaryField;


    @FXML
    private ComboBox<String> MethodComboBox;


    @FXML
    private TextField RightBoundaryField;

    @FXML
    private Button SolveButton;

    @FXML
    private ToggleGroup functions;

    @FXML
    private TableView<ResultRow> ResultTable;

    @FXML
    private Pane FunctionPane;

    @FXML
    private Label ErrorLabel;

    @FXML
    private TextField SystemAccuracyField;

    @FXML
    private TextField XField;

    @FXML
    private TextField YField;

    @FXML
    private ComboBox<String> SystemMethodCombobox;

//    @FXML
//    private Pane SystemPane;


    @FXML
    private Button SystemSolveButton;

    @FXML
    private ToggleGroup systems;

    @FXML
    private Label SystemErrorLabel;

    private void drawFunctionPlot(Function function, double leftBoundary, double rightBoundary) {
        double intervalLength = Math.abs(rightBoundary - leftBoundary);
        double padding = intervalLength / 2.0;

        NumberAxis xAxis = new NumberAxis(leftBoundary - padding, rightBoundary + padding, intervalLength / 10.0); // Adjust the visible range
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("f(x)");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Function Plot");

        LineChart.Series<Number, Number> series = new LineChart.Series<>();
        series.setName(function.toString());

        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        double step = 0.1; // Adjust this for smoother or rougher plot
        for (double x = leftBoundary - padding; x <= rightBoundary + padding; x += step) {
            double y = function.compute(x);
            series.getData().add(new LineChart.Data<>(x, y));
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        // Adjust the y-axis range based on the function values
        double yPadding = Math.abs(maxY - minY) * 0.1; // 10% padding
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(minY - yPadding);
        yAxis.setUpperBound(maxY + yPadding);

        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);

        FunctionPane.getChildren().clear();
        FunctionPane.getChildren().add(lineChart);
        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setRightAnchor(lineChart, 0.0);
    }





    @FXML
    void onSolveEquationButtonClick(ActionEvent event) {
        try {
            RadioButton selectedFunctionButton = (RadioButton) functions.getSelectedToggle();
            String selectedFunctionText = selectedFunctionButton.getText();
            Function selectedFunction = functionMap.get(selectedFunctionText);

            String accuracy = AccuracyField.getText();
            String leftBoundary = LeftBoundaryField.getText();
            String rightBoundary = RightBoundaryField.getText();

            String methodName = MethodComboBox.getValue();
            Method selectedMethod = methodMap.get(methodName);

            double left = Double.parseDouble(leftBoundary);
            double right = Double.parseDouble(rightBoundary);
            double acc = Double.parseDouble(accuracy);

            int digitsAfterComma = calculateDigitsAfterComma(acc);

            if (left < right) {
                // Draw function plot
                drawFunctionPlot(selectedFunction, left, right);
            }

            boolean intervalIsCorrect = selectedFunction.checkIsolationInterval(left, right);
            if (intervalIsCorrect) {
                ErrorLabel.setVisible(false);
                Result result = selectedMethod.compute(selectedFunction, left, right, acc, digitsAfterComma);
                List<String> headers = result.getHeaders();
                List<List<String>> data = result.getData();

                ResultTable.getColumns().clear();
                for (int i = 0; i < headers.size(); i++) {
                    final int columnIndex = i;
                    TableColumn<ResultRow, String> column = new TableColumn<>(headers.get(i));
                    column.setCellValueFactory(cellData -> cellData.getValue().getRowData().get(columnIndex));
                    ResultTable.getColumns().add(column);
                }


                ObservableList<ResultRow> rows = FXCollections.observableArrayList();
                for (List<String> rowData : data) {
                    rows.add(new ResultRow(rowData));
                }

                ResultTable.setItems(rows);
            }
        } catch (NullPointerException e) {
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Please fill all required fields");
        } catch (NumberFormatException e) {
            ErrorLabel.setVisible(true);
            ErrorLabel.setText("Please enter numerical values into text fields");
        }
        catch (InputException e) {
            ErrorLabel.setVisible(true);
            ErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    void onSystemSolveButtonClick(ActionEvent event) {
        try {
            RadioButton selectedSystemButton = (RadioButton) systems.getSelectedToggle();
            String selectedSystemText = selectedSystemButton.getText();
            SystemOfEquations selectedSystem = systemMap.get(selectedSystemText);

            String accuracy = SystemAccuracyField.getText();
            String xFieldText = XField.getText();
            String yFieldText = YField.getText();

            String methodName = SystemMethodCombobox.getValue();
            SystemMethod selectedMethod = systemMethodMap.get(methodName);

            double x0 = Double.parseDouble(xFieldText);
            double y0 = Double.parseDouble(yFieldText);
            double acc = Double.parseDouble(accuracy);

            int digitsAfterComma = calculateDigitsAfterComma(acc);

            SystemErrorLabel.setVisible(false);

//            drawTwoVariableFunctionPlot(systemArray[0].getFunctions()[0],
//                    systemArray[0].getFunctions()[0], x0, y0, 5);


            Result result = selectedMethod.compute(selectedSystem, x0, y0, acc, digitsAfterComma);
            List<String> headers = result.getHeaders();
            List<List<String>> data = result.getData();

            ResultTable.getColumns().clear();
            for (int i = 0; i < headers.size(); i++) {
                final int columnIndex = i;
                TableColumn<ResultRow, String> column = new TableColumn<>(headers.get(i));
                column.setCellValueFactory(cellData -> cellData.getValue().getRowData().get(columnIndex));
                ResultTable.getColumns().add(column);
            }


            ObservableList<ResultRow> rows = FXCollections.observableArrayList();
            for (List<String> rowData : data) {
                rows.add(new ResultRow(rowData));
            }

            ResultTable.setItems(rows);

        } catch (NullPointerException e) {
            SystemErrorLabel.setVisible(true);
            SystemErrorLabel.setText("Please fill all required fields");
        } catch (NumberFormatException e) {
            SystemErrorLabel.setVisible(true);
            SystemErrorLabel.setText("Please enter numerical values into text fields");
        }
    }

    private int calculateDigitsAfterComma(double accuracy) {
        int digits = (int) Math.ceil(-Math.log10(accuracy));
        return Math.max(0, digits);
    }

    private void drawSystemPlot() {
        // Создание осей X и Y
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X");
        yAxis.setLabel("Y");

        // Настройка графика
        chart.setTitle("Function Plot");
        chart.setCreateSymbols(false); // Отключение отображения точек данных

        // Добавление осей к графику
        chart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        chart.setPrefSize(600, 400);
        chart.setAnimated(false);
        chart.setHorizontalGridLinesVisible(true);
        chart.setVerticalGridLinesVisible(true);
        chart.getXAxis().setAutoRanging(true);
        chart.getYAxis().setAutoRanging(true);

        for (SystemOfEquations system : systemArray) {
            for (TwoVariableFunction function : system.getFunctions()) {
                XYChart.Series<Double, Double> series = new XYChart.Series<>();
                series.setName(function.toString());

                // Вычисление точек для построения графика
                for (List<Double> point : function.computePoints()) {
                    series.getData().add(new XYChart.Data<>(point.get(0), point.get(1)));
                }

                // Добавление серии данных на график
                chart.getData().add(series);
            }
        }
    }

//    private void drawTwoVariableFunctionPlot(TwoVariableFunction function1, TwoVariableFunction function2, double x, double y, double range) {
//        double leftBoundaryX = x - range;
//        double rightBoundaryX = x + range;
//        double bottomBoundaryY = y - range;
//        double topBoundaryY = y + range;
//
//        double intervalLengthX = Math.abs(rightBoundaryX - leftBoundaryX);
//        double intervalLengthY = Math.abs(topBoundaryY - bottomBoundaryY);
//        double paddingX = intervalLengthX / 2.0;
//        double paddingY = intervalLengthY / 2.0;
//
//        NumberAxis xAxis = new NumberAxis(leftBoundaryX - paddingX, rightBoundaryX + paddingX, intervalLengthX / 10.0); // Adjust the visible range for x
//        NumberAxis yAxis = new NumberAxis(bottomBoundaryY - paddingY, topBoundaryY + paddingY, intervalLengthY / 10.0); // Adjust the visible range for y
//        xAxis.setLabel("x");
//        yAxis.setLabel("y");
//
//        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
//        lineChart.setTitle("System Plot");
//
//        LineChart.Series<Number, Number> series1 = new LineChart.Series<>();
//        series1.setName(function1.toString());
//
//        LineChart.Series<Number, Number> series2 = new LineChart.Series<>();
//        series2.setName(function2.toString());
//
//        double minX = Double.POSITIVE_INFINITY;
//        double maxX = Double.NEGATIVE_INFINITY;
//        double minY = Double.POSITIVE_INFINITY;
//        double maxY = Double.NEGATIVE_INFINITY;
//
//        double step = 0.1; // Adjust this for smoother or rougher plot
//        for (double i = leftBoundaryX; i <= rightBoundaryX; i += step) {
//            double z1 = function1.compute(i, y);
//            double z2 = function2.compute(x, i);
//            series1.getData().add(new LineChart.Data<>(i, z1));
//            series2.getData().add(new LineChart.Data<>(i, z2));
//            minX = Math.min(minX, i);
//            maxX = Math.max(maxX, i);
//            minY = Math.min(minY, Math.min(z1, z2));
//            maxY = Math.max(maxY, Math.max(z1, z2));
//        }
//
//        // Adjust the y-axis range based on the function values
//        double xPadding = Math.abs(maxX - minX) * 0.1; // 10% padding
//        double yPadding = Math.abs(maxY - minY) * 0.1; // 10% padding
//        xAxis.setAutoRanging(false);
//        yAxis.setAutoRanging(false);
//        xAxis.setLowerBound(minX - xPadding);
//        xAxis.setUpperBound(maxX + xPadding);
//        yAxis.setLowerBound(minY - yPadding);
//        yAxis.setUpperBound(maxY + yPadding);
//
//        lineChart.getData().addAll(series1, series2);
//        lineChart.setCreateSymbols(false);
//
//        SystemPane.getChildren().clear();
//        SystemPane.getChildren().add(lineChart);
//        AnchorPane.setTopAnchor(lineChart, 0.0);
//        AnchorPane.setBottomAnchor(lineChart, 0.0);
//        AnchorPane.setLeftAnchor(lineChart, 0.0);
//        AnchorPane.setRightAnchor(lineChart, 0.0);
//    }
//
//
//


}

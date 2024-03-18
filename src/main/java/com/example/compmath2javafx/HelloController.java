package com.example.compmath2javafx;

import com.example.compmath2javafx.exception.InputException;
import com.example.compmath2javafx.function.Function;
import com.example.compmath2javafx.function.Function1;
import com.example.compmath2javafx.function.Function2;
import com.example.compmath2javafx.function.Function3;
import com.example.compmath2javafx.method.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloController {

    private final Map<String, Method> methodMap = new HashMap<>();
    private final Map<String, Function> functionMap = new HashMap<>();

    public void initialize() {
        methodMap.put("Chord Method", new ChordMethod());
        methodMap.put("Secant Method", new SecantMethod());
        methodMap.put("Simple Iteration Method", new SimpleIterationMethod());
        MethodComboBox.getItems().addAll(methodMap.keySet());

        functionMap.put("Function 1", new Function1());
        functionMap.put("Function 2", new Function2());
        functionMap.put("Function 3", new Function3());
    }

    @FXML
    private TextField AccuracyField;

    @FXML
    private Label AccuracyText;

    @FXML
    private AnchorPane EquationPane;

    @FXML
    private Tab EquationTab;

    @FXML
    private RadioButton Function1Button;

    @FXML
    private ImageView Function1Image;

    @FXML
    private RadioButton Function2Button;

    @FXML
    private ImageView Function2Image;

    @FXML
    private RadioButton Function3Button;

    @FXML
    private Group FunctionButtonGroup;

    @FXML
    private TextField LeftBoundaryField;

    @FXML
    private Label LeftBoundaryText;

    @FXML
    private ComboBox<String> MethodComboBox;

    @FXML
    private Label MethodText;

    @FXML
    private TextField RightBoundaryField;

    @FXML
    private Label RightBoundaryText;

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
    void onHelloButtonClick(ActionEvent event) {
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

        try {
            boolean intervalIsCorrect = selectedFunction.checkIsolationInterval(left, right);
            if (intervalIsCorrect) {
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
        } catch (InputException e) {
            ErrorLabel.setText(e.getMessage());
        }
    }

    private int calculateDigitsAfterComma(double accuracy) {
        int digits = (int) Math.ceil(-Math.log10(accuracy));
        return Math.max(0, digits);
    }

}

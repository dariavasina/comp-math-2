package com.example.compmath2javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ResultRow {
    private final ObservableList<SimpleStringProperty> rowData;

    public ResultRow(List<String> rowData) {
        this.rowData = FXCollections.observableArrayList();
        for (String value : rowData) {
            this.rowData.add(new SimpleStringProperty(value));
        }
    }

    public ObservableList<SimpleStringProperty> getRowData() {
        return rowData;
    }
}
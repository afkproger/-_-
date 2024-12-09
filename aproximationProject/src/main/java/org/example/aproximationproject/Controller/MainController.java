package org.example.aproximationproject.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.example.aproximationproject.Model.Client;
import org.example.aproximationproject.Model.ExcelReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class MainController {

    private final ExcelReader excelReader = new ExcelReader();
    @FXML
    private Button manualButton; // кнопка для обработчки ручного ввода
    @FXML
    private Button addButton; // кнопка для добавления точки
    @FXML
    private Button removeButton; // кнопка для удаления точки
    @FXML
    private Button buildGraphButton; // кнопка для построения графика
    @FXML
    private TextField xCoordinateField; // поле для ввода X
    @FXML
    private TextField yCoordinateField; // поле для ввода Y
    @FXML
    private Button fileButton;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private Button aboutStudent;
    @FXML
    private Button aboutProject;


    @FXML
    public void initialize() {
        manualButton.setOnAction(event -> {
            lineChart.getData().clear();
            lineChart.setVisible(false);
        });

        fileButton.setOnAction(event -> {
            onExcelOpen();
        });

        aboutProject.setOnAction(actionEvent -> {
            lineChart.getData().clear();
            lineChart.setVisible(false);
        });

        aboutStudent.setOnAction(actionEvent -> {
            lineChart.getData().clear();
            lineChart.setVisible(false);
        });
    }



    @FXML
    private void onExcelOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите Excel файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Map<Double, Double> data = excelReader.readCoordinates(selectedFile);
                if (data != null) {
                    double[] coefficients = Client.getApproximationCoefficients(data);
                    System.out.println("Коэффициенты: " + Arrays.toString(coefficients));
                    if (coefficients != null) {
                        lineChart.setVisible(true);
                        drawGraph(coefficients, data);
                    } else {
                        System.out.println("Coefficients not found");
                    }
                } else {
                    System.out.println("ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void drawGraph(double[] coefficients, Map<Double, Double> points) {
        lineChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        try {
            for (int i = 0; i < points.size(); i++) {
                double x = i;
                double y = coefficients[0] * x + coefficients[1];
                System.out.println("x: " + x + ", y: " + y);
                series.getData().add(new XYChart.Data<>(x, y));
            }
            lineChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

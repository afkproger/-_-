package org.example.aproximationproject.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.example.aproximationproject.Model.Client;
import org.example.aproximationproject.Model.ExcelReader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    private final Map<Double, Double> points = new HashMap<>(); // Хранение точек
    private final ObservableList<String> pointsListData = FXCollections.observableArrayList(); // Данные для отображения в ListView

    private final ExcelReader excelReader = new ExcelReader();

    @FXML
    private HBox hBox;

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
    private ListView<String> pointsList; // Ссылка на ListView для отображения точек

    @FXML
    public void initialize() {
        pointsList.setItems(pointsListData); // Привязываем ObservableList к ListView

        manualButton.setOnAction(event -> {
            lineChart.getData().clear();
            lineChart.setVisible(false);
            hBox.setVisible(true);
        });

        fileButton.setOnAction(event -> {
            onExcelOpen();
//            hBox.setVisible(false);
        });

        aboutProject.setOnAction(actionEvent -> {
            lineChart.getData().clear();
            lineChart.setVisible(false);
            hBox.setVisible(false);
        });

        addButton.setOnAction(event -> addPoint());

        removeButton.setOnAction(event -> removePoint());

        buildGraphButton.setOnAction(event -> manualInput());

        aboutStudent.setOnAction(actionEvent -> {
            lineChart.getData().clear();
            lineChart.setVisible(false);
            hBox.setVisible(false);
        });
    }

    // Метод для добавления точки
    private void addPoint() {
        try {
            double x = Double.parseDouble(xCoordinateField.getText());
            double y = Double.parseDouble(yCoordinateField.getText());

            if (!xCoordinateField.getText().isEmpty() && !yCoordinateField.getText().isEmpty()) {
                points.put(x, y);  // Добавляем точку в коллекцию

                // Добавляем строку в ObservableList для отображения в ListView
                pointsListData.add("X: " + x + " Y: " + y);

                xCoordinateField.clear();
                yCoordinateField.clear();
            } else {
                showAlertMessage("Проверьте формат ввода координат");
                xCoordinateField.clear();
                yCoordinateField.clear();
            }
        } catch (NumberFormatException e) {
            showAlertMessage("Проверьте формат ввода координат");
            xCoordinateField.clear();
            yCoordinateField.clear();
        }
    }

    // Метод для удаления точки
    private void removePoint() {
        String selectedPoint = pointsList.getSelectionModel().getSelectedItem();
        if (selectedPoint != null) {
            // Разделяем строку для извлечения X-координаты
            String[] parts = selectedPoint.split(" ");
            double x = Double.parseDouble(parts[1].replace("X:", "").trim());

            points.remove(x);  // Удаляем точку из коллекции
            pointsListData.remove(selectedPoint);  // Удаляем точку из ObservableList
        }
    }

    // Метод для ввода данных вручную и построения графика
    private void manualInput() {
        try {
            double[] coefficients = Client.getApproximationCoefficients(points);
            if (coefficients != null) {
                lineChart.setVisible(true);
                drawGraph(coefficients, points);
            } else {
                showAlertMessage("Коэффициенты для построения графика не найдены");
            }
        } catch (IOException e) {
            showAlertMessage("Ошибка при подключении к серверу");
        }
    }

    // Метод для открытия файла Excel и обработки данных
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
                        showAlertMessage("Ошибка при подключении к серверу");
                    }
                } else {
                    showAlertMessage("Ошибка при открытии EXCEL файла");
                }
            } catch (IOException e) {
                showAlertMessage("Ошибка при отрисовке графика");
            }
        }
    }

    // Метод для построения графика на основе коэффициентов
    private void drawGraph(double[] coefficients, Map<Double, Double> points) {
        lineChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);
        try {
            for (Map.Entry<Double, Double> entry : points.entrySet()) {
                double x = entry.getKey();
                double y = coefficients[0] * x + coefficients[1];
                System.out.println("x: " + x + ", y: " + y);
                series.getData().add(new XYChart.Data<>(x, y));
            }
            lineChart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertMessage(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}

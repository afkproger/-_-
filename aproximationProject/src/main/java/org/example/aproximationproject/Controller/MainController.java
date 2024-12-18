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
import java.util.*;

public class MainController {
    private final Map<Double, ArrayList<Double>> points = new HashMap<>(); // Хранение точек
    private final ObservableList<String> pointsListData = FXCollections.observableArrayList(); // Данные для отображения в ListView
    private final int DEVIATION = 0;
    private final int TEMPERATURE = 1;

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
    private TextField zCoordinateField;
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
            double z = Double.parseDouble(zCoordinateField.getText());

            if (!xCoordinateField.getText().isEmpty() && !yCoordinateField.getText().isEmpty() && !zCoordinateField.getText().isEmpty()) {
                ArrayList<Double> point = new ArrayList<>();
                point.add(y);
                point.add(z);
                points.put(x, point);  // Добавляем точку в коллекцию

                // Добавляем строку в ObservableList для отображения в ListView
                pointsListData.add("Расход, кг/сек: " + x + " Отклонение уровня, мм: " + y + " Температура , С: " + z);

                xCoordinateField.clear();
                yCoordinateField.clear();
                zCoordinateField.clear();
            } else {
                showAlertMessage("Проверьте формат ввода координат");
                xCoordinateField.clear();
                yCoordinateField.clear();
                zCoordinateField.clear();
            }
        } catch (NumberFormatException e) {
            showAlertMessage("Проверьте формат ввода координат");
            xCoordinateField.clear();
            yCoordinateField.clear();
            zCoordinateField.clear();
        }
    }

    // КОЛХОЗНОЕ РЕШЕНИЕ , ПРОШУ ПРОСТИТЬ И НЕ ТРОГАТЬ!!!!
    private void removePoint() {
        String selectedPoint = pointsList.getSelectionModel().getSelectedItem();
        if (selectedPoint != null) {
            try {
                // Разделяем строку на части, используя "Расход, кг/сек:" как разделитель
                String[] parts = selectedPoint.split("Расход, кг/сек: ");
                if (parts.length > 1) {
                    String[] xPart = parts[1].split(" "); // Разделяем по пробелам
                    if (xPart.length > 0) {
                        double x = Double.parseDouble(xPart[0].trim()); // Получаем значение X

                        points.remove(x);  // Удаляем точку из коллекции
                        pointsListData.remove(selectedPoint);  // Удаляем точку из ObservableList
                    } else {
                        showAlertMessage("Ошибка формата данных. Не удалось извлечь значение X.");
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                showAlertMessage("Ошибка при удалении точки. Проверьте формат данных.");
            }
        }
    }

    // Метод для ввода данных вручную и построения графика
    private void manualInput() {
        try {
            double[] deviationGraphData = Client.getApproximationCoefficients(points, DEVIATION);
            double[] temperatureGraphData = Client.getApproximationCoefficients(points, TEMPERATURE);
            lineChart.setVisible(true);
            drawGraph(deviationGraphData, temperatureGraphData, points);
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
                Map<Double, ArrayList<Double>> data = excelReader.readCoordinates(selectedFile);
                if (data != null) {
                    double[] deviationGraphData = Client.getApproximationCoefficients(data, DEVIATION);
                    double[] temperatureGraphData = Client.getApproximationCoefficients(data, TEMPERATURE);
                    System.out.println("Отклонения уровня: " + Arrays.toString(deviationGraphData) + "Температура:" + Arrays.toString(temperatureGraphData));
                    lineChart.setVisible(true);
                    drawGraph(deviationGraphData, temperatureGraphData, data);
                } else {
                    showAlertMessage("Ошибка при открытии EXCEL файла");
                }
            } catch (IOException e) {
                showAlertMessage("Ошибка при отрисовке графика");
            }
        }
    }

    // Метод для создания графика (аппроксимация)
    private XYChart.Series<Number, Number> createGraphSeries(String name, double[] coefficients, int size) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (int x = 0; x < size; x++) {
            double y = coefficients[0] * x + coefficients[1];
            series.getData().add(new XYChart.Data<>(x, y));
        }
        return series;
    }

    // Метод для добавления точек на график
    private XYChart.Series<Number, Number> createPointsSeries(String name, Map<Double, ArrayList<Double>> points, int index) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);
        for (Map.Entry<Double, ArrayList<Double>> entry : points.entrySet()) {
            double x = entry.getKey();
            double y = entry.getValue().get(index);  // Используем нужный индекс для отклонения или температуры
            series.getData().add(new XYChart.Data<>(x, y));
        }
        return series;
    }

    // Метод для построения графика на основе коэффициентов
    private void drawGraph(double[] deviationGraphData, double[] temperatureGraphData, Map<Double, ArrayList<Double>> points) {
        lineChart.getData().clear(); // Очищаем график
        xAxis.setAutoRanging(true);
        yAxis.setAutoRanging(true);

        try {
            // График отклонения уровня (аппроксимация)
            XYChart.Series<Number, Number> deviationSeries = createGraphSeries("Отклонение уровня", deviationGraphData, points.size());
            lineChart.getData().add(deviationSeries);

            // График отклонения уровня (точки до аппроксимации)
            XYChart.Series<Number, Number> deviationPoints = createPointsSeries("Точки(Отклонение уровня)", points, DEVIATION);
            lineChart.getData().add(deviationPoints);

            // График температуры (аппроксимация)
            XYChart.Series<Number, Number> temperatureSeries = createGraphSeries("Температура", temperatureGraphData, points.size());
            lineChart.getData().add(temperatureSeries);

            // График температуры (точки до аппроксимации)
            XYChart.Series<Number, Number> temperaturePoints = createPointsSeries("Температура до аппроксимации", points, TEMPERATURE);
            lineChart.getData().add(temperaturePoints);

        } catch (Exception e) {
            showAlertMessage("Ошибка при отрисовке графиков");
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

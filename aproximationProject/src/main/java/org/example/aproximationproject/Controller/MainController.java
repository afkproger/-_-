package org.example.aproximationproject.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.example.aproximationproject.Model.Client;
import org.example.aproximationproject.Model.ExcelReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainController {

    private final ExcelReader excelReader = new ExcelReader();
    private double[] coefficients;
    @FXML
    private Button manualButton; // кнопка для обработчки ручного ввода
    @FXML
    private Button fileButton;

    @FXML
    private Button aboutStudent;
    @FXML
    private Button aboutProject;
    @FXML
    private Label textLabel;


    @FXML
    public void initialize() {
        manualButton.setOnAction(event -> {
            textLabel.setText("Manual");
        });

        fileButton.setOnAction(event -> {
            onExcelOpen();
        });

        aboutProject.setOnAction(actionEvent -> {
            textLabel.setText("About");
        });

        aboutStudent.setOnAction(actionEvent -> {
            textLabel.setText("About student");
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
                    String testData = Client.getApproximationCoefficients(data);
                    textLabel.setText(testData);
                } else {
                    textLabel.setText("ERROR");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

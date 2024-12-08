package org.example.aproximationproject.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Button manualButton; // кнопка для обработчки ручного ввода
    @FXML
    private Button fileButton;

    @FXML
    private Label textLabel;


    @FXML
    public void initialize() {
        manualButton.setOnAction(event -> {
            textLabel.setText("Manual");
        });

        fileButton.setOnAction(event -> {
            textLabel.setText("File");
        });
    }
}

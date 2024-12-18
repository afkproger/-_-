package org.example.aproximationproject.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ModalController {
    @FXML
    private Label info;

    @FXML
    private ImageView imageView;


    public void setInfo(String infoText) {
        info.setText(infoText);
    }

    public void setImage(String imagePath) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView.setImage(image);
        imageView.setVisible(true);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

    }


}

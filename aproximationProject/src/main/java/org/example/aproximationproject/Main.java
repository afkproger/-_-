package org.example.aproximationproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.aproximationproject.Model.Config;

//TODO: 1)+график по точкам 3)переписать функции зависимостей (т.к) расход - темп , расход - отклонение уровня



public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Config config = Config.getInstance("src/main/resources/config.properties");

        String title = config.getString("app.title" , "");
        int width = config.getInt("app.wid" , 0);
        int height = config.getInt("app.height" , 0);

        if (title == null || width == 0 || height == 0) {
            throw new RuntimeException("Ошибка конфигурации: отсутствуют необходимые параметры");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root , width , height);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();


    }
}

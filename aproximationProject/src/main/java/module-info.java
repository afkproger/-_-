module org.example.aproximationproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;


    opens org.example.aproximationproject to javafx.fxml;
    exports org.example.aproximationproject;
    exports org.example.aproximationproject.Controller;
    opens org.example.aproximationproject.Controller to javafx.fxml;
}
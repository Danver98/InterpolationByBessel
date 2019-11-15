package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("New App");
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
        //Controller c = new Controller();
        //c.setMu(1);
        //BesselPolynom b = new BesselPolynom(c,-10,10,(x) -> 1* Math.sin(1*x) * Math.cos(1/ Math.pow(x - 1,2)),10 );
    }
}

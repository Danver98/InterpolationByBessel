package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.beans.beancontext.BeanContextServicesSupport;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("New App");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        /*int n = 1000000000;
        long start, finish;
        start = System.currentTimeMillis();
        for(int i=0; i < n; i++){
            i<<=1;
            i>>=1;
            i++;
        }
        finish = System.currentTimeMillis();
        double elapsed = ((double)(finish - start)) / 1000;
        System.out.println("Elapsed time: " + (elapsed));*/
        /*BesselPolynom b = new BesselPolynom();
        Platform.exit();
        System.exit(0);*/
    }


    public static void main(String[] args) {

        launch(args);
    }

}

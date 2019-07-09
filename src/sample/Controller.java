package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import javax.swing.event.HyperlinkEvent;
import java.math.BigDecimal;
import java.util.function.Function;

public class Controller {

    private IntegerProperty precision = new SimpleIntegerProperty(2);
    private Function<Double,Double> function;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private LineChart<Double, Double> lineChart;

    @FXML
    private Button functionButton;

    @FXML
    private Button polygonButton;

    @FXML
    private Button diiferenceButton;

    @FXML
    private Button buildButton;

    @FXML
    private void buildFunction(ActionEvent event) {
        XYChart.Series<Double,Double> series = new XYChart.Series<Double,Double>();
        double a = 1 , b = 1 , e = 1, m = 1;
        function= (x) -> a* Math.sin(b*x) * Math.cos(e/ Math.pow(x - m,2));
        //Function<Double,Double> function = (x) ->  Math.cos(x)*Math.sin(x)+ 2*x ;
        series.setName("Trial");
        //xAxis.setLowerBound(-20);
        //xAxis.setUpperBound(-10);
        //for(double t = 0 ; t <= Math.PI;t+=0.1){
            //series.getData().add(new XYChart.Data<Double,Double>(Math.cos(t),Math.sin(t)));
        //}
        //for(double x = -10; x <= 10 ; x+=Math.pow(10, -precision){
        for(double x = -10; x <= 10 ; x+=0.1){
           x = (double)((int)Math.round(x*Math.pow(10,precision.get()))) /Math.pow(10,precision.get());
            if( x - m ==0) System.out.println("Adversity");
            series.getData().add(new XYChart.Data<Double,Double>(x,function.apply(x)));
        }
        lineChart.getData().add(series);
    }

    @FXML
    private void setPrecision(int precision){
        this.precision.set(precision);
    }

    @FXML
    private void buildPolynom(ActionEvent event) {

    }
}

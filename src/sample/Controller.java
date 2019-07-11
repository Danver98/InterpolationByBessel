package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.util.function.Function;

public class Controller {

    private IntegerProperty precision = new SimpleIntegerProperty(2);
    private IntegerProperty step = new SimpleIntegerProperty(1);
    private Function<Double,Double> function;
    private double start = -10;
    private double end = 10;
    private double top = 10;
    private double bottom = -10;
    private double alpha = 1 ;
    private double beta = 1;
    private double epsilon = 1;
    private double mu = 1;
    private int nodesAmount = 30;
    public static String alphaCode = "\u03B1"+":";
    public static String betaCode = "\u03B2"+":";
    public static String epsCode = "\u03B5"+":";
    public static String muCode = "\u03BC"+":";

    @FXML
    private LineChart<Double,Double> lineChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Button functionButton;

    @FXML
    private Button polygonButton;

    @FXML
    private Button diiferenceButton;

    @FXML
    private Label intervalStartLabel;

    @FXML
    private TextField intervalStartField;

    @FXML
    private Label intervalEndLabel;

    @FXML
    private TextField intervalEndField;

    @FXML
    private Label topValuesLabel;

    @FXML
    private TextField topValuesField;

    @FXML
    private Label bottomValuesLabel;

    @FXML
    private TextField bottomValuesField;

    @FXML
    private Label nodesAmountLabel;

    @FXML
    private TextField nodesAmountField;

    @FXML
    private Label alphaLabel;

    @FXML
    private TextField alphaField;

    @FXML
    private Label betaLabel;

    @FXML
    private TextField betaField;

    @FXML
    private Label epsLabel;

    @FXML
    private TextField epsField;

    @FXML
    private Label muLabel;

    @FXML
    private TextField muField;

    @FXML
    private Button buildButton;

    @FXML
    public void initialize(){
        alphaLabel.setText(alphaCode);
        betaLabel.setText(betaCode);
        epsLabel.setText(epsCode);
        muLabel.setText(muCode);
    }

    public int getPrecision() {
        return precision.get();
    }

    public void setPrecision(int precision){
        this.precision.set(precision);
    }

    public int getStep() {
        return step.get();
    }

    public void setStep(int step) {
        this.step.set(step);
    }

    @FXML
    private void build(ActionEvent event){
            lineChart.getData().clear();
        try {
            start = Double.parseDouble(intervalStartField.getText());
            end = Double.parseDouble(intervalEndField.getText());
            bottom = Double.parseDouble(bottomValuesField.getText());
            top = Double.parseDouble(topValuesField.getText());
            xAxis.setLowerBound(start);
            xAxis.setUpperBound(end);
            yAxis.setLowerBound(bottom);
            yAxis.setUpperBound(top);
            alpha = Double.parseDouble(alphaField.getText());
            beta = Double.parseDouble(betaField.getText());
            epsilon = Double.parseDouble(epsField.getText());
            mu = Double.parseDouble(muField.getText());
            nodesAmount = Integer.parseInt(nodesAmountField.getText());
        }
        catch(NumberFormatException e ){
            // какой-нибудь Alert!
        }
        // проверка условий(что строить)
    }

    @FXML
    private void buildFunction(ActionEvent event) {
        XYChart.Series<Double,Double> series = new XYChart.Series<Double,Double>();
        function= (x) -> alpha* Math.sin(beta*x) * Math.cos(epsilon/ Math.pow(x - mu,2));
        series.setName("Trial");
        for(double x = start; x <= end ; x+=Math.pow(10, -step.get())){
           x = (double)((int)Math.round(x*Math.pow(10,precision.get()))) /Math.pow(10,precision.get());
            series.getData().add(new XYChart.Data<Double,Double>(x,function.apply(x)));
        }
        lineChart.getData().add(series);
    }

    @FXML
    private void buildPolynom(ActionEvent event) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        BesselPolynom b = new BesselPolynom(start,end,function,nodesAmount);
        Function<Double , Double> polynom = (x) -> b.besselPolynomAt(x);
        for(double x = start ; x <=end; x+=Math.pow(10, -step.get())){
            series.getData().add(new XYChart.Data<Double,Double>(x,polynom.apply(x)));
        }
        lineChart.getData().add(series);
    }

    @FXML
    void buildDifference(ActionEvent event) {
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        BesselPolynom b = new BesselPolynom(start,end,function,nodesAmount);
        Function<Double , Double> polynom = (x) -> b.besselPolynomAt(x);
        for(double x = start ; x <=end; x+=Math.pow(10, -step.get())){
            series.getData().add(new XYChart.Data<Double,Double>(x,function.apply(x) - polynom.apply(x)));
        }
        lineChart.getData().add(series);
    }

}

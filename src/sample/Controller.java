package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Iterator;
import java.util.ListIterator;
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
    private double xStep = 0.1;
    private int nodesAmount = 30;
    ObservableList<TextField>  textFieldsList;
    XYChart.Series<Double, Double> polynomSeries;
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
    private CheckBox functionCheckBox;

    @FXML
    private CheckBox polynomCheckBox;

    @FXML
    private CheckBox diffCheckBox;

    @FXML
    private ProgressBar progBar;

    @FXML
    private ProgressIndicator progIndicator;

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
    private Label stepLabel;

    @FXML
    private TextField stepField;

    @FXML
    private Button buildButton;

    @FXML
    public void initialize(){
        alphaLabel.setText(alphaCode);
        betaLabel.setText(betaCode);
        epsLabel.setText(epsCode);
        muLabel.setText(muCode);
        polynomSeries = new XYChart.Series<>();
        buildButton.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.ENTER){
                build(new ActionEvent());
            }
        });
        textFieldsList.addAll(intervalStartField,intervalEndField,bottomValuesField,topValuesField,nodesAmountField,alphaField,betaField,epsField,muField);
        for(TextField tf: textFieldsList){
            tf.addEventHandler(KeyEvent.KEY_PRESSED , event -> {
                if(event.getCode() == KeyCode.KP_DOWN){
                    TextField t = textFieldsList.get(textFieldsList.indexOf(tf) + 1);
                    // setFocused
                }
            });
        }
    }

    public double getMu(){
        return mu;
    }

    public void setMu(double mu){
        this.mu = mu;
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

    private boolean in(double x,double arr[]){
        for(int i=0;i<arr.length;i++){
            if(x == arr[i])
                return true;
        }
        return false;
    }

    @FXML
    private void build(ActionEvent event){
            lineChart.getData().clear();
            polynomSeries.getData().clear();
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
            xStep = Double.parseDouble(stepField.getText());

        }
        catch(NumberFormatException e ){
            // какой-нибудь Alert!
        }
        // проверка условий(что строить)
        // здесь можно распараллелить
        if(functionCheckBox.isSelected()){
            buildFunction();
        }
        if(polynomCheckBox.isSelected()){
            buildPolynom();
        }
        if(diffCheckBox.isSelected()){
            buildDifference();
        }

    }

    private int digitsAfterPoint(double number){
        int order = 0;
        while(number < 1){
             number*=10;
             order++;
         }
        return order;
    }

    private void buildFunction() {
        XYChart.Series<Double,Double> series = new XYChart.Series<Double,Double>();
        function= (x) -> alpha* Math.sin(beta*x) * Math.cos(epsilon/ Math.pow(x - mu,2));
        series.setName("Trial");
        int order = digitsAfterPoint(xStep);
        for(double x = start; x <= end ; x+=xStep){
            x = (double)((int)Math.round(x*Math.pow(10,order))) /Math.pow(10,order);
            series.getData().add(new XYChart.Data<Double,Double>(x,function.apply(x)));
        }
        lineChart.getData().add(series);
        series.getData().forEach( x-> System.out.println( x.getXValue() + " -> " +x.getYValue()));
    }

    private void buildPolynom() {
        function= (x) -> alpha* Math.sin(beta*x) * Math.cos(epsilon/ Math.pow(x - mu,2));
        BesselPolynom b = new BesselPolynom(this,start,end,function,nodesAmount);
        /*for(int i=0;i < b.getX().length;i++){
            series.getData().add(new XYChart.Data<Double,Double>(b.getX()[i] , b.getY()[i]));
        }*/
        Function<Double,Double> polynom = (x) -> b.besselPolynomAtNeg(x);
        for(double x = start ; x <=end; x+=xStep){
           /* if(in(x,b.getX())) {
                continue;
            }*/
            polynomSeries.getData().add(new XYChart.Data<Double,Double>(x,polynom.apply(x)));
        }
        System.out.println("Calculation is over");
        /*for(double x = start ; x <=end; x+=Math.pow(10, -step.get())){
            series.getData().add(new XYChart.Data<Double,Double>(x,polynom.apply(x)));
        }*/
        lineChart.getData().add(polynomSeries);
        //polynomSeries.getData().forEach((x) -> System.out.println(x.getXValue() + " -> " + x.getYValue()));
    }

    private void buildDifference() {
        if(!polynomCheckBox.isSelected()){
            buildPolynom();
        }
        XYChart.Series<Double, Double> series = new XYChart.Series<Double, Double>();
        BesselPolynom b = new BesselPolynom(this,start,end,function,nodesAmount);
        //Function<Double , Double> polynom = (x) -> b.besselPolynomAt(x);
        for(double x = start ; x <=end; x+=xStep){
            final double val = x;
            series.getData().add(new XYChart.Data<Double,Double>(x,function.apply(x) - polynomSeries.getData().stream().filter(obj -> obj.getXValue() == val).findFirst().get().getYValue()));
        }
        lineChart.getData().add(series);
    }


}

package sample;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.math.*;

public class BesselPolynom {

    private double step;
    private double sum;
    private double initSum;
    private double x0;
    private double start;
    private double end;
    private int nodesAmount;
    private double y[];
    private double xPos[];
    private double yPos[];
    private double xNeg[];
    private double yNeg[];
    private Function<Double,Double> function;
    private Function<Double,Double> besselPolynom;
    private LinkedHashMap<Double,Double> basicPoints;

    public BesselPolynom(){

    }

    public BesselPolynom(double start, double end, Function<Double, Double> function, int nodesAmount) {
        this.start = start;
        this.end = end;
        this.nodesAmount = nodesAmount;
        this.function = function;
        y = new double[nodesAmount];
        initValues();
        this.initSum = (yPos[0]+ yNeg[1])/2;
    }

    private void initValuesNeg(){
        double x = start;
        if(nodesAmount % 2 ==0) {
            yNeg = new double[nodesAmount >> 1];
            yPos = new double[nodesAmount >> 1 + 1];
            this.step = (end + start) / nodesAmount;
        }
        else {
            yNeg = new double[(nodesAmount +1)>>1];
            yPos = new double[(nodesAmount +1)>>1 +1];
            this.step = (end+start) / (nodesAmount + 1);
        }
            for(int i=0,j = yNeg.length-1; i <yNeg.length;i++,j--){
                x+=i*step;
                yNeg[j] = function.apply(x);
            }
            for(int i=0; i< yPos.length;i++){
                x+=i*step;
                yPos[i] = function.apply(x);
            }
            x0=yPos[0];
        // nodesAmount -=2;
    }

    private void initValues(){
        double x;
        if(nodesAmount % 2 ==0){
            this.step = (end - start) / nodesAmount;
            for(int i=0,j = nodesAmount-1; i <nodesAmount;i++,j--){
                x = start + i*step;
                y[j] = function.apply(x);
            }
        }
        else {

        }

        // nodesAmount -=2;
    }

    public double besselPolynomAt(double x){
        double q = (x -x0)/step,resInter=0,qInter =0;
        BigInteger fact = BigInteger.ONE;
        nodesAmount>>=1;    // по половине узлов
        double res = initSum + (q- 0.5) * diff(1,0);          // конечная разность должна идти по отрицательным индексам
        for(int i=1;i<=nodesAmount;i++){
            for(int j = 2*i - 1;j <=2*i;j++)
                fact=fact.multiply(BigInteger.valueOf(j));
            qInter*=(q-i)*(q+i-1)/fact.longValueExact();
            res+=qInter*((diff(2*i,i*(-1)) + diff(2*i,(i-1)*(-1)))/2 + (q-0.5)*diff(2*i+1,i*(-1))/(2*i+1));
        }
        return res;
    }

    public double besselPolynomAtNeg(double x){
        double q = (x -x0)/step,resInter=0,qInter =0;
        BigInteger fact = BigInteger.ONE;
        double res = initSum + (q- 0.5) * diff(1,0);
        for(int i=1;i<=yNeg.length;i++){
            for(int j = 2*i - 1;j <=2*i;j++)
                fact=fact.multiply(BigInteger.valueOf(j));
            qInter*=(q-i)*(q+i-1)/fact.longValueExact();
            res+=qInter*((diffNeg(2*i,i*(-1)) + diffNeg(2*i,(i-1)*(-1)))/2 + (q-0.5)*diffNeg(2*i+1,i*(-1))/(2*i+1));
        }
        return res;
    }

    private double binom(double a , double b , int n){
        double sum = 0;
        for(int k=0 ; k<=n;k++){
            sum+=comb(k,n).longValueExact()*Math.pow(a,n-k)* Math.pow(b,k);
        }
        return sum;
    }

    private BigInteger comb(int k , int n){
        if(k>n)
            return BigInteger.ZERO;
        BigInteger num =BigInteger.ONE, denum=factBig(k);
        for(int i= n ; i >= n-k+1; i--){
            num= num.multiply(BigInteger.valueOf(i));
        }
        return num.divide(denum);
    }

    private double diff(int order, int k) {
        if(order ==1){
            return y[k+1] - y[k];
        }
        else {
            double sum = 0;
            for(int i=0; i < order;i++){
                sum+=Math.pow(-1,i)* comb(i,order).longValueExact()*y[k+order-i];
            }
            return sum;
        }

    }

    private double diffNeg(int order, int k) {
        if(order ==1){
            return yNeg[k+1] - yNeg[k];
        }
        else {
            double sum = 0;
            for(int i=0; i < order;i++){
                sum+=Math.pow(-1,i)* comb(i,order).longValueExact()*yNeg[k+order-i];
            }
            return sum;
        }

    }

    private double diffRec(int order, int k){
        if(order ==1){
            return y[k+1] - y[k];
        }
        else {
            return diffRec(order -1 , k + 1) - diffRec(order - 1 , k);
        }

    }

    private double diffRecNeg(int order, int k){
        if(order ==1){
            return yNeg[k+1] - yNeg[k];
        }
        else {
            return diffRecNeg(order -1 , k + 1) - diffRecNeg(order - 1 , k);
        }

    }

    private long fact(int n){

        long res =1;
        for(int i=1; i <=n;i++){
            res*=i;
        }
        return res;
    }

    private BigInteger factBig(int n){
        BigInteger big = BigInteger.ONE;
        for(int i=1; i <=n;i++){
            big = big.multiply(BigInteger.valueOf(i));
        }
        return big;
    }

    private int GCD(int a, int b){
        while(a != b) {
            if(a > b){
                a =a - b;
            }
            else {
                b = b-a;
            }
        }

        return a;
    }


}

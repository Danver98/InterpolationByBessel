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

    public BesselPolynom(double step, double sum,double x0,double start,double end,Function<Double,Double> function, LinkedHashMap<Double,Double> basicPoints){
    this.step = step;
    this.sum = sum;
    this.x0 = x0;
    this.start = start;
    this.end = end;
    this.nodesAmount = basicPoints.size();
    this.function = function;
    this.basicPoints = basicPoints;
    this.initSum = (yPos[0]+ yNeg[1])/2;
    }

    public void besselPolynomAt(double x){
        double q = (x -x0)/step,resInter=0,qInter =0,fact = 1;
        double res = initSum + (q- 0.5) * diff(1,0);          // конечная разность должна идти по отрицательным индексам
        for(int i=1;i<=nodesAmount;i++){
            for(int j = 2*i - 1;j <=2*i;j++)
                fact*=j;
            qInter*=(q-i)*(q+i-1)/fact;
            res+=qInter*((diff(2*i,i*(-1)) + diff(2*i,(i-1)*(-1)))/2 + (q-0.5)*diff(2*i+1,i*(-1))/(2*i+1));
        }
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

    private double diffRec(int order, int k){
        if(order ==1){
            return y[k+1] - y[k];
        }
        else {
            return diffRec(order -1 , k + 1) - diffRec(order - 1 , k);
        }

    }

    private long fact(int n){

        long res =1;
        for(int i=1; i <=n;i++){
            res*=i;
        }
        return res;
    }

    private long factRec(int n){
        if((n==1) || (n==0)){
            return 1;
        }
        else return n*factRec(n-1);
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

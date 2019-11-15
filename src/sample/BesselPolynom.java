package sample;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.math.*;

public class BesselPolynom {
    public static double EPSILON = 0.00000001;
    public static double SHIFT = 0.0000001;
    private Controller controller;
    private double step;
    private double initSum;
    private double x0;
    private double start;
    private double end;
    private int nodesAmount;
    private double y[];
    private double x[];
    private double yPos[];
    private double yNeg[];
    private Function<Double,Double> function;
    private Function<Double,Double> besselPolynom;
    private LinkedHashMap<Double,Double> basicPoints;

    public BesselPolynom(){

    }

   /* public BesselPolynom(double start, double end, Function<Double, Double> function, int nodesAmount) {
        this.start = start;
        this.end = end;
        this.nodesAmount = nodesAmount;
        this.function = function;
        y = new double[nodesAmount];
        initValuesNeg();
        this.initSum = (yPos[0]+ yNeg[1])/2;
    }*/

    public BesselPolynom(Controller controller , double start, double end, Function<Double, Double> function, int nodesAmount) {
        this.controller = controller;
        this.start = start;
        this.end = end;
        if(nodesAmount %2 ==0){
            this.nodesAmount = nodesAmount;
        }
        else{
            this.nodesAmount = nodesAmount + 1;
        }
        this.function = function;
        y = new double[this.nodesAmount];
        x = new double[this.nodesAmount];
        System.out.println(y.length);
        initValuesSym();
        initSum =( y[(this.nodesAmount>>1)] + y[(this.nodesAmount>>1) - 1])/2;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    private void initValuesSym(){                    // можно распараллелить ; проверка,если x == epsilon
        double x;
        step = (end - start) / nodesAmount;
        double middle =(start + end) / 2;
        for (int i=0; i < (nodesAmount>>1);i++) {
            x = middle - (0.5 + i)*step;
            if(Math.abs(x - controller.getMu()) < EPSILON){
                x+=SHIFT;
            }
            this.x[(nodesAmount>>1) -1 - i] = x;
            y[(nodesAmount>>1) -1 - i] = function.apply(x);
            x = middle + (0.5 + i)*step;
            if(Math.abs(x - controller.getMu()) < EPSILON){
                x-=SHIFT;
            }
            this.x[(nodesAmount>>1)+ i] = x;
            y[(nodesAmount>>1)+ i] = function.apply(x);
        }
        System.out.println(" Start = " + start + " End = " + end + " Step = " + step + " Middle= " + (middle =(start + end) / 2));
        for (int i=0 ; i < this.x.length;i++) {
            System.out.printf("%.7f ",this.x[i]);
        }
        System.out.println();

        for (int i=0; i < y.length; i++) {
            System.out.printf("%.3f ",y[i]);
        }
    }

    public double besselPolynomAt(double x){
        double q = (x -x0)/step,resInter=0,qInter =0 , rest = 0;
        BigInteger fact = BigInteger.ONE;
        int nodes = nodesAmount>>1;    // по половине узлов
        double res = initSum + (q- 0.5) * diff(1,0);          // конечная разность должна идти по отрицательным индексам
        for(int i=1;i<=nodes;i++){
            for(int j = 2*i - 1;j <=2*i;j++)
                fact=fact.multiply(BigInteger.valueOf(j));
            qInter*=(q-i)*(q+i-1)/fact.longValueExact();
            res+=qInter*((diff(2*i,i*(-1)) + diff(2*i,(i-1)*(-1)))/2 + (q-0.5)*diff(2*i+1,i*(-1))/(2*i+1));
        }
        return res;
    }                        // longValueExact()

    public double besselPolynomAtNeg(double x){
        double q = (x -x0)/step,resInter=0,rest = 0;
        BigDecimal qInter = BigDecimal.ONE, fact = BigDecimal.ONE;
        int m= (nodesAmount-2)>>1;
        double res = initSum + (q- 0.5) * diff(1,0);
        for(int i=1;i<=m;i++){
            for(int j = 2*i - 1;j <=2*i;j++)
                fact=fact.multiply(BigDecimal.valueOf(j));
            qInter=qInter.multiply((BigDecimal.valueOf((q-i)*(q+i-1)))).divide(fact,RoundingMode.HALF_UP);
            res+=qInter.doubleValue()*((diffBig(2*i,m - i).doubleValue() + diffBig(2*i,(m-(i-1))).doubleValue())/2 + (q-0.5)*diffBig(2*i+1,m - i).doubleValue()/(2*i+1));
        }
        return res;
    }

    public double besselPolynomAtNegRec(double x){
        double q = (x -x0)/step,resInter=0,qInter =0,rest = 0;
        BigInteger fact = BigInteger.ONE;
        int m= (nodesAmount-2)>>1;
        double res = initSum + (q- 0.5) * diff(1,0);
        for(int i=1;i<=m;i++){
            System.out.println(" i = " + i + " , 2i+1 = " + (2*i+1)+ " , m - i = " + (m-i) + " , m - i - 1 = " + (m - i-1) + " , m = " + m);
            for(int j = 2*i - 1;j <=2*i;j++)
                fact=fact.multiply(BigInteger.valueOf(j));
            qInter*=(q-i)*(q+i-1)/fact.longValueExact();
            res+=qInter*((diffRec(2*i,m-i) + diffRec(2*i,(m-(i-1))))/2 + (q-0.5)*diffRec(2*i+1,m - i)/(2*i+1));
        }
        return res;
    }                           // longValueExact()

    private double binom(double a , double b , int n){                           // longValueExact()
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
            for(int j=0; j < order;j++){
                sum+=Math.pow(-1,j)* comb(j,order).longValueExact()*y[k+order-j];
            }
            return sum;
        }

    }                      // longValueExact()

    private BigDecimal diffBig(int order, int k) {
        if(order ==1){
            return BigDecimal.valueOf(y[k+1] - y[k]);
        }
        else {
            BigDecimal sum = BigDecimal.ZERO;
            for(int j=0; j < order;j++){
                //sum+=Math.pow(-1,j)* comb(j,order).longValueExact()*y[k+order-j];
                double t = Math.pow(-1,j)*y[k+order-j];
                sum = sum.add(BigDecimal.valueOf(Math.pow(-1,j)*y[k+order-j]).multiply(new BigDecimal(comb(j,order))));
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

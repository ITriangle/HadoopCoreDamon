package utils;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.List;

/**
 * Created by wl on 10/19/17.
 */
public class LinearRegression {


    private SimpleRegression simpleRegression;

    public LinearRegression(double[][] pointData){
        this.simpleRegression = new SimpleRegression();
        this.simpleRegression.addData(pointData);
    }


    public double getIntercept(){

        return this.simpleRegression.getIntercept();
    }

    public double getSlope(){
        return this.simpleRegression.getSlope();
    }

}

package com.chalansoftware.veldopname;

import android.arch.lifecycle.ViewModel;

/**
 * Created by Charl Gouws on 2017/11/11.
 */

public class Point
        extends ViewModel {
    
    private String mName;
    private double mCountPoint;
    private double mPercentage;
    
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public double getCountPoint() {
        return mCountPoint;
    }
    public void setCountPoint(double countPoint) {
        mCountPoint = countPoint;
    }
    public double getPercentage() {
        return mPercentage;
    }
    public void setPercentage(double percentage) {
        mPercentage = percentage;
    }
}

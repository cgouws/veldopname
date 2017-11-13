package com.chalansoftware.veldopname;

import android.arch.lifecycle.ViewModel;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Charl Gouws on 2017/11/11.
 */

public class Point implements Parcelable {
        //extends ViewModel {
    
    private String mName;
    private double mCountPoint;
    private double mPercentage;
    
    public Point(String name) {
        mName = name;
    }
    protected Point(Parcel in) {
        mName = in.readString();
        mCountPoint = in.readDouble();
        mPercentage = in.readDouble();
    }
    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override public Point createFromParcel(Parcel in) {
            return new Point(in);
        }
        
        @Override public Point[] newArray(int size) {
            return new Point[size];
        }
    };
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
    @Override public int describeContents() {
        return 0;
    }
    @Override public void writeToParcel(Parcel dest, int flags) {
    
        dest.writeString(mName);
        dest.writeDouble(mCountPoint);
        dest.writeDouble(mPercentage);
    }
}

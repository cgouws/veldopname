package com.chalansoftware.veldopname;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Charl Gouws on 2017/11/11.
 *
 * Data class for creating count points.
 */

public class Point implements Parcelable {
        //extends ViewModel {
    
    private String mName;
    private double mPointCount;
    private double mPercentage;
    
    Point(String name) {
        mName = name;
    }
    private Point(Parcel in) {
        mName = in.readString();
        mPointCount = in.readDouble();
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
    double getPointCount() {
        return mPointCount;
    }
    void setPointCount(double pointCount) {
        mPointCount = pointCount;
    }
    double getPercentage() {
        return mPercentage;
    }
    void setPercentage(double percentage) {
        mPercentage = percentage;
    }
    @Override public int describeContents() {
        return 0;
    }
    @Override public void writeToParcel(Parcel dest, int flags) {
    
        dest.writeString(mName);
        dest.writeDouble(mPointCount);
        dest.writeDouble(mPercentage);
    }
}

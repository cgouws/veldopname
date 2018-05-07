package com.chalansoftware.veldopname;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by Charl Gouws on 2017/11/11.
 * <p>
 * Data class for creating count points.
 */

public class Point
        implements Parcelable,
        Comparable {
    //extends ViewModel {
    
    private String mName;
    private double mPointCount = 0d;
    private double mPercentage = 0d;
    private UUID mId;
    Point(String name) {
        mName = name;
        mId = UUID.randomUUID();
    }
    public Point(UUID id) {
        // For returning a point from the database with PointCursorWrapper.
        mId = id;
    }
    
    private Point(Parcel in) {
        mName = in.readString();
        mPointCount = in.readDouble();
        mPercentage = in.readDouble();
        mId = UUID.fromString(in.readString());
    }
    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override public Point createFromParcel(Parcel in) {
            return new Point(in);
        }
        
        @Override public Point[] newArray(int size) {
            return new Point[size];
        }
    };
    public UUID getId() {
        return mId;
    }
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    double getPointCount() {
        return mPointCount;
    }
    public void setPointCount(double pointCount) {
        mPointCount = pointCount;
    }
    double getPercentage() {
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
        dest.writeDouble(mPointCount);
        dest.writeDouble(mPercentage);
        dest.writeString(String.valueOf(mId));
    }
    
    @Override public int compareTo(@NonNull Object o) {
        return 0;
    }
}

package com.chalansoftware.veldopname.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.chalansoftware.veldopname.Point;

import java.util.UUID;

import static com.chalansoftware.veldopname.database.PointDbSchema.PointTable.Cols;

/**
 * Created by Charl Gouws on 2017/12/12.
 */

public class PointCursorWrapper
        extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public PointCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Point getPoint() {
        // Used for building a Point object from the database. Used by PointLab.getPointsList()
        // to build a new List from the points in the database.
        String uuidString = getString(getColumnIndex(Cols.POINTUUID));
        String name = getString(getColumnIndex(Cols.POINTNAME));
        Double count = getDouble(getColumnIndex(Cols.POINTCOUNT));
        Double percentage = getDouble(getColumnIndex(Cols.POINTPERCENTAGE));
        
        Point point = new Point(UUID.fromString(uuidString));
        point.setName(name);
        point.setPointCount(count);
        point.setPercentage(percentage);
        
        return point;
    }
}

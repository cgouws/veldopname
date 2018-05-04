package com.chalansoftware.veldopname;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chalansoftware.veldopname.database.PointCursorWrapper;
import com.chalansoftware.veldopname.database.PointDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.chalansoftware.veldopname.database.PointDbSchema.PointTable.Cols;
import static com.chalansoftware.veldopname.database.PointDbSchema.PointTable.TABLE_NAME;

/**
 * Created by Charl Gouws on 2017/12/08.
 * <p>
 * Data handling class. If any changes to the way data is handled needs to be done, only this class
 * needs to change.
 */

class PointLab {
    private static PointLab sPointLab;
    private SQLiteDatabase mDatabase;
    
    static PointLab getInstance(Context context) {
        if (sPointLab == null) {
            sPointLab = new PointLab(context);
        }
        return sPointLab;
    }
    
    static PointLab getInstance(Context context, String databaseName) {
        if (sPointLab == null) {
            sPointLab = new PointLab(context);
        }
        return sPointLab;
    }
    private PointLab(Context context) {
        context = context.getApplicationContext();
        mDatabase = new PointDatabaseHelper(context.getApplicationContext()).getWritableDatabase();
    }
    
    private PointLab(Context context, String databaseName) {
        context = context.getApplicationContext();
        //mDatabase = new PointDatabaseHelper(context.getApplicationContext())
        // .getWritableDatabase();
        mDatabase = new PointDatabaseHelper(context.getApplicationContext(),
                databaseName).getWritableDatabase();
    }
    List<Point> getPointsList() {
        // Building a List from the data in the database, if it exists.
        List<Point> pointsList = new ArrayList<>();
        
        try (PointCursorWrapper cursor = queryPoints(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                pointsList.add(cursor.getPoint());
                cursor.moveToNext();
            }
        }
        return pointsList;
    }
    
    void addPoint(Point point) {
        // Adds a new row to the database.
        // Wrap the new Point object into a ContentValues object and then inserts it into the db.
        // Called from DialogAdd's addButton.
        ContentValues values = wrapIntoContentValuesObject(point);
        mDatabase.insert(TABLE_NAME, null, values);
    }
    void removePoint(Point point) {
        // Removes a row from the database. Gets called when item is swiped.
        String uuidString = point.getId().toString();
        mDatabase.delete(TABLE_NAME, Cols.POINTUUID + " = ?", new String[]{uuidString});
    }
    
    void updatePoint(Point point) {
        // Writes existing Point objects to an existing row in the database. Called when
        // the database needs to be updated, like in MainActivity.onPause().
        String uuidString = point.getId().toString();
        ContentValues values = wrapIntoContentValuesObject(point);
        
        mDatabase.update(TABLE_NAME, values, Cols.POINTUUID + " = ?", new String[]{uuidString});
    }
    
    void updateCount(Point point) {
    
    }
    private static ContentValues wrapIntoContentValuesObject(Point point) {
        // Inserts a point object with all it's data into a ContentValues object, which can then
        // be used to write the data into the database. Called in addPoint() and updatePoint().
        ContentValues values = new ContentValues();
        values.put(Cols.POINTUUID, point.getId().toString());
        values.put(Cols.POINTNAME, point.getName());
        values.put(Cols.POINTCOUNT, point.getPointCount());
        values.put(Cols.POINTPERCENTAGE, point.getPercentage());
        
        return values;
    }
    private PointCursorWrapper queryPoints(String whereClause, String[] whereArgs) {
        // Suppressed because query closed in getPointsList().
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(TABLE_NAME, null, whereClause,
                whereArgs, null, null, null, null);
        return new PointCursorWrapper(cursor);
    }
    public void createTable(String tableName) {
    }
}

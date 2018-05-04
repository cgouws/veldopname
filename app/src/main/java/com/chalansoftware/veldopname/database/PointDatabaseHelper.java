package com.chalansoftware.veldopname.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.chalansoftware.veldopname.database.PointDbSchema.PointTable.Cols;

/**
 * Created by Charl Gouws on 2017/12/08.
 * <p>
 * Helper class to open, create or upgrade a database. Called in PointLab's constructor.
 */

public class PointDatabaseHelper
        extends SQLiteOpenHelper {
    
    public static final int VERSION = 1;
    //public static final String DATABASE_NAME = "pointdatabase.db";
    public static final String TABLE_NAME = "points";
    
    public PointDatabaseHelper(Context context) {
        super(context, PointDbSchema.DATABASE_NAME, null, VERSION);
    }
    
    public PointDatabaseHelper(Context context, String databaseName) {
        super(new DatabaseContext(context), databaseName, null, VERSION);
    }
    @Override public void onCreate(SQLiteDatabase db) {
        // CREATE TABLE points (_id INTEGER PRIMARY KEY AUTOINCREMENT, uuid TEXT, pointname TEXT,
        // pointcount INTEGER, pointpercentage DOUBLE).
        db.execSQL("CREATE TABLE "
                           + TABLE_NAME
                           + "("
                           + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                           + Cols.POINTUUID
                           + " TEXT"
                           + ", "
                           + Cols.POINTNAME
                           + " TEXT"
                           + ", "
                           + Cols.POINTCOUNT
                           + " DOUBLE"
                           + ", "
                           + Cols.POINTPERCENTAGE
                           + " DOUBLE"
                           + ")");
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    
    }
}

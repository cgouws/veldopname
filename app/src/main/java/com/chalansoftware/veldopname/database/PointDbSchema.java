package com.chalansoftware.veldopname.database;

/**
 * Created by Charl Gouws on 2017/12/08.
 * <p>
 * Class to define the database schema. Simply a collection of string constants for use in the
 * database classes to ensure consistency in string usage.
 */

public class PointDbSchema {
    // Inner class to define string constants for the table definition.
    public static final String DATABASE_NAME = "pointdatabase.db";
    public static final class PointTable {
        public static final String TABLE_NAME = "points";
        
        // Inner class to describe the columns
        public static final class Cols {
            public static final String POINTUUID = "uuid";
            public static final String POINTNAME = "Naam";
            public static final String POINTCOUNT = "Hoeveelheid";
            public static final String POINTPERCENTAGE = "Persentasie";
        }
    }
}

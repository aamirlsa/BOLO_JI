package com.boloji.videocallchat.AdmobAds;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataHelprt extends SQLiteOpenHelper {

    public static final String DATABASENAME = "Count";
    public static final String TABLENAME = "data";
    public static final String COL_1 = "id";
    public static final String COL_2 = "category";
    public static final String COL_3 = "count";

    public static final String CREATE_TABLE = "CREATE TABLE data(id INTEGER PRIMARY KEY, category TEXT, count int)";

    public DataHelprt(Context context) {
        super(context, DATABASENAME, null, 1);
        Log.v(":::Database Operation", "Database Created...");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Log.v(":::Database Operation", "Table Created...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);

        Log.v(":::Database Operation", "Table Created...again");
        onCreate(db);
    }


    public void adddata(String category, int count) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_2, category);
        values.put(COL_3, count);

        db.insert(TABLENAME, null, values);
        Log.v(":::Database Operation", "One Row Inserted...");

        db.close();
    }

    public boolean checkExistance(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SelectQuery = "SELECT * FROM " + TABLENAME + " WHERE " + COL_2 + " = " + "'" + category + "'";
        Cursor cursor = db.rawQuery(SelectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            db.close();
            return  true;
        }else {
            db.close();
            return false;
        }

    }

    public int getCategorycount(String category) {

        int count = 1;
        SQLiteDatabase db = this.getReadableDatabase();
        String SelectQuery = "SELECT * FROM " + TABLENAME + " WHERE " + COL_2 + " = " + "'" + category + "'";
        Cursor cursor = db.rawQuery(SelectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            count = cursor.getInt(cursor.getColumnIndex(COL_3));
            Log.v(":::Database Operation", "category data retrived..."+count);

            db.close();
        }
        return  count;
    }

    public void addCategorycount(String category,int count) {

        SQLiteDatabase db = this.getReadableDatabase();
        String SelectQuery = "UPDATE " + TABLENAME + " SET " + COL_3 + " = " + "'" + count + "'" + " WHERE "
                + COL_2 + " = " + "'" + category + "'";
        Cursor cursor = db.rawQuery(SelectQuery, null);

        Log.v(":::Database Operation", "Query..."+SelectQuery);
        Log.v(":::Database Operation", "One Row Updated..."+cursor.getCount());

    }


}

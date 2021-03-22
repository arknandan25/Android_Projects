package com.example.e2eeapp_alpha.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CHATAPP.db";
    public static final String TABLE_NAME = "TEMP1";

    //COLS
    public static final String col1 = "ID";
    public static final String col2 = "MESSAGE_ID";
    public static final String col3 = "MESSAGE";
    public static final String col4 = "TIME";
    public static final String col5 = "SENDER";
    public static final String col6 = "RECEIVER";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,  MESSAGE_ID TEXT, MESSAGE TEXT, TIME TEXT, SENDER TEXT, RECEIVER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

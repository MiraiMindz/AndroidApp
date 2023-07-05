package com.uezo.uezo_av2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_LINK = "Login.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_LINK, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        database.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = database.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "Select * from users where username=?",
                new String[] {
                        username
                });
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(
                "Select * from users where username=? and password=?",
                new String[] {
                        username, password
                });
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }
}

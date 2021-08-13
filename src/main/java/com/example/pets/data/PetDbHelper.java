package com.example.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class PetDbHelper extends SQLiteOpenHelper {

    public PetDbHelper( Context context) {
        super(context,PetContract.PetEntry.TABLE_NAME, null,PetContract.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_DB_TABLE = " CREATE TABLE " + PetContract.PetEntry.TABLE_NAME
                + "(" + PetContract.PetEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PetContract.PetEntry.COLUMN_GENDER +" INTEGER,"
                + PetContract.PetEntry.COLUMN_BREED + " TEXT NOT NULL,"
                + PetContract.PetEntry.COLUMN_WEIGHT + " INTEGER,"
                + PetContract.PetEntry.COLUMN_NAME + " TEXT NOT NULL);";
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

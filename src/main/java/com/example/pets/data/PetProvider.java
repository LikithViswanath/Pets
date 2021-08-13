package com.example.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class PetProvider extends ContentProvider {

    PetDbHelper dbHelper;
    HashMap<String ,String > values;
    final static int ALL_ENTRIES = 2;
    final static int SINGLE_ENTRIES = 1;
    static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
      uriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PetEntry.TABLE_NAME,ALL_ENTRIES);
      uriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PetEntry.TABLE_NAME+"/*",SINGLE_ENTRIES);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new PetDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if(db!=null)
        return true;
        return false;
    }

    @Override
    public Cursor query( Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PetContract.PetEntry.TABLE_NAME);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int matcher = uriMatcher.match(uri);
        if(matcher==ALL_ENTRIES||matcher==SINGLE_ENTRIES)
            qb.setProjectionMap(values);
        if(sortOrder==null||sortOrder=="") sortOrder= PetContract.PetEntry.COLUMN_ID;
        Cursor cursor=null;
        if(matcher==SINGLE_ENTRIES)
        {
            long id = ContentUris.parseId(uri);
            selection =  PetContract.PetEntry._ID+" = ?";
            selectionArgs = new String[]{String.valueOf(id)};
            cursor = qb.query(db,projection,selection,selectionArgs,null,null,sortOrder);
            Log.v("check outPut","Single Entry");
        }
        else
        {
            cursor = qb.query(db,projection,selection,selectionArgs,null,null,sortOrder);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case SINGLE_ENTRIES:
                return PetContract.CONTENT_ITEM_TYPE;
            case ALL_ENTRIES:
                return PetContract.CONTENT_LIST_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri );
        }
    }

    @Nullable
    @Override
    public Uri insert( Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = db.insert(PetContract.PetEntry.TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(PetContract.PetEntry.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int delete( Uri uri, String selection, String[] selectionArgs) {
        int count=0;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(match==ALL_ENTRIES||match==SINGLE_ENTRIES){
            count = db.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
        }
        else
            throw new IllegalArgumentException("Unknown Uri: " + uri );
        if(count!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public int update( Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count=0;
        int match = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(match==ALL_ENTRIES||match==SINGLE_ENTRIES){
            count = db.update(PetContract.PetEntry.TABLE_NAME,values,selection,selectionArgs);
        }
        else
            throw new IllegalArgumentException("Unknown Uri: " + uri );
        if(count!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}

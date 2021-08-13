package com.example.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PetContract {

    public static String DATA_BASE_NAME = "shelter.db";
    public static int VERSION = 1;
    public static String CONTENT_AUTHORITY = "com.example.pets.data.PetProvider";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static String PET_PATH = "pets";

    public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PET_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PET_PATH;

    public static class PetEntry implements BaseColumns{

        public static Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PET_PATH);
        public static String TABLE_NAME = "pets";
        public static String COLUMN_ID = BaseColumns._ID;
        public static String COLUMN_NAME = "name";
        public static String COLUMN_BREED = "breed";
        public static String COLUMN_GENDER = "gender";
        public static String COLUMN_WEIGHT = "weight";

    }

}

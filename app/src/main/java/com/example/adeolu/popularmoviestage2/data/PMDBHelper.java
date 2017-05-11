package com.example.adeolu.popularmoviestage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.adeolu.popularmoviestage2.data.PMContract.FavoriteEntry;

/**
 * Created by ADEOLU on 5/11/2017.
 */

public class PMDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "favorite.DB";
    private static final int DATABASE_VERSION = 3;

    final String SQL_CREATE_FAVORITE_TABLE =  "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
            FavoriteEntry._ID  + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FavoriteEntry.COLUMN_ID  + " TEXT UNIQUE , " +
            FavoriteEntry.COLUMN_RELEASEDATE  + " TEXT NOT NULL, " +
            FavoriteEntry.COLUMN_TITLE  + " TEXT NOT NULL, " +
            FavoriteEntry.COLUMN_VOTEAVERAGE  + " TEXT NOT NULL, " +
            FavoriteEntry.COLUMN_OVERVIEW  + " TEXT NOT NULL, " +
            FavoriteEntry.COLUMN_IMG + " TEXT NOT NULL);";

    public PMDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db,oldVersion,newVersion);
    }
}

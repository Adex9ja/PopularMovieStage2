package com.example.adeolu.popularmoviestage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.adeolu.popularmoviestage2.data.PMContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by ADEOLU on 5/11/2017.
 */

public class PMContentProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int CODE_MOVIE = 100;
    private static final int CODE_MOVIE_ID = 101;

    private SQLiteDatabase db;
    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PMContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PMContract.PATH_FAVORITE, CODE_MOVIE);
        matcher.addURI(authority, PMContract.PATH_FAVORITE + "/*", CODE_MOVIE_ID);
        return matcher;
    }

    private PMDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new PMDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        db = mOpenHelper.getReadableDatabase();
        Cursor cursor = null;
        final int matchid = sUriMatcher.match(uri);
        switch (matchid){
            case CODE_MOVIE:
                try {
                    cursor = db.query(TABLE_NAME,null,null,null,null,null, PMContract.FavoriteEntry._ID);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case CODE_MOVIE_ID:
                try {
                    cursor = db.query(TABLE_NAME,null,null,null,null,null,null);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                throw new UnsupportedOperationException("error:" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case CODE_MOVIE:
                return "vnd.android.cursor.dir" + "/" + PMContract.CONTENT_AUTHORITY + "/" + PMContract.PATH_FAVORITE;
            case CODE_MOVIE_ID:
                return "vnd.android.cursor.item" + "/" + PMContract.CONTENT_AUTHORITY + "/" + PMContract.PATH_FAVORITE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues)  {
        db = mOpenHelper.getWritableDatabase();
        final int matchid = sUriMatcher.match(uri);
        Uri uriTorReturn;
        switch (matchid){
            case CODE_MOVIE:
                long ret = db.insert(TABLE_NAME,null,contentValues);
                if(ret > 0)
                    uriTorReturn = ContentUris.withAppendedId(PMContract.FavoriteEntry.CONTENT_URI,ret);
                else
                    uriTorReturn = null;
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return uriTorReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

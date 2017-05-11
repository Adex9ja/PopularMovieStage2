package com.example.adeolu.popularmoviestage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ADEOLU on 5/11/2017.
 */

public class PMContract {
    public static final String CONTENT_AUTHORITY = "com.example.adeolu.popularmoviestage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FAVORITE = "favorite";

    public final static class FavoriteEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITE)
                .build();

        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTEAVERAGE = "voteAverage";
        public static final String COLUMN_RELEASEDATE = "releasedate";
        public static final String COLUMN_IMG = "img";
    }
}


package com.example.adeolu.popularmoviestage2.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.example.adeolu.popularmoviestage2.R;

/**
 * Created by ADEOLU on 4/14/2017.
 */
public class MoviePreference {

    public static String getSortOrder(Context vcontext){
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(vcontext);
        return shared.getString(vcontext.getString(R.string.sorttype),vcontext.getString(R.string.popular));
    }

}

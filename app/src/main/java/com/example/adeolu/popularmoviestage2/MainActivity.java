package com.example.adeolu.popularmoviestage2;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


import com.example.adeolu.popularmoviestage2.utilities.JSONObjectUtil;
import com.example.adeolu.popularmoviestage2.utilities.MovieAdapter;
import com.example.adeolu.popularmoviestage2.utilities.MoviePreference;
import com.example.adeolu.popularmoviestage2.utilities.NetworkUtils;

import static com.example.adeolu.popularmoviestage2.data.PMContract.FavoriteEntry.CONTENT_URI;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickListener,
        LoaderManager.LoaderCallbacks<List<JSONObjectUtil.MovieJSONResponse>>, SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.movie_list)    RecyclerView movie_list;
    @BindView(R.id.pb_loading_indicator)    ProgressBar mLoadingIndicator;
    @BindView(R.id.tv_error_message_display)    TextView errMessage;
    @BindView(R.id.toolbar)    Toolbar toolbar;
    private MovieAdapter adapter;
    private ActionBar bar;
    private final int LOADER_ID = 1112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        bar = getSupportActionBar();

        adapter = new MovieAdapter(this,this);
        GridLayoutManager layout = new GridLayoutManager(this,2);
        movie_list.setLayoutManager(layout);
        movie_list.setHasFixedSize(true);
        movie_list.setAdapter(adapter);


        if(savedInstanceState == null)
             freshLoad();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovie();
    }
    private void freshLoad(){
        showMovieView();
        String sort = MoviePreference.getSortOrder(this);
        changeTitlebar(sort);
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.param), new String[]{sort, getString(R.string.apikey)});
        getSupportLoaderManager().initLoader(LOADER_ID,bundle,this);

    }
    private void loadMovie() {
        showMovieView();
        String sort = MoviePreference.getSortOrder(this);
        changeTitlebar(sort);
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.param), new String[]{sort, getString(R.string.apikey)});
        getSupportLoaderManager().restartLoader(LOADER_ID,bundle,this);
    }

    private void changeTitlebar(String sorttype) {
        if(sorttype.equals(getString(R.string.popular)))
            bar.setTitle(getString(R.string.titlepopular));
        else if(sorttype.equals(getString(R.string.toprated)))
            bar.setTitle(getString(R.string.titletoprated));
        else if(sorttype.equals(getString(R.string.favorite)))
            bar.setTitle(getString(R.string.titlefavorite));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

        if (id == R.id.nav_refresh)
            loadMovie();
        else if(id == R.id.nav_setting)
            startActivity(new Intent(this,SettingsActivity.class));
        return true;
    }
    @Override
    public void onclick(JSONObjectUtil.MovieJSONResponse moviedata) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(getString(R.string.movietitle),moviedata.getOriginal_title());
        intent.putExtra(getString(R.string.movieimg),moviedata.getPoster_path());
        intent.putExtra(getString(R.string.movieoverview),moviedata.getOverview());
        intent.putExtra(getString(R.string.movierating),moviedata.getVote_average());
        intent.putExtra(getString(R.string.moviereleasedate),moviedata.getRelease_date());
        intent.putExtra(getString(R.string.moviegenre),moviedata.getGenre_ids());
        intent.putExtra(getString(R.string.movieid),moviedata.getId());
        startActivity(intent);
    }

    @Override
    public Loader<List<JSONObjectUtil.MovieJSONResponse>> onCreateLoader(int id, Bundle args) {
        final String params[] = args.getStringArray(getString(R.string.param));
        return new AsyncTaskLoader<List<JSONObjectUtil.MovieJSONResponse>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }
            @Override
            public List<JSONObjectUtil.MovieJSONResponse> loadInBackground() {
                if (params.length == 0) {
                    return null;
                }

                String sortorder = params[0];
                String apikey = params[1];
                URL movieRequestURL = NetworkUtils.buildUrl(sortorder,apikey);

                try {
                    if(!sortorder.equals(getString(R.string.favorite))){
                        String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                        List<JSONObjectUtil.MovieJSONResponse> MovieJSONResponse = new JSONObjectUtil().getMovieListFromJSonResponse( movieResponse);
                        return MovieJSONResponse;
                    }
                    else{
                       Cursor cursor = getContentResolver().query(CONTENT_URI,null,null,null,null);
                        List<JSONObjectUtil.MovieJSONResponse> MovieJSONResponse = new JSONObjectUtil().getMovieListFromCursor( cursor);
                        return MovieJSONResponse;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<List<JSONObjectUtil.MovieJSONResponse>> loader, List<JSONObjectUtil.MovieJSONResponse> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            showMovieView();
            adapter.setMovieData(data);
        } else {
            showErrorView();
        }
    }
    @Override
    public void onLoaderReset(Loader<List<JSONObjectUtil.MovieJSONResponse>> loader) {

    }
    private void showErrorView() {
        movie_list.setVisibility(View.INVISIBLE);
        errMessage.setVisibility(View.VISIBLE);
    }
    private void showMovieView() {
        errMessage.setVisibility(View.INVISIBLE);
        movie_list.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        loadMovie();
    }
}

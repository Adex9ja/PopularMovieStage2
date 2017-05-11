package com.example.adeolu.popularmoviestage2;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeolu.popularmoviestage2.data.PMContract;
import com.example.adeolu.popularmoviestage2.utilities.JSONObjectUtil;
import com.example.adeolu.popularmoviestage2.utilities.ReviewAdapter;
import com.example.adeolu.popularmoviestage2.utilities.TrailerAdapter;
import com.squareup.picasso.Picasso;




import com.example.adeolu.popularmoviestage2.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.adeolu.popularmoviestage2.data.PMContract.FavoriteEntry.CONTENT_URI;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<JSONObjectUtil.TrailerJSonResponse>>,TrailerAdapter.TrailerAdapterOnClickListener, View.OnClickListener {
    private String title,img,overview,voteAverage,releasedate;
    private int id;
    @BindView(R.id.movie_releasedate) TextView  movie_releasedate;
    @BindView(R.id.movieoverview) TextView movieoverview;
    @BindView(R.id.movie_rating)  RatingBar  movie_rating;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.backdrop) ImageView backdrop;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;
    @BindView(R.id.pb_loading_indicator2) ProgressBar mLoadingIndicator2;
    @BindView(R.id.trailerlist)  RecyclerView trailerlist;
    @BindView(R.id.reviewlist) RecyclerView reviewlist;
    @BindView(R.id.btnreview) Button btnreview;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.tv_error_message_display)    TextView errMessage;


    private int[] genre;
    private final int TRAILER_ID = 113;
    private final int REVIEW_ID = 114;
    private Bundle savedInstanceBundle;
    private TrailerAdapter mAdapter;
    private ReviewAdapter mAdapterReview;
    private List<JSONObjectUtil.TrailerJSonResponse> trailerdata;
    private ActionBar bar;
    private BottomSheetBehavior bottomSheetBehavior;
    private int checkid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        savedInstanceBundle = savedInstanceState;

        LinearLayoutManager layout = new LinearLayoutManager(this);
        mAdapter = new TrailerAdapter(this,this);
        mAdapterReview = new ReviewAdapter(this);

        trailerlist.setLayoutManager(layout);
        trailerlist.setAdapter(mAdapter);

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));

         reviewlist.setLayoutManager(new LinearLayoutManager(this));
         reviewlist.setAdapter(mAdapterReview);

        btnreview.setOnClickListener(this);
        fab.setOnClickListener(this);

        getMovieDetailFromIntent();
        setMovieDetails();
        loadMovieTrailer();
    }

    private void loadMovieReview(){
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.moviedetials), new String[]{Integer.toString(id),getString(R.string.review)});
        if(savedInstanceBundle == null)
            getSupportLoaderManager().initLoader(REVIEW_ID,bundle,this);
        else
            getSupportLoaderManager().restartLoader(REVIEW_ID,bundle,this);
    }
    private void loadMovieTrailer() {
        Bundle bundle = new Bundle();
        bundle.putCharSequenceArray(getString(R.string.moviedetials), new String[]{Integer.toString(id),getString(R.string.video)});
        if(savedInstanceBundle == null)
            getSupportLoaderManager().initLoader(TRAILER_ID,bundle,this);
        else
            getSupportLoaderManager().restartLoader(TRAILER_ID,bundle,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovieTrailer();
    }

    private void getMovieDetailFromIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra(getString(R.string.movietitle))){
            title = intent.getStringExtra(getString(R.string.movietitle));
            img = intent.getStringExtra(getString(R.string.movieimg));
            releasedate = intent.getStringExtra(getString(R.string.moviereleasedate));
            voteAverage = intent.getStringExtra(getString(R.string.movierating));
            overview = intent.getStringExtra(getString(R.string.movieoverview));
            genre = intent.getIntArrayExtra(getString(R.string.moviegenre));
            id = intent.getIntExtra(getString(R.string.movieid),0);
        }
    }
    private void setMovieDetails() {
        Picasso.with(this)
                .load(NetworkUtils.PIXURL + img)
                .placeholder(android.R.drawable.alert_dark_frame)
                .error(android.R.drawable.alert_dark_frame)
                .into(backdrop);
        movie_rating.setRating((Float.parseFloat(voteAverage)/10) * 4);
        movie_rating.setIsIndicator(true);
        movie_releasedate.setText(releasedate);
        movieoverview.setText(overview);
        bar.setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_favorite:
                addToFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addToFavorite() {
        ContentValues values = new ContentValues();
        values.put(PMContract.FavoriteEntry.COLUMN_ID,id );
        values.put(PMContract.FavoriteEntry.COLUMN_OVERVIEW,overview );
        values.put(PMContract.FavoriteEntry.COLUMN_IMG,img );
        values.put(PMContract.FavoriteEntry.COLUMN_RELEASEDATE,releasedate );
        values.put(PMContract.FavoriteEntry.COLUMN_TITLE,title );
        values.put(PMContract.FavoriteEntry.COLUMN_VOTEAVERAGE,voteAverage );


        Uri uri = getContentResolver().insert(CONTENT_URI,values);
        if(uri != null)
            Toast.makeText(this,"Successfully added to favorite!",Toast.LENGTH_SHORT ).show();
        else
            Toast.makeText(this,"Already exist in favorite!",Toast.LENGTH_SHORT ).show();
    }


    @Override
    public Loader<List<JSONObjectUtil.TrailerJSonResponse>> onCreateLoader( int id, Bundle args) {
        final String params [] = args.getStringArray(getString(R.string.moviedetials));
        checkid = id;
        return new AsyncTaskLoader<List<JSONObjectUtil.TrailerJSonResponse>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(checkid == TRAILER_ID)
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                else
                    mLoadingIndicator2.setVisibility(View.VISIBLE);
                forceLoad();
            }
            @Override
            public List<JSONObjectUtil.TrailerJSonResponse> loadInBackground() {
                if (params.length == 0) {
                    return null;
                }
                List<JSONObjectUtil.TrailerJSonResponse> jsonResponse = null;
                String movieid = params[0];
                String type = params[1];
                URL movieRequestURL = NetworkUtils.buildVideoReviewUrl(movieid,type,getString(R.string.apikey));
                switch (checkid){
                    case TRAILER_ID:
                        try {
                            String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                            jsonResponse = new JSONObjectUtil().getTrailerListFromJSonResponse( movieResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case REVIEW_ID:
                        try {
                            String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestURL);
                            jsonResponse = new JSONObjectUtil().getReviewListFromJSonResponse( movieResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                return jsonResponse;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<JSONObjectUtil.TrailerJSonResponse>> loader, List<JSONObjectUtil.TrailerJSonResponse> data) {
        if(checkid == TRAILER_ID){
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            trailerdata = data;
            mAdapter.setTrailerlistData(trailerdata);
        }

        else{
            mLoadingIndicator2.setVisibility(View.INVISIBLE);
            trailerdata = data;
            if(trailerdata != null && trailerdata.size() >0)
                showReview(trailerdata);
            else
                showErrorView();
        }


    }

    @Override
    public void onLoaderReset(Loader<List<JSONObjectUtil.TrailerJSonResponse>> loader) {

    }

    @Override
    public void onclick(JSONObjectUtil.TrailerJSonResponse response) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + response.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + response.getKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btnreview){
            loadMovieReview();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else if(view == fab)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
    private void showErrorView() {
        reviewlist.setVisibility(View.INVISIBLE);
        errMessage.setVisibility(View.VISIBLE);
    }
    private void showReview(List<JSONObjectUtil.TrailerJSonResponse> trailerdata) {
        errMessage.setVisibility(View.INVISIBLE);
        reviewlist.setVisibility(View.VISIBLE);
        mAdapterReview.setReviewListData(trailerdata);
    }


}

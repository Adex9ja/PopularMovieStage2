package com.example.adeolu.popularmoviestage2.utilities;

import android.database.Cursor;

import com.example.adeolu.popularmoviestage2.data.PMContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADEOLU on 4/14/2017.
 */
public class JSONObjectUtil {
    private static final String RESULT = "results";
    private static final String ID = "id";
    private static final String KEY = "key";
    private static final String BACKDROP = "backdrop_path";
    private static final String GENEREID = "genre_ids";
    private static final String ORIGINALTITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String POSTERPATH = "poster_path";
    private static final String RELEASEDATE = "release_date";
    private static final String VOTEAVERAGE = "vote_average";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";

    private  List<MovieJSONResponse> parsedMovies;
    private MovieJSONResponse movieresponse;

    private List<TrailerJSonResponse>parsedTrailer;
    private TrailerJSonResponse trailerresponse;






    public  List<MovieJSONResponse> getMovieListFromJSonResponse(String jsonResponse) throws JSONException {
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray data = object.getJSONArray(RESULT);
        parsedMovies = new ArrayList<>();
        for(int i= 0; i<data.length(); i++){
            JSONObject moviedata = (JSONObject)data.get(i);
            movieresponse = new MovieJSONResponse();

            movieresponse.setId(moviedata.getInt(ID));
            movieresponse.setBackdrop_path(moviedata.getString(BACKDROP));
            movieresponse.setOriginal_title(moviedata.getString(ORIGINALTITLE));
            movieresponse.setOverview(moviedata.getString(OVERVIEW));
            movieresponse.setPoster_path(moviedata.getString(POSTERPATH));
            movieresponse.setRelease_date(moviedata.getString(RELEASEDATE));
            movieresponse.setVote_average(moviedata.getString(VOTEAVERAGE));



            JSONArray genre = moviedata.getJSONArray(GENEREID);
            int [] temp = new int[genre.length()];
            for(int j=0; j<genre.length();j++){
                temp[j] = genre.getInt(j);
            }

            movieresponse.setGenre_ids(temp);

            parsedMovies.add(movieresponse);

        }

        return  parsedMovies;
    }
    public  List<TrailerJSonResponse> getTrailerListFromJSonResponse(String jsonResponse) throws JSONException {
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray data = object.getJSONArray(RESULT);
        parsedTrailer = new ArrayList<>();
        for(int i= 0; i<data.length(); i++){
            JSONObject trailerdata = (JSONObject)data.get(i);
            trailerresponse = new TrailerJSonResponse();

            trailerresponse.setId(trailerdata.getString(ID));
            trailerresponse.setKey(trailerdata.getString(KEY));
            parsedTrailer.add(trailerresponse);
        }
        return  parsedTrailer;
    }
    public  List<TrailerJSonResponse> getReviewListFromJSonResponse(String jsonResponse) throws JSONException {
        JSONObject object = new JSONObject(jsonResponse);
        JSONArray data = object.getJSONArray(RESULT);
        parsedTrailer = new ArrayList<>();
        for(int i= 0; i<data.length(); i++){
            JSONObject reviewdata = (JSONObject)data.get(i);
            trailerresponse = new TrailerJSonResponse();

            trailerresponse.setAuthor(reviewdata.getString(AUTHOR));
            trailerresponse.setContent(reviewdata.getString(CONTENT));
            parsedTrailer.add(trailerresponse);
        }
        return  parsedTrailer;
    }
    public  List<MovieJSONResponse> getMovieListFromCursor(Cursor cursor) throws JSONException {
        parsedMovies = new ArrayList<>();
        while (cursor.moveToNext()){
            movieresponse = new MovieJSONResponse();
            movieresponse.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PMContract.FavoriteEntry.COLUMN_ID)));
            movieresponse.setOriginal_title(cursor.getString(cursor.getColumnIndexOrThrow(PMContract.FavoriteEntry.COLUMN_TITLE)));
            movieresponse.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(PMContract.FavoriteEntry.COLUMN_OVERVIEW)));
            movieresponse.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(PMContract.FavoriteEntry.COLUMN_IMG)));
            movieresponse.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(PMContract.FavoriteEntry.COLUMN_RELEASEDATE)));
            movieresponse.setVote_average(cursor.getString(cursor.getColumnIndexOrThrow(PMContract.FavoriteEntry.COLUMN_VOTEAVERAGE)));

            parsedMovies.add(movieresponse);

        }

        return  parsedMovies;
    }

    public class MovieJSONResponse{
        private String original_title;
        private String poster_path;
        private String overview;
        private String vote_average;
        private String release_date;
        private int id;
        private int [] genre_ids;
        private String backdrop_path;

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getVote_average() {
            return vote_average;
        }

        public void setVote_average(String vote_average) {
            this.vote_average = vote_average;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public int[] getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(int[] genre_ids) {
            this.genre_ids = genre_ids;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }



    }
    public class TrailerJSonResponse{
        private String id;
        private String key;
        private String author;
        private String content;
        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
        private String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }


    }

}

package com.tachyonlabs.popularmoviesstage2.utilities;

import com.tachyonlabs.popularmoviesstage2.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class TmdbJsonUtils {
    private static final String TAG = TmdbJsonUtils.class.getSimpleName();

    public static Movie[] getPosterUrlsFromJson(Context context, String moviesJsonStr) throws JSONException {
        // Create an array of Movie objects using the TMDb JSON data
        JSONObject movieDataJson = new JSONObject(moviesJsonStr);

        JSONArray resultsArray = movieDataJson.getJSONArray("results");

        Movie[] movies = new Movie[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            Movie movie = new Movie();
            JSONObject result = resultsArray.getJSONObject(i);
            movie.setTitle(result.getString("title"));
            movie.setOverview(result.getString("overview"));
            movie.setPosterUrl(result.getString("poster_path"));
            movie.setReleaseDate(result.getString("release_date"));
            movie.setUserRating(String.valueOf(result.getDouble("vote_average")));
            movies[i] = movie;
        }
        return movies;
    }
}

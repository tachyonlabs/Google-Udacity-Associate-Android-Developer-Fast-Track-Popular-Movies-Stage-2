package com.tachyonlabs.popularmoviesstage2.utilities;

import com.tachyonlabs.popularmoviesstage2.models.Movie;
import com.tachyonlabs.popularmoviesstage2.models.Review;

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
            movie.setId(String.valueOf((int) result.getDouble("id")));
            movies[i] = movie;
        }
        return movies;
    }

    public static Review[] getReviewsFromJson(Context context, String reviewsJsonStr) throws JSONException {
        // Create an array of Movie objects using the TMDb JSON data
        JSONObject reviewDataJson = new JSONObject(reviewsJsonStr);

        JSONArray resultsArray = reviewDataJson.getJSONArray("results");

        Review[] reviews = new Review[resultsArray.length()];

        for (int i = 0; i < resultsArray.length(); i++) {
            Review review = new Review();
            JSONObject result = resultsArray.getJSONObject(i);
            review.setAuthor(result.getString("author"));
            review.setContent(result.getString("content"));
            reviews[i] = review;
        }
        return reviews;
    }
}

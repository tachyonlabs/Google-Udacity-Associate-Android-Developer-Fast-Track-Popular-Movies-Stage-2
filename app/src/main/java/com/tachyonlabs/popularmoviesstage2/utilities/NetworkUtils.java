package com.tachyonlabs.popularmoviesstage2.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    final static String API_KEY_PARAM = "api_key";
    final static String API_APPEND_TO_RESPONSE_PARAM = "append_to_response";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String TMDB_MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";

    public static URL buildMoviesUrl(String sortOrder, String tmdbApiKey) {
        // build URL to return TMDb data in popular or top-rated sort order as desired
        Uri builtUri = Uri.parse(TMDB_MOVIES_BASE_URL + sortOrder).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, tmdbApiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, url.toString());
        return url;
    }

    public static URL buildTrailersAndReviewsUrl(String movieID, String tmdbApiKey) {
        // build URL to return reviews for selected movie ID
        Uri builtUri = Uri.parse(TMDB_MOVIES_BASE_URL + movieID).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, tmdbApiKey)
                .appendQueryParameter(API_APPEND_TO_RESPONSE_PARAM, "reviews,videos")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, url.toString());
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        // get TMDb JSON data
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

package com.tachyonlabs.popularmoviesstage2.utilities;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        // get TMDb JSON data

        // set the connection timeout to 5 seconds and the read timeout to 10 seconds
        // see https://eventuallyconsistent.net/2011/08/02/working-with-urlconnection-and-timeouts/
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(5000);
        urlConnection.setReadTimeout(10000);
        try {

            // get a stream to read data from
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

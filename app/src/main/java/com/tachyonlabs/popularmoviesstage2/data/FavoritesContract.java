package com.tachyonlabs.popularmoviesstage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.tachyonlabs.popularmoviesstage2";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "favorites" directory
    public static final String PATH_FAVORITES = "favorites";

    /* Favorite is an inner class that defines the contents of the favorites table */
    public static final class Favorite implements BaseColumns {

        // Favorite content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        // Favorites table and column names
        public static final String TABLE_NAME = "favorites";

        // Since Favorite implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUMN_MOVIE_POSTER_URL = "movie_poster_url";
        public static final String COLUMN_MOVIE_USER_RATING = "movie_user_rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
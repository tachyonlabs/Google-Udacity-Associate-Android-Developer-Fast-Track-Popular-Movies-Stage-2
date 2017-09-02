package com.tachyonlabs.popularmoviesstage2.data;

import com.tachyonlabs.popularmoviesstage2.data.FavoritesContract.Favorite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "favoritesDb.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 1;

    // Constructor
    FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the favorites database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create favorites table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE " + Favorite.TABLE_NAME + " (" +
                Favorite._ID + " INTEGER PRIMARY KEY, " +
                Favorite.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                Favorite.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                Favorite.COLUMN_MOVIE_POSTER_URL + " TEXT NOT NULL, " +
                Favorite.COLUMN_MOVIE_USER_RATING + " TEXT NOT NULL, " +
                Favorite.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                Favorite.COLUMN_MOVIE_ID + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     *
     * I know I should not just drop the table in production code, but I also know that what
     * upgrade code would go here would depend on what changes there were when there actually
     * *were* changes and users to be affected by them. :-) My reviewer gave me this link
     * https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
     * which was very good and which I have bookmarked for the future.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Favorite.TABLE_NAME);
        onCreate(db);
    }
}

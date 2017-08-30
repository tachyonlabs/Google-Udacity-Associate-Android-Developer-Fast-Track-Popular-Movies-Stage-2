package com.tachyonlabs.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.tachyonlabs.popularmoviesstage2.data.FavoritesContract.Favorite.TABLE_NAME;

public class FavoritesContentProvider extends ContentProvider {

    // Define final integer constants for the directory of favorites and a single item.
    // It's convention to use 100, 200, 300, etc for directories,
    // and related ints (101, 102, ..) for items in that directory.
    public static final int FAVORITES = 100;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // Define a static buildUriMatcher method that associates URI's with their int match
    // Member variable for a FavoritesDbHelper that's initialized in the onCreate() method
    private FavoritesDbHelper mFavoritesDbHelper;

    /**
     * Initialize a new matcher object without any matches,
     * then use .addURI(String authority, String path, int match) to add matches
     */
    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the favorites directory and a single item by ID.
         */
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES, FAVORITES);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        // Complete onCreate() and initialize a FavoritesDbHelper on startup
        Context context = getContext();
        mFavoritesDbHelper = new FavoritesDbHelper(context);
        return true;
    }

    // Implement insert to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the favorites database (to write new data to)
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the favorites directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAVORITES:
                // Inserting favorited movie into favorites table
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.Favorite.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = mFavoritesDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query to see if a movie has been favorited
            case FAVORITES:
                retCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int favoritesDeleted;

        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVORITES:
                favoritesDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // COMPLETED (3) Notify the resolver of a change and return the number of items deleted
        if (favoritesDeleted != 0) {
            // A favorite was unfavorited/deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of favorites deleted
        return favoritesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

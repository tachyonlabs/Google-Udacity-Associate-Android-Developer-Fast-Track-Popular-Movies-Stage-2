package com.tachyonlabs.popularmoviesstage2;

import com.tachyonlabs.popularmoviesstage2.PosterAdapter.PosterAdapterOnClickHandler;
import com.tachyonlabs.popularmoviesstage2.data.FavoritesContract;
import com.tachyonlabs.popularmoviesstage2.databinding.ActivityMainBinding;
import com.tachyonlabs.popularmoviesstage2.models.Movie;
import com.tachyonlabs.popularmoviesstage2.utilities.NetworkUtils;
import com.tachyonlabs.popularmoviesstage2.utilities.TmdbJsonUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements PosterAdapterOnClickHandler {
    public static final String SORT_ORDER_FAVORITES = "favorites";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SORT_ORDER_POPULAR = "popular";
    private static final String SORT_ORDER_TOP_RATED = "top_rated";
    ActivityMainBinding mBinding;
    private String API_KEY;
    private RecyclerView mRecyclerView;
    private com.tachyonlabs.popularmoviesstage2.PosterAdapter mPosterAdapter;
    private TextView tvErrorMessageDisplay;
    private ProgressBar pbLoadingIndicator;
    private String sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        API_KEY = getApiKey();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set up recyclerview and adapter to display the posters
        mRecyclerView = mBinding.rvPosters;
        tvErrorMessageDisplay = mBinding.tvErrorMessageDisplay;
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mPosterAdapter = new com.tachyonlabs.popularmoviesstage2.PosterAdapter(this);
        mRecyclerView.setAdapter(mPosterAdapter);

        // display progress bar, and load and display posters in preferred sort order
        pbLoadingIndicator = mBinding.pbLoadingIndicator;
        sortOrder = getSortOrderSetting();
        loadPosters(sortOrder);
    }

    public String getSortOrderSetting() {
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        return mSettings.getString("sortOrder", SORT_ORDER_POPULAR);
    }

    public void saveSortOrderSetting(String newSortOrder) {
        SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("sortOrder", newSortOrder);
        editor.apply();
    }

    @Override
    public void onClick(Movie movie) {
        // tapping a poster brings up DetailActivity with details on the movie
        Intent intent = new Intent(this, com.tachyonlabs.popularmoviesstage2.DetailActivity.class);
        intent.putExtra("movie", movie);
        intent.putExtra("posters_base_url", com.tachyonlabs.popularmoviesstage2.PosterAdapter.POSTERS_BASE_URL);
        intent.putExtra("poster_width", com.tachyonlabs.popularmoviesstage2.PosterAdapter.POSTER_WIDTH);
        intent.putExtra("tmdb_api_key", API_KEY);
        intent.putExtra("sortorder", sortOrder);
        startActivity(intent);
    }

    private void loadPosters(String sortOrder) {
        showPosters();
        if (sortOrder.equals(SORT_ORDER_FAVORITES)) {
            fetchFavoritesPosters();
        } else {
            new FetchPopularOrTopRatedPostersTask().execute(sortOrder);
        }
    }

    public void fetchFavoritesPosters() {
        // Does a query against the table and returns a Cursor object
        Cursor mCursor = getContentResolver().query(FavoritesContract.Favorite.CONTENT_URI, null, null, null, null);                       // The sort order for the returned rows

        if (mCursor == null || mCursor.getCount() == 0) {
            showErrorMessage(getString(R.string.no_favorites_yet));
        } else {
            Movie[] movies = new Movie[mCursor.getCount()];
            int index = 0;
            while (mCursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setTitle(mCursor.getString(mCursor.getColumnIndex(FavoritesContract.Favorite.COLUMN_MOVIE_TITLE)));
                movie.setOverview(mCursor.getString(mCursor.getColumnIndex(FavoritesContract.Favorite.COLUMN_MOVIE_OVERVIEW)));
                movie.setPosterUrl(mCursor.getString(mCursor.getColumnIndex(FavoritesContract.Favorite.COLUMN_MOVIE_POSTER_URL)));
                movie.setUserRating(mCursor.getString(mCursor.getColumnIndex(FavoritesContract.Favorite.COLUMN_MOVIE_USER_RATING)));
                movie.setReleaseDate(mCursor.getString(mCursor.getColumnIndex(FavoritesContract.Favorite.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setId(mCursor.getString(mCursor.getColumnIndex(FavoritesContract.Favorite.COLUMN_MOVIE_ID)));
                movies[index] = movie;
                index++;
            }
            mPosterAdapter.setPosterData(movies);
        }
        mCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu, with the settings action
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // bring up the settings dialog if the settings menu option is selected
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            settingsMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void settingsMenu() {
        // the settings dialog lets you select betwen popular, top-rated, and favorites poster display sort orders
        final String previousSortOrder = sortOrder;
        int currentSetting = 0;
        if (sortOrder.equals(SORT_ORDER_POPULAR)) {
            currentSetting = 0;
        } else if (sortOrder.equals(SORT_ORDER_TOP_RATED)) {
            currentSetting = 1;
        } else if (sortOrder.equals(SORT_ORDER_FAVORITES)) {
            currentSetting = 2;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // show radio buttons for the three options, with the current option selected
        builder.setTitle("Select poster sort order").setSingleChoiceItems(R.array.sort_orders, currentSetting,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AlertDialog alert = (AlertDialog) dialog;
                int selectedPosition = alert.getListView().getCheckedItemPosition();
                sortOrder = new String[]{SORT_ORDER_POPULAR, SORT_ORDER_TOP_RATED, SORT_ORDER_FAVORITES}[selectedPosition];
                // if they changed the sort order and clicked OK, update both preferences and the display
                if (!sortOrder.equals(previousSortOrder)) {
                    saveSortOrderSetting(sortOrder);
                    loadPosters(sortOrder);
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if they canceled, don't do anything
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showPosters() {
        tvErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMessage) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        tvErrorMessageDisplay.setText(errorMessage);
        tvErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    // get the TMDb API key from AndroidManifest.xml
    public String getApiKey() {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("TMDB_API_KEY");

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return null;
    }

    // using a background task, get the TMDb data and display the posters
    public class FetchPopularOrTopRatedPostersTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            String tmdbApiKey = getApiKey();
            String movieSortOrder = params[0];

            URL postersRequestUrl = NetworkUtils.buildMoviesUrl(movieSortOrder, tmdbApiKey);

            try {
                String jsonTmdbResponse = NetworkUtils.getResponseFromHttpUrl(postersRequestUrl);

                Movie[] moviesFromJson = TmdbJsonUtils.getPosterUrlsFromJson(MainActivity.this, jsonTmdbResponse);

                return moviesFromJson;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showPosters();
                mPosterAdapter.setPosterData(movies);
            } else {
                showErrorMessage(getString(R.string.no_data_received));
            }
        }
    }

}

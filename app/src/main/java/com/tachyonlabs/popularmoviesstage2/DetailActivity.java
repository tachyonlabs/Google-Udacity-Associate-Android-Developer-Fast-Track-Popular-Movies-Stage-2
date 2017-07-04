package com.tachyonlabs.popularmoviesstage2;

import com.squareup.picasso.Picasso;
import com.tachyonlabs.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.tachyonlabs.popularmoviesstage2.models.Movie;
import com.tachyonlabs.popularmoviesstage2.models.Review;
import com.tachyonlabs.popularmoviesstage2.utilities.NetworkUtils;
import com.tachyonlabs.popularmoviesstage2.utilities.TmdbJsonUtils;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();
    ActivityDetailBinding mBinding;
    private RecyclerView mRecyclerView;
    private com.tachyonlabs.popularmoviesstage2.ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // set up recyclerview and adapter to display the reviews
        mRecyclerView = mBinding.rvReviews;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mReviewAdapter = new com.tachyonlabs.popularmoviesstage2.ReviewAdapter();
        mRecyclerView.setAdapter(mReviewAdapter);

        TextView tvTitle = mBinding.tvTitle;
        ImageView ivThumbnail = mBinding.ivThumbnail;
        TextView tvOverview = mBinding.tvOverview;
        TextView tvRating = mBinding.tvRating;

        Intent callingIntent = getIntent();

        if (callingIntent.hasExtra("movie")) {
            Movie movie = callingIntent.getParcelableExtra("movie");
            String postersBaseUrl = callingIntent.getStringExtra("posters_base_url");
            String posterWidth = callingIntent.getStringExtra("poster_width");

            String titleAndYear = String.format(getString(R.string.movie_title_and_year), movie.getTitle(), movie.getReleaseDate().substring(0,4));
            String rating = String.format(getString(R.string.movie_rating), movie.getUserRating());
            String id = movie.getId();
            String tmdbApiKey = callingIntent.getStringExtra("tmdb_api_key");
            tvTitle.setText(titleAndYear);
            tvRating.setText(rating);
            // Picasso caches images, so it should not cause extra work/bandwidth to give it the
            // same poster URL used in the main activity
            Picasso.with(this).load(postersBaseUrl + posterWidth + movie.getPosterUrl()).into(ivThumbnail);
            tvOverview.setText(movie.getOverview());
            loadReviews(id, tmdbApiKey);
        }
    }

    private void loadReviews(String movieId, String apiKey) {
        //showPosters();
        new FetchReviewsTask().execute(movieId, apiKey, null);
    }

    // using a background task, get the TMDb data and display the posters
    public class FetchReviewsTask extends AsyncTask<String, String, Review[]> {//Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Review[] doInBackground(String... params) {
            URL reviewsRequestUrl = NetworkUtils.buildReviewssUrl(params[0], params[1]);

            try {
                String jsonTmdbResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);

                Review[] reviewsFromJson = TmdbJsonUtils.getReviewsFromJson(DetailActivity.this, jsonTmdbResponse);
                for (int i = 0; i < reviewsFromJson.length; i++) {
                    Log.d(TAG, reviewsFromJson[i].getAuthor());
                }

                return reviewsFromJson;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "error");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            //pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (reviews != null) {
                //showPosters();
                mReviewAdapter.setReviewData(reviews);
            } else {
                //showErrorMessage("No data was received - please check your Internet connection and try again.");
            }
        }
    }

}

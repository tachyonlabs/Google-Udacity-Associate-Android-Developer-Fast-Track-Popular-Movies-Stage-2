package com.tachyonlabs.popularmoviesstage2;

import com.squareup.picasso.Picasso;
import com.tachyonlabs.popularmoviesstage2.databinding.ActivityDetailBinding;
import com.tachyonlabs.popularmoviesstage2.models.Movie;
import com.tachyonlabs.popularmoviesstage2.models.Review;
import com.tachyonlabs.popularmoviesstage2.models.Trailer;
import com.tachyonlabs.popularmoviesstage2.utilities.NetworkUtils;
import com.tachyonlabs.popularmoviesstage2.utilities.TmdbJsonUtils;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {
    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String YOUTUBE_VIDEO_BASE_URL = "https://www.youtube.com/watch?v=";
    ActivityDetailBinding mBinding;
    private RecyclerView mTrailersRecyclerView;
    private com.tachyonlabs.popularmoviesstage2.TrailerAdapter mTrailerAdapter;
    private RecyclerView mReviewsRecyclerView;
    private com.tachyonlabs.popularmoviesstage2.ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        // set up recyclerview and adapter to display the trailers
        mTrailersRecyclerView = mBinding.rvTrailers;
        LinearLayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        mTrailersRecyclerView.setLayoutManager(trailersLayoutManager);
        mTrailerAdapter = new com.tachyonlabs.popularmoviesstage2.TrailerAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailerAdapter);

        // set up recyclerview and adapter to display the reviews
        mReviewsRecyclerView = mBinding.rvReviews;
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(reviewsLayoutManager);
        mReviewAdapter = new com.tachyonlabs.popularmoviesstage2.ReviewAdapter();
        mReviewsRecyclerView.setAdapter(mReviewAdapter);

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
            loadTrailersAndReviews(id, tmdbApiKey);
        }
    }

    private void loadTrailersAndReviews(String movieId, String apiKey) {
        //showPosters();
        new FetchTrailersAndReviewsTask().execute(movieId, apiKey, null);
    }

    @Override
    public void onClick(Trailer trailer) {
        // tapping a trailer thumbnail brings up the trailer
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_VIDEO_BASE_URL + trailer.getKey())));
    }

    // using a background task, get the reviews and trailers data and display it
    public class FetchTrailersAndReviewsTask extends AsyncTask<String, String, List<Object>> {//Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pbLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Object> doInBackground(String... params) {
            URL trailersAndReviewsRequestUrl = NetworkUtils.buildTrailersAndReviewsUrl(params[0], params[1]);
            Log.d(TAG, trailersAndReviewsRequestUrl.toString());
            try {
                String jsonTmdbResponse = NetworkUtils.getResponseFromHttpUrl(trailersAndReviewsRequestUrl);

                Review[] reviewsFromJson = TmdbJsonUtils.getReviewsFromJson(DetailActivity.this, jsonTmdbResponse);
                for (int i = 0; i < reviewsFromJson.length; i++) {
                    Log.d(TAG, reviewsFromJson[i].getAuthor());
                }

                Trailer[] trailersFromJson = TmdbJsonUtils.getTrailersFromJson(DetailActivity.this, jsonTmdbResponse);

                List<Object> reviewsAndTrailers = new ArrayList<Object>();
                reviewsAndTrailers.add(reviewsFromJson);
                reviewsAndTrailers.add(trailersFromJson);

                return reviewsAndTrailers;

            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "error");
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Object> reviewsAndTrailers) {
            //pbLoadingIndicator.setVisibility(View.INVISIBLE);
            if (reviewsAndTrailers != null) {
                //showPosters();
                Review[] reviews = (Review[]) reviewsAndTrailers.get(0);
                mReviewAdapter.setReviewData(reviews);

                Trailer[] trailers = (Trailer[]) reviewsAndTrailers.get(1);
                mTrailerAdapter.setTrailerData(trailers);

            } else {
                //showErrorMessage("No data was received - please check your Internet connection and try again.");
            }
        }
    }

}

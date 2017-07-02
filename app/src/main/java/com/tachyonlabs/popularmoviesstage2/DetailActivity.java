package com.tachyonlabs.popularmoviesstage2;

import com.squareup.picasso.Picasso;
import com.tachyonlabs.popularmoviesstage2.models.Movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        ImageView ivThumbnail = (ImageView) findViewById(R.id.iv_thumbnail);
        TextView tvOverview = (TextView) findViewById(R.id.tv_overview);
        TextView tvRating = (TextView) findViewById(R.id.tv_rating);

        Intent callingIntent = getIntent();

        if (callingIntent.hasExtra("movie")) {
            Movie movie = callingIntent.getParcelableExtra("movie");
            String postersBaseUrl = callingIntent.getStringExtra("posters_base_url");
            String posterWidth = callingIntent.getStringExtra("poster_width");

            String titleAndYear = String.format(getString(R.string.movie_title_and_year), movie.getTitle(), movie.getReleaseDate().substring(0,4));
            String rating = String.format(getString(R.string.movie_rating), movie.getUserRating());

            tvTitle.setText(titleAndYear);
            tvRating.setText(rating);
            // Picasso caches images, so it should not cause extra work/bandwidth to give it the
            // same poster URL used in the main activity
            Picasso.with(this).load(postersBaseUrl + posterWidth + movie.getPosterUrl()).into(ivThumbnail);
            tvOverview.setText(movie.getOverview());
        }
    }
}

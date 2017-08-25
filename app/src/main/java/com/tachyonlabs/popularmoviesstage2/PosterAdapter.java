package com.tachyonlabs.popularmoviesstage2;

import com.squareup.picasso.Picasso;
import com.tachyonlabs.popularmoviesstage2.models.Movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterAdapterViewHolder> {
    public static final String POSTERS_BASE_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_WIDTH = "w185/";
    private static final String TAG = PosterAdapter.class.getSimpleName();
    final private PosterAdapterOnClickHandler mClickHandler;
    private Movie[] mMovies;

    public PosterAdapter(PosterAdapterOnClickHandler posterAdapterOnClickHandler) {
        mClickHandler = posterAdapterOnClickHandler;
    }

    @Override
    public PosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        PosterAdapterViewHolder viewHolder = new PosterAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PosterAdapterViewHolder holder, int position) {
        String posterUrl = POSTERS_BASE_URL + POSTER_WIDTH + mMovies[position].getPosterUrl();
        // TODO make an error image later too
        Picasso.with(holder.ivPoster.getContext())
                .load(posterUrl)
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        if (mMovies == null) {
            return 0;
        } else {
            return mMovies.length;
        }
    }

    public void setPosterData(Movie[] movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public interface PosterAdapterOnClickHandler {
        void onClick(Movie clickedItem);
    }

    public class PosterAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView ivPoster;

        public PosterAdapterViewHolder(View itemView) {
            super(itemView);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie movie = mMovies[getAdapterPosition()];
            mClickHandler.onClick(movie);
        }
    }
}

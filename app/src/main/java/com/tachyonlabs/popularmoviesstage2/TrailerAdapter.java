package com.tachyonlabs.popularmoviesstage2;

import com.squareup.picasso.Picasso;
import com.tachyonlabs.popularmoviesstage2.models.Trailer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    public static final String YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    public static final String YOUTUBE_DEFAULT_THUMBNAIL = "/default.jpg";
    final private TrailerAdapterOnClickHandler mClickHandler;
    private Trailer[] mTrailers;

    public TrailerAdapter(TrailerAdapterOnClickHandler trailerAdapterOnClickHandler) {
        mClickHandler = trailerAdapterOnClickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailerAdapterViewHolder viewHolder = new TrailerAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        String thumbnailUrl = YOUTUBE_THUMBNAIL_BASE_URL + mTrailers[position].getKey() + YOUTUBE_DEFAULT_THUMBNAIL;
        Log.d(TAG, thumbnailUrl);
        Picasso.with(holder.ivTrailerThumbnail.getContext())
                .load(thumbnailUrl)
                .placeholder(R.drawable.trailer_placeholder)
                .into(holder.ivTrailerThumbnail);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) {
            return 0;
        } else {
            return mTrailers.length;
        }
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer clickedItem);
    }

    public void setTrailerData(Trailer[] trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView ivTrailerThumbnail;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            ivTrailerThumbnail = (ImageView) itemView.findViewById(R.id.iv_trailer_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Trailer trailer = mTrailers[getAdapterPosition()];
            mClickHandler.onClick(trailer);
        }
    }
}

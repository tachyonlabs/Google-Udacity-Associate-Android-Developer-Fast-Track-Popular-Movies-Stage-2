package com.tachyonlabs.popularmoviesstage2;

import com.tachyonlabs.popularmoviesstage2.models.Review;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private Review[] mReviews;

    public ReviewAdapter() {
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewAdapterViewHolder viewHolder = new ReviewAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.tvReviewAuthor.setText(mReviews[position].getAuthor());
        holder.tvReviewContent.setText(mReviews[position].getContent());
    }

    @Override
    public int getItemCount() {
        if (mReviews == null) {
            return 0;
        } else {
            return mReviews.length;
        }
    }

    public void setReviewData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvReviewAuthor;
        public final TextView tvReviewContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            tvReviewAuthor = (TextView) itemView.findViewById(R.id.tv_review_author);
            tvReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        }
    }
}

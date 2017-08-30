package com.tachyonlabs.popularmoviesstage2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    String title;
    String overview;
    String posterUrl;
    String userRating;
    String releaseDate;
    String id;

    private Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        posterUrl = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
        id = in.readString();
    }

    public Movie() {
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(overview);
        parcel.writeString(posterUrl);
        parcel.writeString(userRating);
        parcel.writeString(releaseDate);
        parcel.writeString(id);
    }
}

package com.riders.thelab.data.local.model;

import com.riders.thelab.data.local.bean.MovieEnum;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Parcel
public class Movie {

    String title;
    String genre;
    String year;
    String urlThumbnail;

    public Movie() {
    }

    public Movie(String title, String genre, String year, String urlThumbnail) {
        this.title = title;
        this.genre = genre;
        this.year = year;

        this.urlThumbnail = urlThumbnail;
    }

    public Movie(MovieEnum movieEnum) {
        this.title = movieEnum.getTitle();
        this.genre = movieEnum.getGenre();
        this.year = movieEnum.getYear();

        this.urlThumbnail = movieEnum.getUrl();
    }
}

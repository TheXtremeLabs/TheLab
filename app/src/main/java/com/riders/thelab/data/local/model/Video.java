package com.riders.thelab.data.local.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Parcel
public class Video implements Parcelable {

    Integer db_id;

    @SerializedName("id")
    String fav_id;
    String name;
    String description;

    @SerializedName("imageUrl")
    String imageThumb;

    String videoUrl;


    public Video() {
    }

    public Video(String fav_id, String name) {
        this.fav_id = fav_id;
        this.name = name;
    }

    public Video(Integer db_id, String fav_id, String name) {

        this.db_id = db_id;

        this.fav_id = fav_id;
        this.name = name;
    }

    public Video(String fav_id, String name, String description, String imageThumb, String videoUrl) {

        this.fav_id = fav_id;
        this.name = name;
        this.description = description;
        this.imageThumb = imageThumb;
        this.videoUrl = videoUrl;

    }

    public Video(Integer db_id, String fav_id, String name, String description, String imageThumb, String videoUrl) {

        this.db_id = db_id;

        this.fav_id = fav_id;
        this.name = name;
        this.description = description;
        this.imageThumb = imageThumb;
        this.videoUrl = videoUrl;

    }


    public Video(android.os.Parcel source) {

        this.fav_id = source.readString();
        this.name = source.readString();
        this.description = source.readString();
        this.imageThumb = source.readString();
        this.videoUrl = source.readString();

    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(android.os.Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(fav_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imageThumb);
        dest.writeString(videoUrl);

    }


    ///////////////////////////////////////////////////////////////////////
    ////////////////////  GETTER & SETTER  ///////////////////////////
    ///////////////////////////////////////////////////////////////////////

    public Integer getId() {
        return db_id;
    }

    public void setId(Integer db_id) {
        this.db_id = db_id;
    }

    public String getFavId() {
        return fav_id;
    }

    public void setFavId(String fav_id) {
        this.fav_id = fav_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String toString() {
        return "name : " + this.name + ", description : " + this.description + ", icon : " + this.imageThumb;
    }
}

package ru.guu.radiog.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dmitry on 26.07.17.
 */

public class LastFmTrack {
    @SerializedName("name")
    String name;
    @SerializedName("album")
    LastFmAlbum album;

    public String getName() {
        return name;
    }

    public LastFmAlbum getAlbum() {
        return album;
    }

    public class LastFmAlbum {
        @SerializedName("artist")
        String artist;
        @SerializedName("title")
        String title;
        @SerializedName("url")
        String url;
        @SerializedName("image")
        ArrayList<LastFmImage> images;

        public String getArtist() {
            return artist;
        }

        public String getTitle() {
            return title;
        }

        public String getUrl() {
            return url;
        }

        public ArrayList<LastFmImage> getImages() {
            return images;
        }
    }

    public class LastFmImage {
        @SerializedName("#text")
        String imageUrl;
        @SerializedName("size")
        String size;

        public String getImageUrl() {
            return imageUrl;
        }

        public String getSize() {
            return size;
        }
    }
}
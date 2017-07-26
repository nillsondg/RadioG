package ru.guu.radiog.network.model;

import com.google.gson.annotations.SerializedName;


/**
 * Created by dmitry on 26.07.17.
 */

public class ItunesSong {


    @SerializedName("wrapperType")
    String wrapperType;
    @SerializedName("kind")
    String kind;
    @SerializedName("artistId")
    long artistId;
    @SerializedName("collectionId")
    long collectionId;

    @SerializedName("trackId")
    long trackId;
    @SerializedName("artistName")
    String artistName;
    @SerializedName("collectionName")
    String collectionName;
    @SerializedName("collectionCensoredName")
    String collectionCensoredName;
    @SerializedName("trackCensoredName")
    String trackCensoredName;
    @SerializedName("artistViewUrl")
    String artistViewUrl;
    @SerializedName("collectionViewUrl")
    String collectionViewUrl;
    @SerializedName("trackViewUrl")
    String trackViewUrl;
    @SerializedName("previewUrl")
    String previewUrl;
    @SerializedName("artworkUrl30")
    String artworkUrl30;
    @SerializedName("artworkUrl60")
    String artworkUrl60;
    @SerializedName("artworkUrl100")
    String artworkUrl100;
    @SerializedName("releaseDate")
    String releaseDate;
    @SerializedName("primaryGenreName")
    String primaryGenreName;

    public String getWrapperType() {
        return wrapperType;
    }

    public String getKind() {
        return kind;
    }

    public long getArtistId() {
        return artistId;
    }

    public long getCollectionId() {
        return collectionId;
    }

    public long getTrackId() {
        return trackId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public String getCollectionCensoredName() {
        return collectionCensoredName;
    }

    public String getTrackCensoredName() {
        return trackCensoredName;
    }

    public String getArtistViewUrl() {
        return artistViewUrl;
    }

    public String getCollectionViewUrl() {
        return collectionViewUrl;
    }

    public String getTrackViewUrl() {
        return trackViewUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getArtworkUrl30() {
        return artworkUrl30;
    }

    public String getArtworkUrl60() {
        return artworkUrl60;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public String getArtworkUrl600() {
        return artworkUrl100.replace("100", "600");
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }
}

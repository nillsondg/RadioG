package ru.guu.radiog.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dmitry on 26.07.17.
 */

public class LastFmApiResult {
    @SerializedName("track")
    LastFmTrack track;

    public LastFmTrack getTrack() {
        return track;
    }
}

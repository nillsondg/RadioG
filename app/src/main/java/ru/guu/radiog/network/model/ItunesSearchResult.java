package ru.guu.radiog.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dmitry on 26.07.17.
 */

public class ItunesSearchResult {

    @SerializedName("resultCount")
    int resultCount;

    @SerializedName("results")
    ArrayList<ItunesSong> results;


    public ArrayList<ItunesSong> getResults() {
        return results;
    }

    public int getResultCount() {
        return resultCount;
    }
}

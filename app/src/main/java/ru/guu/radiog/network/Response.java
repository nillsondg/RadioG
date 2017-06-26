package ru.guu.radiog.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dmitry on 22.06.17.
 */

@SuppressWarnings("WeakerAccess")
public class Response {
    boolean status;
    String text;

    @SerializedName("response_id")
    String responseId;
    @SerializedName("request_time")
    String requestTime;

    public boolean isOk() {
        return status;
    }

    public String getText() {
        return text;
    }

    public String getResponseId() {
        return responseId;
    }

    public String getRequestTime() {
        return requestTime;
    }
}
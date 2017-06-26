package ru.guu.radiog.network.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import ru.guu.radiog.utils.DateUtils;

/**
 * Created by dmitry on 22.06.17.
 */
@SuppressWarnings("WeakerAccess")
public class EventDate extends DataModel implements Comparable<EventDate> {
    public static final String FIELDS_LIST = "start_datetime_utc,end_datetime_utc";

    @SerializedName("event_date")
    int eventDate;

    @SerializedName("start_datetime_utc")
    int startDateTime;
    @SerializedName("end_datetime_utc")
    int endDateTime;


    @Override
    public int getEntryId() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull EventDate another) {
        return eventDate > another.eventDate ? 1 : eventDate == another.eventDate ? 0 : -1;
    }

    public Date getEventDate() {
        return DateUtils.date(eventDate);
    }

    public Date getStartDateTime() {
        return DateUtils.date(startDateTime);
    }

    public Date getEndDateTime() {
        return DateUtils.date(endDateTime);
    }

}
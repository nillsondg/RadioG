package ru.guu.radiog.network.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

import ru.guu.radiog.utils.DateUtils;
import ru.guu.radiog.utils.ServiceUtils;

/**
 * Created by dmitry on 22.06.17.
 */

@SuppressWarnings("WeakerAccess")
public class Event extends DataModel {
    public static final String FIELDS_LIST =
            "description,created_at,is_now," +
                    "dates" + ServiceUtils.encloseFields(EventDate.FIELDS_LIST);
    public static final String ORDER_BY = "-is_now,nearest_event_date";

    @SerializedName("id")
    int eventId;
    String title;
    @SerializedName("first_event_date")
    int firstDateTime;
    @SerializedName("last_event_date")
    int lastDateTime;
    @SerializedName("nearest_event_date")
    @Nullable
    Integer nearestDateTime;
    @SerializedName("image_horizontal_url")
    String imageHorizontalUrl;
    @SerializedName("image_vertical_url")
    String imageVerticalUrl;
    @SerializedName("organization_id")
    int organizationId;

    String description;

    @SerializedName("dates")
    ArrayList<EventDate> dateList;


    @Override
    public int getEntryId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public Date getFirstDateTime() {
        return DateUtils.date(firstDateTime);
    }

    public Date getLastDateTime() {
        return DateUtils.date(lastDateTime);
    }

    @Nullable
    public Date getNearestDateTime() {
        return nearestDateTime == null ? null : DateUtils.date(nearestDateTime);
    }

    public String getImageHorizontalUrl() {
        return imageHorizontalUrl;
    }

    public String getImageVerticalUrl() {
        return imageVerticalUrl;
    }

    public int getOrganizationId() {
        return organizationId;
    }


    public ArrayList<EventDate> getDateList() {
        return dateList;
    }

    public String getDescription() {
        return description;
    }

    public EventDate getCurrentDate(Date date) {
        for (EventDate eventDate : dateList) {
            if (eventDate.getStartDateTime().getTime() > date.getTime()
                    && date.getTime() < eventDate.getEndDateTime().getTime())
                return eventDate;
        }
        return dateList.get(0);
    }
}

package ru.guu.radiog.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import ru.guu.radiog.network.model.Event;
import ru.guu.radiog.network.model.EventDate;

/**
 * Created by dmitry on 26.06.17.
 */

class EventFormatter {
    public static String formatDateInterval(Event event) {
        //10-13, 15, 20-31 december; 23 january
        if (event.getDateList().size() == 0) {
            //no dates -> error at server
            return "";
        }
        Collections.sort(event.getDateList());
        String firstDay = "";
        String firstMonth = "";
        String curDay = "";
        String curMonth = "";
        String prevDay = "";
        String prevMonth = "";
        StringBuilder resStr = new StringBuilder();
        String curStr = "";
        for (EventDate eventDate : event.getDateList()) {
            if (firstDay.equals("")) {
                firstDay = formatDay(eventDate.getEventDate());
                firstMonth = formatMonth(eventDate.getEventDate());
                curDay = firstDay;
                curMonth = firstMonth;
                curStr = curDay;
                continue;
            }
            prevDay = curDay;
            prevMonth = curMonth;
            curDay = formatDay(eventDate.getEventDate());
            curMonth = formatMonth(eventDate.getEventDate());
            if (Integer.parseInt(curDay) - 1 != Integer.parseInt(prevDay)) {
                resStr.append(curStr);
                curStr = "";
                if (Integer.parseInt(prevDay) == Integer.parseInt(firstDay)) {
                    if (!firstMonth.equals(curMonth)) {
                        resStr.append(" ").append(prevMonth);
                        curStr = "; ";
                    } else {
                        curStr += ", ";
                    }
                } else if (Integer.parseInt(prevDay) - 1 != Integer.parseInt(firstDay)) {
                    if (!firstMonth.equals(curMonth)) {
                        resStr.append("-").append(prevDay).append(" ").append(prevMonth);
                        curStr = "; ";
                    } else {
                        resStr.append("-").append(prevDay);
                        curStr = ", ";
                    }
                } else {
                    if (!firstMonth.equals(prevMonth)) {
                        resStr.append(", ").append(prevDay).append(" ").append(prevMonth);
                        curStr = "; ";
                    } else {
                        resStr.append(", ").append(prevDay);
                        curStr = ", ";
                    }
                }
                firstDay = curDay;
                firstMonth = curMonth;
                curStr += curDay;
            }
        }
        if (!prevDay.equals("") && Integer.parseInt(curDay) == Integer.parseInt(firstDay)) {
            if (!curMonth.equals(firstMonth)) {
                resStr.append(" ").append(prevMonth).append("; ");
                resStr.append(curDay).append(" ").append(curMonth);
            } else {
                resStr.append(curStr).append(" ").append(curMonth);
            }
        } else if (!prevDay.equals("") &&
                Integer.parseInt(curDay) - 1 != Integer.parseInt(firstDay)) {
            if (!curMonth.equals(firstMonth)) {
                resStr.append(" ").append(prevMonth).append("; ").append(curDay).append(" ").append(curMonth);
            } else {
                resStr.append(curStr).append("-").append(curDay).append(" ").append(curMonth);
            }
        } else {
            if (Integer.parseInt(curDay) - 1 == Integer.parseInt(firstDay))
                resStr.append(curStr).append(", ").append(curDay).append(" ").append(curMonth);
            else
                resStr.append(curStr).append(" ").append(curMonth);
        }
        return resStr.toString();
    }

    private static String formatDay(Date date) {
        DateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());
        return dayFormat.format(date);
    }

    private static String formatMonth(Date date) {
        DateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        return monthFormat.format(date);
    }

    public static String formatDate(Date date) {
        return DateFormatter.formatEventSingleDate(date);
    }

    public static String formatEventTime(Date startDateTime, Date endDateTime) {
        return DateFormatter.formatTime(startDateTime) + " - " +
                DateFormatter.formatTime(endDateTime);
    }

    public static Date getNearestDateTime(Event event) {
        Date currentDate = new Date(System.currentTimeMillis());
        for (EventDate eventDate : event.getDateList()) {
            if (eventDate.getStartDateTime().getTime() <= currentDate.getTime()
                    && eventDate.getEndDateTime().getTime() >= currentDate.getTime()) {
                return currentDate;
            }
        }
        if (event.getNearestDateTime() == null) {
            return event.getLastDateTime();
        }
        return event.getNearestDateTime();
    }
}

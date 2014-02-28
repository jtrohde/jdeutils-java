package com.whitelightgrp.mobility.android.jde;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

/**
 * Utility methods for working with JDE data.
 *
 * @author Justin Rohde, WhiteLight Group
 */
public class JdeUtils {
    /**
     * Convert JDE date and time integers to {@link org.joda.time.DateTime}, including seconds by default.
     *
     * @param dateJde The JDE date integer to convert.
     * @param timeJde The JDE time integer to convert and add to the date.
     * @return The resulting {@link org.joda.time.DateTime}.
     */
    public static DateTime convertJdeToDateTime(int dateJde, int timeJde) {
        return convertJdeToDateTime(dateJde, timeJde, true);
    }

    /**
     * Convert JDE date and time integers to {@link org.joda.time.DateTime}.
     *
     * @param dateJde The JDE date integer to convert.
     * @param timeJde The JDE time integer to convert and add to the date.
     * @param includeSeconds Whether seconds are included in the input time.
     * @return The resulting {@link org.joda.time.DateTime}.
     */
    public static DateTime convertJdeToDateTime(int dateJde, int timeJde, boolean includeSeconds) {
        // Convert JDE date and time into year, day, hour and minute
        int year = 1900 + dateJde / 1000;
        int dayOfYear = dateJde % 1000;

        // Split time into components
        int hours = timeJde / 10000;
        int remainder = (timeJde - hours * 10000);
        int minutes = remainder / 100;
        int seconds = remainder % 100;
        if (!includeSeconds) {
            // Shift left because seconds aren't included
            hours = minutes;
            minutes = seconds;
            seconds = 0;
        }

        // Construct the result
        MutableDateTime dateTime = new MutableDateTime();
        dateTime.setYear(year);
        dateTime.setDayOfYear(dayOfYear);
        dateTime.setHourOfDay(hours);
        dateTime.setMinuteOfHour(minutes);
        dateTime.setSecondOfMinute(seconds);
        dateTime.setMillisOfSecond(0);
        return dateTime.toDateTime();
    }

    /**
     * Convert a {@link org.joda.time.DateTime} to JDE integer date and time, with seconds included by default.
     *
     * @param dateTime {@link org.joda.time.DateTime} representing the date and time to convert. If {@code null}, the current instant will be used.
     * @return An array with JDE Julian date as the first element and JDE time as the second.
     */
    public static int[] convertDateTimeToJde(DateTime dateTime) {
        return convertDateTimeToJde(dateTime, true);
    }

    /**
     * Convert a {@link org.joda.time.DateTime} to JDE integer date and time.
     *
     * @param dateTime {@link org.joda.time.DateTime} representing the date and time to convert. If {@code null}, the current instant will be used.
     * @param includeSeconds Whether to factor seconds into the result.
     * @return An array with JDE Julian date as the first element and JDE time as the second.
     */
    public static int[] convertDateTimeToJde(DateTime dateTime, boolean includeSeconds) {
        if (dateTime == null) {
            dateTime = DateTime.now();
        }

        int yearJde = dateTime.getYear() - 1900;
        int dayOfYearJde = dateTime.getDayOfYear();
        int hourOfDayJde = dateTime.getHourOfDay();
        int minuteOfHourJde = dateTime.getMinuteOfHour();
        int secondOfMinuteJde = dateTime.getSecondOfMinute();

        String timeText;
        if (includeSeconds) {
            timeText = String.format("%02d%02d%02d", hourOfDayJde, minuteOfHourJde, secondOfMinuteJde);
        }
        else {
            timeText = String.format("%02d%02d", hourOfDayJde, minuteOfHourJde);
        }

        int timeJde;
        try {
            timeJde = Integer.parseInt(timeText);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            timeJde = 0;
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
            timeJde = 0;
        }

        return new int[] { yearJde * 1000 + dayOfYearJde, timeJde };
    }
}

package com.example.funfood.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    // ISO 8601 format from API
    private static final String ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String ISO_FORMAT_SHORT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    // Display formats
    private static final String DISPLAY_FORMAT = "dd/MM/yyyy HH:mm";
    private static final String DISPLAY_FORMAT_SHORT = "dd/MM/yyyy";
    private static final String DISPLAY_FORMAT_TIME = "HH:mm";
    private static final String DISPLAY_FORMAT_ORDER = "dd MMM yyyy, HH:mm";

    private static final Locale LOCALE = new Locale("vi", "VN");

    /**
     * Parse ISO date string to Date
     */
    public static Date parseISODate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat(ISO_FORMAT, LOCALE);
            return format.parse(isoDate);
        } catch (ParseException e) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(ISO_FORMAT_SHORT, LOCALE);
                return format.parse(isoDate);
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Format date to display string
     * Example: "25/10/2024 14:30"
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat format = new SimpleDateFormat(DISPLAY_FORMAT, LOCALE);
        return format.format(date);
    }

    public static String formatDate(String isoDate) {
        Date date = parseISODate(isoDate);
        return formatDate(date);
    }

    /**
     * Format date to short display string
     * Example: "25/10/2024"
     */
    public static String formatDateShort(Date date) {
        if (date == null) return "";
        SimpleDateFormat format = new SimpleDateFormat(DISPLAY_FORMAT_SHORT, LOCALE);
        return format.format(date);
    }

    public static String formatDateShort(String isoDate) {
        Date date = parseISODate(isoDate);
        return formatDateShort(date);
    }

    /**
     * Format time only
     * Example: "14:30"
     */
    public static String formatTime(Date date) {
        if (date == null) return "";
        SimpleDateFormat format = new SimpleDateFormat(DISPLAY_FORMAT_TIME, LOCALE);
        return format.format(date);
    }

    public static String formatTime(String isoDate) {
        Date date = parseISODate(isoDate);
        return formatTime(date);
    }

    /**
     * Format for order display
     * Example: "25 Oct 2024, 14:30"
     */
    public static String formatOrderDate(String isoDate) {
        Date date = parseISODate(isoDate);
        if (date == null) return "";
        SimpleDateFormat format = new SimpleDateFormat(DISPLAY_FORMAT_ORDER, LOCALE);
        return format.format(date);
    }

    /**
     * Get relative time
     * Example: "2 hours ago", "Just now", "Yesterday"
     */
    public static String getRelativeTime(String isoDate) {
        Date date = parseISODate(isoDate);
        if (date == null) return "";

        long now = System.currentTimeMillis();
        long time = date.getTime();
        long diff = now - time;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (seconds < 60) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days == 1) {
            return "Hôm qua";
        } else if (days < 7) {
            return days + " ngày trước";
        } else {
            return formatDateShort(date);
        }
    }

    /**
     * Check if date is today
     */
    public static boolean isToday(Date date) {
        if (date == null) return false;

        SimpleDateFormat format = new SimpleDateFormat(DISPLAY_FORMAT_SHORT, LOCALE);
        String dateStr = format.format(date);
        String todayStr = format.format(new Date());

        return dateStr.equals(todayStr);
    }

    public static boolean isToday(String isoDate) {
        Date date = parseISODate(isoDate);
        return isToday(date);
    }
}
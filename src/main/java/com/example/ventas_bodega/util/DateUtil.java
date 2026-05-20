package com.example.ventas_bodega.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter HOUR_MINUTE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static String formatToYearMonthDay(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        return dateTime.format(FORMATTER);
    }

    public static String formatToHourMinute(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        return dateTime.format(HOUR_MINUTE_FORMATTER);
    }

}

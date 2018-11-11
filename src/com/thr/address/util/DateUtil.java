package com.thr.address.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Thr
 */
public class DateUtil {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static String format(LocalDate date) {
        if (null == date) {
            return null;
        }
        return DATE_TIME_FORMATTER.format(date);
    }

    public static LocalDate parse(String dateString) {
        try {
            return DATE_TIME_FORMATTER.parse(dateString, LocalDate::from);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString) != null;
    }

}
package com.group.cs520.service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static LocalDate dateFormatter(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return formattedDate;
    }
}

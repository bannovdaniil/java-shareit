package ru.practicum.shareit;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final int PAGE_SIZE_NUM = 20;
    public static final String PAGE_SIZE_STRING = "20";
    public static final String DATE_TIME_SRING = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_SRING);
}

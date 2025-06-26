package com.example.bootJPA.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeConverter {
    public static String timeOrDate(LocalDateTime localDateTime) {
        String date = localDateTime.toLocalDate().toString();
        String time = localDateTime.toLocalTime().toString().substring(0, 8);
        if (LocalDate.now().toString().equals(date)) {
            return time;
        }
        return date;
    }

    public static String detailTimeOrDate(LocalDateTime localDateTime){
        String detailDateTime = localDateTime.toLocalDate().toString()
                + " " + localDateTime.toLocalTime().toString().substring(0, 8);

        return detailDateTime;
    }
}

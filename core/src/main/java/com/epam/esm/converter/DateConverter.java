package com.epam.esm.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {
    private TimeZone tz;
    private DateFormat df;

    public DateConverter() {
        this.tz = TimeZone.getTimeZone("Europe/Minsk");
        this.df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"); // Quoted "Z" to indicate UTC, no timezone offset
        this.df.setTimeZone(tz);
    }

    public Date parse(String date) {
        try {
            return df.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public String formatDate(Date date) {
        return df.format(date);
    }
}

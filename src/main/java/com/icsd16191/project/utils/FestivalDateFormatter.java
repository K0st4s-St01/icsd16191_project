package com.icsd16191.project.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FestivalDateFormatter {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy->hh:mm");
    public static Date toDate(String date) throws ParseException {
        return simpleDateFormat.parse(date);
    }
    public static String toString(Date date){
        return simpleDateFormat.format(date);
    }
}

package me.borawski.arena.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ethan on 7/29/2017.
 */
public class TimeUtil {

    public static String getTime() {
        long current = System.currentTimeMillis();
        Date date = new Date(current);
        SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy HH:mm");
        return f.format(date);
    }

    public static String getTime(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("MM dd, yyyy").format(date);
    }

    public static String getPrettyTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM dd, yyyy");

        String[] e = format.format(date).split(" ");
        String month = e[0];
        String day = e[1].replace(",", "");
        String year = e[2];

        System.out.println(format.format(date));
        return getMonth(month) + " " + day + (day.endsWith("1")?"st":day.endsWith("2")?"nd":day.endsWith("3")?"rd":"th") + " " + year;
    }

    public static String getMonth(String dateFormat) {
        if(dateFormat.equalsIgnoreCase("01")) {
            return "January";
        } else if(dateFormat.equalsIgnoreCase("02")) {
            return "February";
        } else if(dateFormat.equalsIgnoreCase("03")) {
            return "March";
        } else if(dateFormat.equalsIgnoreCase("04")) {
            return "April";
        } else if(dateFormat.equalsIgnoreCase("05")) {
            return "May";
        } else if(dateFormat.equalsIgnoreCase("06")) {
            return "June";
        } else if(dateFormat.equalsIgnoreCase("07")) {
            return "July";
        } else if(dateFormat.equalsIgnoreCase("08")) {
            return "August";
        } else if(dateFormat.equalsIgnoreCase("09")) {
            return "September";
        } else if(dateFormat.equalsIgnoreCase("10")) {
            return "October";
        } else if(dateFormat.equalsIgnoreCase("11")) {
            return "November";
        } else if(dateFormat.equalsIgnoreCase("12")) {
            return "December";
        } else {
            return "";
        }
    }


}

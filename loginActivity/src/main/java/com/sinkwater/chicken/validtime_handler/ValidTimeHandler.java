package com.sinkwater.chicken.validtime_handler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Alex on 12/9/2014.
 */
public class ValidTimeHandler {
    public ValidTimeHandler() {

    }
    //TODO Verify that admin entered in a correct time using RegEx
    /*
    public boolean isValidTime(String time) {
        return false;
    }
    */

    public boolean checkValidTime(String time) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("America/Los_Angeles"));
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        // If current day is Sunday, day=1. Saturday, day=7.
        String hour = "";
        String minute = "";
        String lowercase_time = time.toLowerCase();
        switch (day) {
            case Calendar.SUNDAY:
                if(!(lowercase_time.contains("su")))
                    return false;
                hour = (lowercase_time.substring(lowercase_time.indexOf("su")+2)).substring(0,5);
                minute = (lowercase_time.substring(lowercase_time.indexOf("su")+8)).substring(0,5);
                break;

            case Calendar.MONDAY:
                if(!(lowercase_time.contains("mo")))
                    return false;
                // MO22.12-22.13
                hour = (lowercase_time.substring(lowercase_time.indexOf("mo")+2)).substring(0,5);
                minute = (lowercase_time.substring(lowercase_time.indexOf("mo")+8)).substring(0,5);
                break;
            case Calendar.TUESDAY:
                if(!(lowercase_time.contains("tu")))
                    return false;
                hour = (lowercase_time.substring(lowercase_time.indexOf("tu")+2)).substring(0,5);
                minute = (lowercase_time.substring(lowercase_time.indexOf("tu")+8)).substring(0,5);
                break;
            case Calendar.WEDNESDAY:
                if(!(lowercase_time.contains("we")))
                    return false;
                hour = (lowercase_time.substring(lowercase_time.indexOf("we")+2)).substring(0,5);
                minute = (lowercase_time.substring(lowercase_time.indexOf("we")+8)).substring(0,5);
                break;
            case Calendar.THURSDAY:
                if(!(lowercase_time.contains("th")))
                    return false;
                hour = (lowercase_time.substring(lowercase_time.indexOf("th")+2)).substring(0,5);
                minute = (lowercase_time.substring(lowercase_time.indexOf("th")+8)).substring(0,5);
                break;
            case Calendar.FRIDAY:
                if(!(lowercase_time.contains("fr")))
                    return false;
                hour = (lowercase_time.substring(lowercase_time.indexOf("fr")+2)).substring(0,5);
                minute = (lowercase_time.substring(lowercase_time.indexOf("fr")+8)).substring(0,5);
                break;
            case Calendar.SATURDAY:
                if(!(lowercase_time.contains("sa")))
                    return false;
                hour = (lowercase_time.substring(lowercase_time.indexOf("sa")+2)).substring(0,5);
                minute = (lowercase_time.substring(lowercase_time.indexOf("sa")+8)).substring(0,5);
                break;
        }
        double startHour = Double.parseDouble(hour);
        double endHour = Double.parseDouble(minute);
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
        String currentTime = sdf.format(calendar.getTime());
        double currentTimeNumber = Double.parseDouble(currentTime);
        if(currentTimeNumber >= startHour && currentTimeNumber <=endHour)
            return true;
        else
            return false;
    }
}

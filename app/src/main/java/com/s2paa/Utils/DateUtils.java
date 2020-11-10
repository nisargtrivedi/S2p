package com.s2paa.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 8/2/2017.
 */

public class DateUtils {

    public static String GETLOCALE_DATETIME()
    {
        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        return stringDate;
    }
    public static String GETLOCALE_DATETIME(Date date)
    {
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        return stringDate;
    }
    public static String GETLOCALE_DATE(Date date)
    {
        String stringDate = DateFormat.getDateTimeInstance().format(date);
        return stringDate;
    }
    public static String GETLOCALE_DATE()
    {
        Date date = new Date();
        String stringDate = DateFormat.getDateInstance().format(date);
        return stringDate;
    }
    public static String GETLOCALE_TIME()
    {
        Date date = new Date();
        String stringDate = DateFormat.getTimeInstance().format(date);
        return stringDate;
    }

    public static String getTimeFromTime(String time)
    {
        Date dateObj=new Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            dateObj = sdf.parse(time);
//            System.out.println(dateObj);
//            System.out.println(new SimpleDateFormat("hh:mm a").format(dateObj));
           // holder.time.setText(new SimpleDateFormat("hh:mm a").format(dateObj));
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(new SimpleDateFormat("hh:mm a").format(dateObj));
    }



}

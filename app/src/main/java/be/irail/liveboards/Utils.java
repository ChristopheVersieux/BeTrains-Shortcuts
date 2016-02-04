package be.irail.liveboards;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by 201601 on 28-Jan-16.
 */
public class Utils {
    public static String getTrainId(String train) {
        String[] array = train.split("\\.");

        if (array.length == 0)
            return train;
        else
            return array[array.length - 1];
    }

    public static String formatDate(long dateFromAPI, boolean isDuration,
                                    boolean isDelay) {
        // TODO: Lot of tweaks, need to be cleaned
        Date date;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));

        if (dateFromAPI == 0)
            return "";
        try {
            if (isDuration) {

                if (isDelay)
                    return "+" + dateFromAPI / 60 + "'";
                else
                    date = new Date((dateFromAPI - 3600) * 1000);
            } else {
                date = new Date(dateFromAPI * 1000);
            }
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "" + dateFromAPI;
        }

    }
    public static String formatDate(Date d, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(d);
    }
}

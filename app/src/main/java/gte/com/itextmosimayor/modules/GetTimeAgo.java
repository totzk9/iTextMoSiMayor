package gte.com.itextmosimayor.modules;

import android.app.Application;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeAgo extends Application {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final int WEEK_MILLIS = 7 * DAY_MILLIS;
    private static final long YEAR_MILLIS = (long) 365 * DAY_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "A few seconds ago.";
        }

        final long diff = now - time;

        if (diff < 48 * HOUR_MILLIS)
            return new SimpleDateFormat("hh:mm aa").format(new Date(time));
        else if (diff < WEEK_MILLIS)
            return new SimpleDateFormat("EEEE").format(new Date(time));
        else if (diff < YEAR_MILLIS)
            return new SimpleDateFormat("MMM dd").format(new Date(time));
        else
            return new SimpleDateFormat("MMM dd, yyyy").format(new Date(time));
    }
}
package utils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtil {
    public static final String fullTime = "yyyy-MM-dd HH:mm:ss";
    public static final String calendar = "yyyy-MM-dd";

    public static String toDateStr(Date date, String fmt) {
        SimpleDateFormat sf = new SimpleDateFormat(fmt);
        return sf.format(date);
    }
    public static Date toDateNf(String dateStr) {
        String fmt = null;
        if (dateStr.length() == fullTime.length()) {
            fmt = fullTime;
        }
        if (dateStr.length() == calendar.length()) {
            fmt = calendar;
        }
        if (fmt == null) {
            return null;
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat(fmt);
            return sf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        DateFormat df
                = DateFormat.getDateTimeInstance(
                DateFormat.FULL, DateFormat.SHORT, Locale.CHINA);
        String message = df.format(new Date());
        System.out.println(message);
    }
}
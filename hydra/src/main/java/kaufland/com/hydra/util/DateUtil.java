package kaufland.com.hydra.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat ISO8601_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static Date parseISO8601StandardDate(String date) throws ParseException {
        return ISO8601_FORMATTER.parse(date);
    }

}

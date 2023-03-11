package ua.moisak.PostService.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDataTimeUtil {

    public static String getLocalDateTimeWithFormatter() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        LocalDateTime localDateTime = LocalDateTime.now();
        String formattedDate = localDateTime.format(format);
        return formattedDate;
    }
}

package edu.usm.config;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Andrew on 5/17/2015.
 */
@Component
public class DateFormatConfig {

    public static final String applicationDateFormat = "yyyy-MM-dd";

    public static String formatDomainDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(applicationDateFormat));
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(applicationDateFormat);
    }

}

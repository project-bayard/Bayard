package edu.usm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Andrew on 5/17/2015.
 */
@Component
public class DateFormatConfig {

    @Value("${dates.domainDateFormat}")
    private String domainDateFormat;

    public String formatDomainDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(domainDateFormat));
    }

}

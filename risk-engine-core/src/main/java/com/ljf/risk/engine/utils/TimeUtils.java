package com.ljf.risk.engine.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Date;

/**
 * @author lijinfeng
 */
public class TimeUtils {

    /**
     * @param timeAmount
     * @return
     */
    public static Date timeAmountChangeDate(String timeAmount) {
        LocalDateTime base = LocalDateTime.now();
        TemporalAmount amount = parse(timeAmount);
        return Date.from(base.plus(amount).atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param timeAmount
     * @return
     */
    public static long timeAmountChangeMillis(String timeAmount) {
        LocalDateTime base = LocalDateTime.now();
        TemporalAmount amount = parse(timeAmount);
        LocalDateTime plus = base.plus(amount);
        Duration between = Duration.between(base, plus);
        return between.toMillis();
    }

    public static TemporalAmount parse(String feString) {
        if (Character.isUpperCase(feString.charAt(feString.length() - 1))) {
            return Period.parse("P" + feString);
        } else {
            return Duration.parse("PT" + feString);
        }
    }



}

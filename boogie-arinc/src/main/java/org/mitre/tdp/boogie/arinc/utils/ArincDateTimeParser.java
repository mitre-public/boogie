package org.mitre.tdp.boogie.arinc.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.function.BiFunction;

public final class ArincDateTimeParser implements BiFunction<String, String, Instant> {
  public static final ArincDateTimeParser INSTANCE = new ArincDateTimeParser();

  private static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .appendPattern("dd-MMM-yyyy")
      .toFormatter(Locale.ENGLISH);
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

  private ArincDateTimeParser() {}
  @Override
  public Instant apply(String dateString, String timeString) {
    LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
    LocalTime time = LocalTime.parse(timeString, TIME_FORMATTER);
    return date.atTime(time).toInstant(ZoneOffset.UTC);
  }
}

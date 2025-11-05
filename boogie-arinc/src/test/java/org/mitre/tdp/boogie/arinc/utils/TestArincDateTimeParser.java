package org.mitre.tdp.boogie.arinc.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;

import org.junit.jupiter.api.Test;

class TestArincDateTimeParser {
  static String date = "09-DEC-2024";
  static String time = "10:32:38";
  static ArincDateTimeParser parser = ArincDateTimeParser.INSTANCE;

  @Test
  void test() {
    Instant instant = parser.apply(date, time);
    assertEquals(1733740358000L, instant.toEpochMilli());
  }
}
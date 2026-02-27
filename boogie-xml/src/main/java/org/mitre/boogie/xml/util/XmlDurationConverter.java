package org.mitre.boogie.xml.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class XmlDurationConverter implements Function<javax.xml.datatype.Duration, Optional<Duration>> {
  public static final XmlDurationConverter INSTANCE = new XmlDurationConverter();

  private static final Logger LOG = LoggerFactory.getLogger(XmlDurationConverter.class);

  private  XmlDurationConverter() {}
  @Override
  public Optional<Duration> apply(javax.xml.datatype.Duration xmlDuration) {
    try {
      // Convert the javax.xml.datatype.Duration to its ISO 8601 string representation
      String isoString = xmlDuration.toString();
      // Parse the ISO 8601 string into a java.time.Duration
      return Optional.of(java.time.Duration.parse(isoString));
    } catch (DateTimeParseException e) {
      LOG.info("Conversion failed: {}", xmlDuration);
      return Optional.empty(); // Or throw an exception
    }
  }
}

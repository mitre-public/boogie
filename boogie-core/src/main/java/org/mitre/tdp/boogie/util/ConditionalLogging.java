package org.mitre.tdp.boogie.util;

import static java.util.Objects.requireNonNull;

import java.util.function.BiConsumer;

/**
 * Collection of logging methods for use in logging things conditionally. If slf4j ever decides to update their APIs to support
 * LOG.if(...) type statements this class can be retired.
 */
public final class ConditionalLogging {

  private ConditionalLogging() {
    throw new IllegalStateException("Unable to instantiate static utility class.");
  }

  /**
   * Logs the message and args via the supplied logger if the initial condition is met.
   */
  public static void logIf(boolean condition, BiConsumer<String, Object[]> logger, String message, Object... args) {
    if (condition) {
      logger.accept(requireNonNull(message), requireNonNull(args));
    }
  }
}

package org.mitre.tdp.boogie;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Facade class for internal only dependency on the {@link Airac} project.
 */
public final class AiracCycle {

  private static final ConcurrentHashMap<String, Instant> CYCLE_TO_DATE = new ConcurrentHashMap<>();

  private AiracCycle() {
  }

  /**
   * This method returns the starting Instant for the input AIRAC cycle.
   * @param cycle the YYCC cycle
   * @return an instant that starts that cycle
   */
  public static Instant startDate(String cycle) {
    return CYCLE_TO_DATE.computeIfAbsent(cycle, c -> Airac.fromIdentifier(cycle).getEffective());
  }

  /**
   * This method returns the expiration Instant for the input AIRAC cycle.
   * @param cycle the YYCC cycle
   * @return an instant that ends that cycle (and starts the next cycle)
   */
  public static Instant endDate(String cycle) {
    Airac nextCycle = Airac.fromIdentifier(cycle).getNext();
    return startDate(nextCycle.toString());
  }

  public static boolean isValidCycle(String cycle) {
    return cycle.length() == 4
        && Character.isDigit(cycle.charAt(0))
        && Character.isDigit(cycle.charAt(1))
        && Character.isDigit(cycle.charAt(2))
        && Character.isDigit(cycle.charAt(3));
  }

  /**
   * Provides the AIRAC cycle for the given time.
   * @param time the time you want to see the cycle for.
   * @return the AIRAC cycle which is YYCC where the YY is the year and the CC is the cycle for the year.
   */
  public static String cycleFor(Instant time) {
    return Optional.of(time)
        .filter(i -> i.isAfter(Airac.epoch))
        .map(Airac::fromInstant)
        .map(Airac::toString)
        .orElseThrow(() -> new IllegalArgumentException("Could not convert the time to an airac cycle: " + time));
  }

  /**
   * Pulled from the <a href="https://github.com/jwkohnen/airac-java">airac-java</a> project.
   *
   * <p>An AIRAC cycle.
   *
   * <p>Provides calculations on Aeronautical Information Regulation And Control (AIRAC) cycle identifiers and effective
   * calendar dates.
   *
   * <p>This class is immutable and thread-safe.
   *
   * <p>Regular, planned Aeronautical Information Publications (AIP) as defined by the International Civil Aviation
   * Organization (ICAO) are published and become effective at fixed dates. This class implements the AIRAC cycle
   * definition as published in the ICAO Aeronautical Information Services Manual (DOC 8126; AN/872; 6th Edition; 2003).
   * Test cases validate officially documented dates from 1998 until 2020, including the rare case of a 14th cycle in the
   * year 2020.
   *
   * <p>This class assumes that AIRAC cycles are effective from the effective date at 00:00:00 UTC until 27 days later at
   * 23:59:59 UTC. However that is not correct:
   *
   * <p>ICAO DOC 8126, 6th Edition (2003), paragraph 2.6.4:
   *
   * <p>"In addition to the use of a predetermined schedule of effective AIRAC dates, Coordinated Universal Time (UTC)
   * must also be used to indicate the time when the AIRAC information will become effective. Since Annex 15,
   * paragraph 3.2.3 specifies that the Gregorian calendar and UTC must be used as the temporal reference system for
   * international civil aviation, in addition to AIRAC dates, 00:01 UTC must be used to indicate the time when the
   * AIRAC-based information will become effective."
   *
   * <p>However this is a "wontfix", because that may just confuse users.
   *
   * <p>This implementation will silently produce bogus data before the internal epoch of 1901-01-10. Contrary to the
   * original Golang implementation this class does not appear to have a significant upper time boundary. At least
   * values corresponding to dates between 1901-01-10 up to year 2200 are plausible.
   *
   * <p>This class only provides calculations on effective dates, not publication or reception dates etc. Although effective
   * dates are clearly defined and are consistent at least between 1998 until 2020, the derivative dates changed
   * historically.[citation needed]
   *
   * @since 1.8
   */
  private static class Airac implements Comparable<Airac>, Serializable {
    /**
     * ICAO DOC 8126, 6th edition (2003); Paragraph 2.6.2 b):
     * the AIRAC effective dates must be in accordance
     * with the predetermined, internationally agreed
     * schedule of effective dates based on an interval of
     * 28 days, including 29 January 1998
     */
    static final Duration durationCycle = Duration.ofDays(28);
    /**
     * All calculations use the UTC time zone.
     */
    private static final ZoneOffset UTC = ZoneOffset.UTC;
    /**
     * Magic effective date of a fictive AIRAC cycle that aligns with the documented cycles, e.g. 1998-01-29.
     */
    static final Instant epoch = Instant.from(ZonedDateTime.of(1901, 1, 10, 0, 0, 0, 0, UTC));

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 19010110L;

    /**
     * Pattern of AIRAC cycle identifiers (yyoo).
     */
    private static final Pattern identifierPattern = Pattern.compile("^(\\d{2})(\\d{2})$");

    /**
     * Internal serial of this cycle, relative to {@code epoch}.
     */
    private final int serial;

    /**
     * Create an instance of an AIRAC with a {@code serial} relative to the {@code epoch}.
     *
     * @param serial the {@code serial} relative to the {@code epoch}
     */
    private Airac(int serial) {
      this.serial = serial;
    }

    /**
     * Obtains an instance of {@code Airac} that occurred at {@link Instant}.
     * <p>
     * Will silently produce bogus data, when instant is before the internal epoch of 1901-01-10.
     *
     * @param instant the point in time at which the AIRAC cycle of interest was current, not null
     * @return an instance of {@code Airac} that occurred at {@link Instant}
     */
    public static Airac fromInstant(Instant instant) {
      Objects.requireNonNull(instant, "instant");
      Duration between = Duration.between(epoch, instant);
      int cycles = (int) (between.getSeconds() / durationCycle.getSeconds());
      return new Airac(cycles);
    }

    /**
     * Obtains an instance of {@code Airac} that is represented by the identifier {@code yyoo}.
     * <p>
     * The String {@code yyoo} must consist of the last two digits of the year and the ordinal, each with leading zeros.
     * This works for years between 1964 and 2063. Identifiers between "6401" and "9913" are interpreted as AIRAC cycles
     * between the years 1964 and 1999 inclusive. AIRAC cycles between "0001" and "6313" are interpreted as AIRAC cycles
     * between the years 2000 and 2063 inclusive.
     *
     * @param yyoo the identifier of an AIRAC cycle ({@code YYOO}), not null.
     * @return An instance of an AIRAC cycle that is represented by the identifier.
     * @throws IllegalArgumentException if the identifier {@code yyoo} is null, malformed or implied cycle does not
     *                                  exist.
     */
    public static Airac fromIdentifier(String yyoo) {
      Objects.requireNonNull(yyoo);

      Matcher m = identifierPattern.matcher(yyoo);
      if (!m.find()) {
        throw new IllegalArgumentException("illegal AIRAC identifier: " + yyoo);
      }

      final int year;
      final int yy = Integer.parseInt(m.group(1));
      if (yy > 63) {
        year = 1900 + yy;
      } else {
        year = 2000 + yy;
      }
      final int ordinal = Integer.parseInt(m.group(2));

      Airac lastAiracOfPreviousYear = fromInstant(Instant.from(ZonedDateTime.of(year - 1, 12, 31, 0, 0, 0, 0, UTC)));
      Airac airac = new Airac(lastAiracOfPreviousYear.serial + ordinal);

      if (airac.getYear() != year) {
        throw new IllegalArgumentException(String.format("year %d does not have %d cycles", year, ordinal));
      }

      return airac;
    }

    /**
     * Returns the effective date (instant) of this AIRAC cycle.
     *
     * @return the effective date (instant) of this AIRAC cycle
     */
    public Instant getEffective() {
      return epoch.plus(durationCycle.multipliedBy(serial));
    }

    /**
     * Returns the ordinal for this AIRAC cycle's identifier.
     *
     * @return the ordinal for this AIRAC cycle's identifier
     */
    public int getOrdinal() {
      return (getEffective().atZone(UTC).getDayOfYear() - 1) / 28 + 1;
    }

    /**
     * Returns the year for this AIRAC cycle's identifier.
     *
     * @return the year for this AIRAC cycle's identifier
     */
    public int getYear() {
      return getEffective().atZone(UTC).getYear();
    }

    /**
     * Returns a short representation of this AIRAC cycle as in "YYOO".
     *
     * @return a short representation of this AIRAC cycle
     */
    @Override
    public String toString() {
      return String.format("%02d%02d", getYear() % 100, getOrdinal());
    }

    /**
     * Returns a verbose representation of this AIRAC cycle as in
     * "YYOO (effective: YYYY-MM-DD; expires: YYYY-MM-DD)"
     *
     * @return a verbose representation of this AIRAC cycle
     */
    public String toLongString() {
      return String.format("%02d%02d (effective: %s; expires: %s)",
          getYear() % 100,
          getOrdinal(),
          getEffective().atZone(UTC).format(DateTimeFormatter.ISO_LOCAL_DATE),
          getNext().getEffective().minusSeconds(1).atZone(UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)
      );
    }

    /**
     * Returns the successor AIRAC cycle to this AIRAC cycle.
     *
     * @return the successor AIRAC cycle to this AIRAC cycle
     */
    public Airac getNext() {
      return new Airac(serial + 1);
    }

    /**
     * Returns the predecessor AIRAC cycle to this AIRAC cycle.
     *
     * @return the predecessor AIRAC cycle to this AIRAC cycle
     */
    public Airac getPrevious() {
      return new Airac(serial - 1);
    }

    /**
     * Indicates whether some other AIRAC cycle is "equal to" this one.
     *
     * @param obj the reference AIRAC cycle with which to compare.
     * @return {@code true} if this AIRAC cycle is the same as the obj
     * argument; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof Airac)) {
        return false;
      }

      Airac other = (Airac) obj;

      return serial == other.serial;
    }

    /**
     * Returns a hash code value for the AIRAC cycle.
     *
     * @return a hash code value for this AIRAC cycle.
     */
    @Override
    public int hashCode() {
      return serial;
    }

    /**
     * Compares this AIRAC cycle to the specified AIRAC cycle.
     * <p>
     * It is "consistent with equals", as defined by {@link Comparable}.
     *
     * @param otherCycle the other AIRAC cycle to be compared to, not null.
     * @return a negative integer, zero, or a positive integer as this AIRAC cycle
     * is less than, equal to, or greater than the specified AIRAC cycle.
     * @throws NullPointerException if the specified AIRAC cycle is null
     */
    @Override
    public int compareTo(Airac otherCycle) {
      Objects.requireNonNull(otherCycle);
      return Integer.compare(serial, otherCycle.serial);
    }
  }
}

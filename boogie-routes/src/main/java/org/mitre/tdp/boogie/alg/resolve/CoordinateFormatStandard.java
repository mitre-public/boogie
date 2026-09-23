package org.mitre.tdp.boogie.alg.resolve;

import java.util.Optional;

public final class CoordinateFormatStandard {

  private static final String ZEROS = "00";
  private static final String D_ZEROS = "0000";

  private CoordinateFormatStandard() {
  }

  /**
   * This method returns true if the coordinate format is supported
   */
  public static boolean supported(String s) {
    return SectionHeuristics.FAA_LATLON.matcher(s).matches()
        || SectionHeuristics.ICAO_LATLON_DM.matcher(s).matches()
        || SectionHeuristics.ICAO_LATLON_D.matcher(s).matches();
  }

  /**
   * This method takes the full lat/long string and returns a latitude in a standard
   * format of DDMMSS{N,S} if the string matches either FAA or ICAO plan formats.
   */
  public static Optional<String> makeLat(String s) {

    if (SectionHeuristics.FAA_LATLON.matcher(s).matches() || SectionHeuristics.ICAO_LATLON_DM.matcher(s).matches()) {
      return Optional.of(String.format("%s%s%s", s.substring(0, 4), ZEROS, s.charAt(4)));
    }

    if (SectionHeuristics.ICAO_LATLON_D.matcher(s).matches()) {
      return Optional.of(String.format("%s%s%s", s.substring(0, 2), D_ZEROS, s.charAt(2)));
    }

    return Optional.empty();
  }

  /**
   * This method takes the full lat/long string and returns a latitude in a standard
   * format of DDDMMSS{E,W} if the string matches either FAA or ICAO plan formats.
   */
  public static Optional<String> makeLon(String s) {

    if (SectionHeuristics.FAA_LATLON.matcher(s).matches()) {
      return Optional.of(String.format("%s%s%S", s.substring(6, 11), ZEROS, s.charAt(11)));
    }

    if (SectionHeuristics.ICAO_LATLON_D.matcher(s).matches()) {
      return Optional.of(String.format("%s%s%s", s.substring(3, 6), D_ZEROS, s.charAt(6)));
    }

    if (SectionHeuristics.ICAO_LATLON_DM.matcher(s).matches()) {
      return Optional.of(String.format("%s%s%s", s.substring(5, 10), ZEROS, s.charAt(10)));
    }

    return Optional.empty();
  }
}

package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE OFFSET DISTANCE IN NAUTICAL MILES AND TENTHS OF A NAUTICAL MILE BETWEEN THE DME AND
 * ASSOCIATED LANDING THRESHOLD. USE WHEN THE DME FACILITY IS SOURCED AS BIAS OR IS SOURCED AS ZERO RANGED TO THE RUNWAY.
 * ONLY USED WHEN DME TRANSMITTER OUTPUT IS MODIFIED TO INDUCE A READING OF ZERO (0) ON  COCKPIT DME READOUTS WHEN
 * AIRCRAFT REACHES RUNWAY THRESHOLD.  EXAMPLES:NULL9.90.11.0
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0.1 - 9.9
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 *
 */
public final class IlsDmeBias extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 3;
  }

  @Override
  public int fieldCode() {
    return 151;
  }

  @Override
  public String regex() {
    return "((0\\.[1-9]|[1-9]\\.[0-9])?)";
  }
}
package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A ONE LETTER CODE IDENTIFYING THE TYPE OF ATS ROUTE.
 * A - ATLANTIC
 * B - BAHAMA
 * C - CORRIDOR
 * D - ADVISORY (ADR)
 * E - DIRECT, CONTROL AREA ROUTES
 * M - MILITARY
 * N - NORTH AMERICAN (NAR)
 * O - OCEANIC
 * R - RNAV
 * S - SUBSTITUTE, CANADIAN CONTROL AREA TRACKS
 * T - TACAN
 * W - AIRWAY
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * A, B, C, D, E, M, N, O, R, S, T, W
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class AtsRouteType extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 41;
  }

  @Override
  public String regex() {
    return "(A|B|C|D|E|M|N|O|R|S|T|W)";
  }
}
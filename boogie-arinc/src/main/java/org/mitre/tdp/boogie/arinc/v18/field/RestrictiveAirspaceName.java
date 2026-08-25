package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The Restrictive Airspace Name field will contain the name of
 * the restrictive airspace when assigned. May be blank.
 */
public final class RestrictiveAirspaceName extends TrimmableString {
  @Override
  public int fieldLength() {
    return 30;
  }

  @Override
  public String fieldCode() {
    return "5.126";
  }
}

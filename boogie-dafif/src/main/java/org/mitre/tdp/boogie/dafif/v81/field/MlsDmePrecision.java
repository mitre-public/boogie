package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE STANDARD CONFIGURATION OF MLS GROUND BASED EQUIPMENT IN REGARDS TO MLS DME.  A ONE
 * CHARACTER FIELD TO INDICATE IF THE MLS DME IS NON-PRECISION (STANDARD) OR PRECISION.
 * POPULATED WHEN THE ILS COMPONENT TYPE IS ‘P’ MLS DME.  DEFAULT POPULATION IS ‘N’,
 * NON-PRECISION (STANDARD), UNLESS SOURCE INDICATES OTHERWISE.  MLS DME N - NON-PRECISION
 * (STANDARD) MLS DME P - PRECISION
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * N, P OR NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 *
 */
public final class MlsDmePrecision extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 187;
  }

  @Override
  public String regex() {
    return "((N|P)?)";
  }
}
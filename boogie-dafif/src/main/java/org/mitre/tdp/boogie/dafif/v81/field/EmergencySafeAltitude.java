package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * ALTITUDE IN FEET ABOVE MEAN SEA LEVEL WHICH PROVIDES AT LEAST 1,000 FEET OF OBSTACLE  CLEARANCE IN NON-MOUNTAINOUS AREAS AND 2,000 FEET OF OBSTACLE CLEARANCE IN DESIGNATED  MOUNTAINOUS AREAS WITHIN A 100 NAUTICAL MILE RADIUS OF THE AIRPORT OR HELIPORT UPON WHICH  THE PROCEDURE IS PREDICATED.  (P/CG)
 *
 * EXAMPLE(S):
 * 06600
 * 12800
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 00001-99999 (PADDED WITH LEADING ZEROS)
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class EmergencySafeAltitude extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 107;
  }

  @Override
  public String regex() {
    return "(([0-9]{4}[1-9]|[0-9]{3}[1-9][0-9]|[0-9]{2}[1-9][0-9]{2}|[0-9][1-9][0-9]{3}|[1-9][0-9]{4})?)";
  }
}
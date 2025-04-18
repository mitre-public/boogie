package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * ALTERNATE MINIMUMS NOT STANDARD, ALTERNATE MINIMUMS NOT AUTHORIZED, TAKEOFF MINIMUMS NOT STANDARD AND/OR DEPARTURE PROCEDURES ARE PUBLISHED. (DAFIF)
 * THE SYMBOLS IN THE DOD TERMINAL PUBLICATIONS ARE:
 * TRIANGLE-A
 * TRIANGLE-A WITH NA
 * AN INVERTED TRIANGLE-T
 *
 * A: ALTERNATE MINIMUMS NOT STANDARD. USA/USAF/USN PILOTS REFER TO APPROPRIATE REGULATION.
 * ANA: ALTERNATE MINIMUMS ARE NOT AUTHORIZED DUE TO UNMONITORED FACILITY OR ABSENCE OF
 * WEATHER REPORTING SERVICE.
 * T: TAKE-OFF MINIMUMS NOT STANDARD AND/OR DEPARTURE PROCEDURES ARE PUBLISHED.
 * SEE THE DAFIF AIRPORT REMARKS FOR DATA.
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * A, ANA, T, TA, TANA
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class AlternateTakeoffMinimums extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 16;
  }

  @Override
  public String regex() {
    return "((TANA|ANA|TA|A|T)?)";
  }
}
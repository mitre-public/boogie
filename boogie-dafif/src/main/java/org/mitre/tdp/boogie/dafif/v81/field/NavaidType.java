package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A NUMBER CODE ASSIGNED TO THE SPECIFIC TYPE OF NAVAID.
 *
 * 1 - VOR
 * 2 - VORTAC
 * 3 - TACAN
 * 4 - VOR-DME
 * 5 - NDB
 * 7 - NDB-DME
 * 9 - DME (EXCLUDING ILS-DME)
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 1, 2, 3, 4, 5, 7, 9
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class NavaidType extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 212;
  }

  @Override
  public String regex() {
    return "((1|2|3|4|5|7|9)?)";
  }
}
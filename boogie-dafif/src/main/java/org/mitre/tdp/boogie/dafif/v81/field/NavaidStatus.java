package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DESIGNATES OPERATIONAL STATUS OF A NAVAID.  NAVAIDS OUT OF SERVICE FOR THE PERIOD BEGINNING  WITH THE RELEASE OF THE DIGITAL DATA AND ARE EXPECTED TO BE OUT OF SERVICE FOR 30 DAYS OR  MORE WILL HAVE AN 'O' INDICATION.  IN SERVICE NAVAIDS WILL HAVE AN 'I' INDICATION.  ON TEST WILL  HAVE A 'T' INDICATION.
 *
 * I - IN SERVICE
 * O - OUT OF SERVICE
 * T - ON TEST
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * I, O, T
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class NavaidStatus extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 211;
  }

  @Override
  public String regex() {
    return "(I|O|T)";
  }
}
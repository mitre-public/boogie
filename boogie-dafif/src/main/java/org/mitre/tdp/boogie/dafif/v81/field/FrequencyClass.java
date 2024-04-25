package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A ONE LETTER CODE IDENTIFYING THE ATS ROUTE FREQUENCY CLASS AS FOLLOWS:
 * A - UHF/VHF
 * B - LF/MF
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * A, B
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class FrequencyClass extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 129;
  }

  @Override
  public String regex() {
    return "(A|B)";
  }
}
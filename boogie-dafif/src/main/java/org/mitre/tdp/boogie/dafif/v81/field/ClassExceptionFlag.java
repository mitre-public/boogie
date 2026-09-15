package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Y indicates exceptions to the airspace class.
 * <p>DAFIF 8.1 data dictionary field 63.</p>
 */
public final class ClassExceptionFlag extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 63;
  }

  @Override
  public String regex() {
    return "((Y)?)";
  }
}

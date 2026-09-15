package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Description of exceptions to the airspace class.
 * <p>DAFIF 8.1 data dictionary field 64.</p>
 */
public final class ClassExceptionRemarks extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 80;
  }

  @Override
  public int fieldCode() {
    return 64;
  }

  @Override
  public String regex() {
    return "(([A-Z0-9\\W]{1,80})?)";
  }
}

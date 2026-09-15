package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Effective date of the special use airspace in dd MMM yy format, for example 03 Dec 10.
 * <p>DAFIF 8.1 data dictionary field 104.</p>
 */
public final class EffectiveDate extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 9;
  }

  @Override
  public int fieldCode() {
    return 104;
  }

  @Override
  public String regex() {
    return "((0[1-9]|[1-2][0-9]|3[0-1])[ ](Jan|Mar|May|Jul|Aug|Oct|Dec)[ ]([0-9]{2})|(0[1-9]|[1-2][0-9]|30)[ ](Apr|Jun|Sep|Nov)[ ]([0-9]{2})|(0[1-9]|[1-2][0-9])[ ]Feb[ ]([0-9]{2}))";
  }
}

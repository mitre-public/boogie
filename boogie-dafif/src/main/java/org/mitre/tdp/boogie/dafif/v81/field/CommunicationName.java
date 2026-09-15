package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Name of the facility to contact for airspace communications.
 * <p>DAFIF 8.1 data dictionary field 73.</p>
 */
public final class CommunicationName extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 20;
  }

  @Override
  public int fieldCode() {
    return 73;
  }

  @Override
  public String regex() {
    return "(([^ ][A-Z0-9\\W]{1,19})?)";
  }
}

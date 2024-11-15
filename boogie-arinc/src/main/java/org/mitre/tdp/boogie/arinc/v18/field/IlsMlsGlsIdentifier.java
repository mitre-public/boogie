package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Localizer/MLS/GLS Identifier” field identifies the localizer, MLS facility or GLS Ref Path defined in the record.
 * In the Runway Record, two “Landing Systems” may be defined.
 */
public final class IlsMlsGlsIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.44";
  }
}

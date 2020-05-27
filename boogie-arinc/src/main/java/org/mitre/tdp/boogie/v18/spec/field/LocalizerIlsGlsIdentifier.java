package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Localizer/MLS/GLS Identifier” field identifies the localizer, MLS facility or GLS Ref Path defined in the record.
 * In the Runway Record, two “Landing Systems” may be defined.
 */
public class LocalizerIlsGlsIdentifier implements FreeFormString {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.44";
  }
}

package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.BooleanStringParser;

/**
 * The “IFR Capability” field indicates if the Airport/Heliport has any published Instrument Approach Procedures.
 * <br>
 * Source/Content: The field contains “Y” if there is an Official Government Instrument Approach Procedure published, otherwise
 * the field will contain “N”. (Note: The presence of “Y” in this field does not necessarily imply that the published instrument
 * approach is coded in the data.)
 * <br>
 * Similar to {@link DaylightTimeIndicator}, most sources we see in the real world this is often not populated... lol. In accordance
 * with the spec though {@code null==unknown} and {@code unknown=false}, therefore not present implies no.
 */
public final class IfrCapability implements FieldSpec<Boolean> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.108";
  }

  @Override
  public Optional<Boolean> apply(String fieldValue) {
    return Optional.of(fieldValue).map(BooleanStringParser.INSTANCE::test);
  }
}

package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * The “Altitude Description” field will designate whether a waypoint should be crossed “at,” “at or above,” “at or below” or “at or above to at
 * or below” specified altitudes. The field is also used to designate recommended altitudes and cases where two distinct altitudes are provided
 * at a single fix.
 */
public class AltitudeDescription implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.29";
  }

  @Override
  public String parse(String fieldString) {
    checkSpec(this, fieldString, allowedValues().contains(fieldString));
    return fieldString;
  }

  /**
   * The list of allowed values for the field - refer to the ARINC spec for interpretation.
   */
  public List<String> allowedValues() {
    return Arrays.asList(
        "+",
        "-",
        "@",
        "B",
        "C",
        "G",
        "H",
        "I",
        "J",
        "V",
        "X",
        "Y");
  }
}

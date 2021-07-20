package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec2;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * The “Component Elevation” field defines the elevation of a given component in the Localizer, GLS and MLS records.
 * <br>
 * The “Glide Slope Elevation (GS ELEV)” defines the elevation of the Glide Slope component in the Localizer Records.
 * <br>
 * The “EL Elevation (EL ELEV)” defines the elevation of the Elevation component of the MLS Record.
 * <br>
 * The “Azimuth Elevation (AZ ELEV)” defines the elevation of the Azimuth component of the MLS Record.
 * <br>
 * The “Back Azimuth Elevation (BAZ ELEV)” defines the elevation of the Back Azimuth component of the MLS Record.
 * <br>
 * The “GLS station elevation (GLS ELEV)” defines the elevation of the GLS ground station in the GLS record.
 */
public final class ComponentElevation implements FieldSpec2<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.74";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(ValidArincNumeric.INSTANCE).map(String::trim).map(Double::parseDouble);
  }
}

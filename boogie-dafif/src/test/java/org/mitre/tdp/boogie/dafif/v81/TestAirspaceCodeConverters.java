package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;
import org.mitre.tdp.boogie.dafif.model.enums.Derivation;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;
import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;
import org.mitre.tdp.boogie.dafif.v81.converter.BoundaryTypeConverter;
import org.mitre.tdp.boogie.dafif.v81.converter.DerivationConverter;
import org.mitre.tdp.boogie.dafif.v81.converter.ShapeConverter;
import org.mitre.tdp.boogie.dafif.v81.converter.SpecialUseAirspaceTypeConverter;

class TestAirspaceCodeConverters {

  // DAFIF 8.1 field definition 51: BOUNDARY TYPE.
  @ParameterizedTest
  @CsvSource({
      "1, ADVISORY_AREA",
      "2, AIR_DEFENSE_IDENTIFICATION_ZONE",
      "3, AIR_ROUTE_TRAFFIC_CONTROL_CENTER",
      "4, AREA_CONTROL_CENTER",
      "5, BUFFER_ZONE",
      "6, CONTROL_AREA",
      "7, CONTROL_ZONE",
      "8, FIR",
      "9, OCEAN_CONTROL_AREA",
      "10, RADAR_AREA",
      "11, TERMINAL_CONTROL_AREA",
      "12, UIR",
      "13, MODE_C_DEFINED_AREA",
      "14, OTHER",
      "15, FUNCTIONAL_AIRSPACE_BLOCK"
  })
  void mapsBoundaryCodesToTheirSpecifiedTypes(int code, BoundaryType expected) {
    assertAll(
        () -> assertEquals(expected, BoundaryTypeConverter.INSTANCE.apply(code)),
        () -> assertEquals(code, expected.code())
    );
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(ints = {Integer.MIN_VALUE, -1, 0, 16, Integer.MAX_VALUE})
  void rejectsUndefinedBoundaryCodes(Integer code) {
    assertThrows(IllegalArgumentException.class, () -> BoundaryTypeConverter.INSTANCE.apply(code));
  }

  // DAFIF 8.1 field definition 293: SPECIAL USE AIRSPACE TYPE.
  @ParameterizedTest
  @CsvSource({
      "A, ALERT",
      "D, DANGER",
      "M, MILITARY_OPERATIONS",
      "P, PROHIBITED",
      "R, RESTRICTED",
      "T, TEMPORARY_RESERVED",
      "W, WARNING"
  })
  void mapsSpecialUseCodesToTheirSpecifiedTypes(String code, SpecialUseAirspaceType expected) {
    assertAll(
        () -> assertEquals(expected, SpecialUseAirspaceTypeConverter.INSTANCE.apply(code)),
        () -> assertEquals(code, expected.code())
    );
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"X", " ", "a", "AA", " A", "A ", "ALERT"})
  void rejectsUndefinedSpecialUseCodes(String code) {
    assertThrows(IllegalArgumentException.class, () -> SpecialUseAirspaceTypeConverter.INSTANCE.apply(code));
  }

  // DAFIF 8.1 field definition 289: SHAPE.
  @ParameterizedTest
  @CsvSource({
      "A, POINT",
      "B, GREAT_CIRCLE",
      "C, CIRCLE",
      "G, GENERALIZED",
      "H, RHUMB_LINE",
      "L, COUNTERCLOCKWISE_ARC",
      "R, CLOCKWISE_ARC"
  })
  void mapsShapeCodesToTheirSpecifiedGeometry(String code, Shape expected) {
    assertAll(
        () -> assertEquals(expected, ShapeConverter.INSTANCE.apply(code)),
        () -> assertEquals(code, expected.code())
    );
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"X", " ", "r", "AA", " R", "R ", "POINT"})
  void rejectsUndefinedShapeCodes(String code) {
    assertThrows(IllegalArgumentException.class, () -> ShapeConverter.INSTANCE.apply(code));
  }

  // DAFIF 8.1 field definition 91: DERIVATION.
  @ParameterizedTest
  @CsvSource({
      "B, DISTANCE_AND_BEARING",
      "E, END_COORDINATES",
      "R, PLOTTED_COORDINATES"
  })
  void mapsDerivationCodesToTheirSpecifiedDefinitions(String code, Derivation expected) {
    assertAll(
        () -> assertEquals(expected, DerivationConverter.INSTANCE.apply(code)),
        () -> assertEquals(code, expected.code())
    );
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"X", " ", "b", "BB", " B", "B ", "END_COORDINATES"})
  void rejectsUndefinedDerivationCodes(String code) {
    assertThrows(IllegalArgumentException.class, () -> DerivationConverter.INSTANCE.apply(code));
  }
}

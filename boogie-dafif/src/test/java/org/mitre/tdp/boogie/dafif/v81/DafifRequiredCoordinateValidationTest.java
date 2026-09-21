package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAirportSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifIlsSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifNavaidSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifRunwaySpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifWaypointSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifAirportValidator;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifIlsValidator;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifNavaidValidator;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifRunwayValidator;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifWaypointValidator;

class DafifRequiredCoordinateValidationTest {

  @ParameterizedTest(name = "{0} requires {1}")
  @MethodSource("requiredCoordinates")
  void rejectsMissingPrimaryCoordinates(ValidatedTable table, String fieldName) {
    String[] fields = table.raw().replace("\n", "").split("\t", -1);
    for (int i = 0; i < table.spec().recordFields().size(); i++) {
      if (table.spec().recordFields().get(i).fieldName().equals(fieldName)) fields[i] = "";
    }
    var parser = DafifRecordParser.standard(table.spec());
    var complete = parser.parse(table.spec().recordType(), table.raw()).orElseThrow();
    var missing = parser.parse(table.spec().recordType(), String.join("\t", fields)).orElseThrow();

    assertAll(
        () -> assertTrue(table.validator().test(complete)),
        () -> assertFalse(table.validator().test(missing))
    );
  }

  @Test
  void acceptsNavaidsWithoutSupplementalDmeCoordinates() {
    var record = DafifRecordParser.standard(new DafifNavaidSpec())
        .parse(DafifRecordType.NAV, TestDafifNavaidSpec.RAW_NAVAID).orElseThrow();

    assertAll(
        () -> assertTrue(new DafifNavaidValidator().test(record)),
        () -> assertTrue(record.optionalField("dmeGeodeticLatitude").isEmpty()),
        () -> assertTrue(record.optionalField("dmeDegreesLatitude").isEmpty()),
        () -> assertTrue(record.optionalField("dmeGeodeticLongitude").isEmpty()),
        () -> assertTrue(record.optionalField("dmeDegreesLongitude").isEmpty())
    );
  }

  private static Stream<Arguments> requiredCoordinates() {
    List<String> primary = List.of("geodeticLatitude", "degreesLatitude", "geodeticLongitude", "degreesLongitude");
    return Stream.of(
        new ValidatedTable(new DafifAirportSpec(), TestDafifAirportSpec.RAW_AIRPORT, new DafifAirportValidator(), primary),
        new ValidatedTable(new DafifNavaidSpec(), TestDafifNavaidSpec.RAW_NAVAID, new DafifNavaidValidator(), primary),
        new ValidatedTable(new DafifWaypointSpec(), TestDafifWaypointSpec.RAW_WAYPOINT, new DafifWaypointValidator(), primary),
        new ValidatedTable(new DafifIlsSpec(), TestDafifIlsSpec.RAW_ILS, new DafifIlsValidator(), primary),
        new ValidatedTable(new DafifRunwaySpec(), TestDafifRunwaySpec.RAW_RUNWAY, new DafifRunwayValidator(), List.of(
            "highEndGeodeticLatitude", "highEndDegreesLatitude", "highEndGeodeticLongitude", "highEndDegreesLongitude",
            "lowEndGeodeticLatitude", "lowEndDegreesLatitude", "lowEndGeodeticLongitude", "lowEndDegreesLongitude")))
        .flatMap(table -> table.coordinates().stream().map(field -> Arguments.of(table, field)));
  }

  private record ValidatedTable(DafifRecordSpec spec, String raw, Predicate<DafifRecord> validator, List<String> coordinates) {
    @Override
    public String toString() {
      return spec.recordType().toString();
    }
  }
}

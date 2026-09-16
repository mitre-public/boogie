package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifTerminalSegmentConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalSegmentSpec;

import com.google.common.collect.Range;

class ProcedureAssemblyStrategyTest {

  @ParameterizedTest(name = "ALT_DESC={0}, ALT_ONE={1}, ALT_TWO={2} => {3}")
  @MethodSource("altitudeRestrictions")
  void assemblesParsedAltitudeRestrictions(String descriptor, String altitude1, String altitude2, Range<Double> expected) {
    var leg = assemble(Map.of("altitudeDescription", descriptor, "altitude1", altitude1, "altitude2", altitude2));

    assertEquals(expected, leg.altitudeConstraint());
  }

  private static Stream<Arguments> altitudeRestrictions() {
    return Stream.of(
        Arguments.of("", "03000", "", Range.singleton(3000.0)),
        Arguments.of("", "-0010", "", Range.singleton(-10.0)),
        Arguments.of("", "FL70", "", Range.singleton(7000.0)),
        Arguments.of("+", "FL70", "", Range.atLeast(7000.0)),
        Arguments.of("-", "03000", "", Range.atMost(3000.0)),
        Arguments.of("B", "FL110", "06000", Range.closed(6000.0, 11000.0)),
        Arguments.of("C", "", "04000", Range.atLeast(4000.0)),
        Arguments.of("G", "03000", "02000", Range.singleton(3000.0)),
        Arguments.of("I", "03000", "02000", Range.singleton(3000.0)),
        Arguments.of("H", "03000", "02000", Range.atLeast(3000.0)),
        Arguments.of("J", "03000", "02000", Range.atLeast(3000.0)),
        // With no crossing altitude, ALT_TWO only describes the glide-slope intercept.
        Arguments.of("", "", "02000", Range.all()),
        Arguments.of("", "", "", Range.all())
    );
  }

  @ParameterizedTest(name = "SPEED={0}/{1}/{2}, SPEED2={3}/{4}/{5} => maximum {6}")
  @CsvSource({
      "'',  '', '',      '',  '', '',     ",
      "210, A,  '',      '',  '', '',     210",
      "210, '', '',      '',  '', '',     210",
      "210, J,  '',      '',  '', '',     ",
      "210, T,  '',      '',  '', '',     ",
      "90,  O,  '',      '',  '', '',     ",
      "210, A,  FL100,   '',  '', '',     ",
      "210, '', 08000,   '',  '', '',     ",
      "'',  '', '',      180, A,  '',     180",
      "'',  '', '',      180, '', '',     180",
      "'',  '', '',      180, J,  '',     ",
      "'',  '', '',      180, T,  '',     ",
      "'',  '', '',      90,  O,  '',     ",
      "'',  '', '',      180, A,  FL80,   ",
      "210, A,  '',      180, A,  '',     180",
      "180, A,  '',      210, A,  '',     180",
      "210, J,  '',      180, A,  '',     180",
      "210, A,  '',      180, J,  '',     210",
      "210, J,  '',      180, T,  '',     ",
      "210, A,  FL100,   180, '', '',     180",
      "210, '', '',      180, A,  FL80,   210",
      "'',  J,  FL100,   180, A,  '',     180",
      "210, A,  '',      '',  T,  FL80,   210"
  })
  void appliesOnlyUnqualifiedSpeedLimits(String speed1, String aircraft1, String belowAltitude1,
      String speed2, String aircraft2, String belowAltitude2, Double maximum) {
    var leg = assemble(Map.of(
        "speedLimit1", speed1, "speedLimitAircraftType1", aircraft1, "speedLimitAltitude1", belowAltitude1,
        "speedLimit2", speed2, "speedLimitAircraftType2", aircraft2, "speedLimitAltitude2", belowAltitude2));

    assertEquals(maximum == null ? Range.all() : Range.atMost(maximum), leg.speedConstraint());
  }

  private static Leg assemble(Map<String, String> restrictions) {
    var fields = new HashMap<>(Map.of(
        "airportIdentification", "US00001", "terminalProcedureType", "3", "terminalIdentifier", "I01",
        "terminalSequenceNumber", "10", "terminalApproachType", "I", "icaoCode", "KAAA",
        "trackDescriptionCode", "IF", "cycleDate", "202601"));
    fields.putAll(restrictions);

    var spec = new DafifTerminalSegmentSpec();
    String source = spec.recordFields().stream()
        .map(field -> fields.getOrDefault(field.fieldName(), ""))
        .collect(Collectors.joining("\t"));
    var record = DafifRecordParser.standard(spec).parse(DafifRecordType.TRM_SEG, source).orElseThrow();
    var segment = new DafifTerminalSegmentConverter().apply(record).orElseThrow();
    return ProcedureAssemblyStrategy.standard().convertLeg(segment, null, null, null);
  }
}

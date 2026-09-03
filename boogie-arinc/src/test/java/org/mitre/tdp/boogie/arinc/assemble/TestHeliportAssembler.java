package org.mitre.tdp.boogie.arinc.assemble;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.IsThisAPrimaryRecord;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This data is different enough between versions to warrant two test inputs.
 */
public class TestHeliportAssembler {
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));
  private static final File arincTestFile2 = new File(System.getProperty("user.dir").concat("/src/test/resources/kjra_9vak5-and-friends"));
  private static final TestArincFileParser fileParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V19_NAV.specs()));
  private static final TestArincFileParser v22Parser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V22_NAV.specs()));
  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase19;
  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase22;
  private static HeliportAssembler<Heliport> assembler19;
  private static HeliportAssembler<Heliport> assembler22;

  @BeforeAll
  static void setup() {
    IsThisAPrimaryRecord isThisAPrimaryRecord = new IsThisAPrimaryRecord();
    ConvertingArincRecordConsumer v18Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
    ConvertingArincRecordConsumer v22Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22_NAV);
    fileParser.parseAll(arincTestFile).stream().filter(isThisAPrimaryRecord).forEach(v18Consumer);
    v22Parser.parseAll(arincTestFile2).stream().filter(isThisAPrimaryRecord).forEach(v22Consumer);
    ConvertedArincRecords testV18Records = v18Consumer.snapshot();
    ConvertedArincRecords testV22Records = v22Consumer.snapshot();

    arincTerminalAreaDatabase19 = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV18Records.arincAirports(),
        testV18Records.arincRunways(),
        testV18Records.arincLocalizerGlideSlopes(),
        testV18Records.arincNdbNavaids(),
        testV18Records.arincVhfNavaids(),
        testV18Records.arincWaypoints(),
        testV18Records.arincProcedureLegs(),
        testV18Records.arincGnssLandingSystems(),
        testV18Records.arincHelipads(), //no pads
        testV18Records.arincHeliports()
    );

    arincTerminalAreaDatabase22 = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV22Records.arincAirports(),
        testV22Records.arincRunways(),
        testV22Records.arincLocalizerGlideSlopes(),
        testV22Records.arincNdbNavaids(),
        testV22Records.arincVhfNavaids(),
        testV22Records.arincWaypoints(),
        testV22Records.arincProcedureLegs(),
        testV22Records.arincGnssLandingSystems(),
        testV22Records.arincHelipads(),
        testV22Records.arincHeliports()
    );
    assembler19 = HeliportAssembler.standard(arincTerminalAreaDatabase19);
    assembler22 = HeliportAssembler.standard(arincTerminalAreaDatabase22);
  }

  @Test
  void testKjraAssembly() {
    List<Heliport> ports = arincTerminalAreaDatabase19.heliports("KJRA").stream()
        .map(assembler19::assemble)
        .toList();
    Heliport heliport = ports.get(0);

    Map<String, Helipad> helipads = heliport.helipads().stream().collect(Collectors.toMap(Helipad::padIdentifier, Function.identity()));

    assertAll(
        () -> assertEquals(1, ports.size()),
        () -> assertEquals("KJRA", heliport.heliportIdentifier()),
        () -> assertEquals(-13, heliport.magneticVariation().orElseThrow().angle().inDegrees()),
        () -> assertEquals(1, helipads.size()),
        () -> assertEquals("H1", helipads.get("H1").padIdentifier()),
        () -> assertEquals(heliport.latLong(), helipads.get("H1").origin()),
        () -> assertEquals(40.75437777777778, heliport.latitude()),
        () -> assertEquals(-74.00743611111112, heliport.longitude())
    );
  }
  /**
   * The way these used to be is really bad feeling but this is how we go.
   * The main issues is that this prevents the HA record from being used directly as a fix
   * .... which they are not in -19 (restriction) so its ok.
   */
  @Test
  void test50MS() {
    List<Heliport> ports = arincTerminalAreaDatabase19.heliports("50MS").stream()
        .map(assembler19::assemble)
        .toList();
    Heliport heliport = ports.stream().filter(p -> p.helipads().stream().anyMatch(pad -> "H1".equals(pad.padIdentifier()))).findFirst().orElseThrow();
    Heliport heliport2 = ports.stream().filter(p -> p.helipads().stream().anyMatch(pad -> "H2".equals(pad.padIdentifier()))).findFirst().orElseThrow();
    Map<String, Helipad> helipads = heliport.helipads().stream().collect(Collectors.toMap(Helipad::padIdentifier, Function.identity()));
    Map<String, Helipad> helipads2 = heliport2.helipads().stream().collect(Collectors.toMap(Helipad::padIdentifier, Function.identity()));

    assertAll("Making it explicit that we have duplicate (by ident) heliports because of pads being included",
        () -> assertEquals("50MS", heliport.heliportIdentifier(), "Has the name"),
        () -> assertEquals("50MS", heliport2.heliportIdentifier(), "has the name"),
        () -> assertNotEquals(heliport, heliport2, "BUT NOT THE SAME OBJECT OR VALUE"),
        () -> assertEquals("H1", helipads.get("H1").padIdentifier()),
        () -> assertEquals("H2", helipads2.get("H2").padIdentifier())
    );
  }

  /**
   * Because the later versions of 424 have pads to integrate we need to test data that follows that scheme as well.
   */
  @Test
  void testLaterVersions() {
    List<Heliport> ports = arincTerminalAreaDatabase22.heliports("KJRA").stream()
        .map(assembler22::assemble)
        .toList();
    Heliport heliport = ports.get(0);

    Map<String, Helipad> helipads = heliport.helipads().stream().collect(Collectors.toMap(Helipad::padIdentifier, Function.identity()));
    Helipad h1 = helipads.get("H1");

    assertAll(
        () -> assertEquals(1, ports.size()),
        () -> assertEquals("KJRA", heliport.heliportIdentifier()),
        () -> assertEquals(-13, heliport.magneticVariation().orElseThrow().angle().inDegrees()),
        () -> assertEquals(6, helipads.size()),
        () -> assertEquals("H1", h1.padIdentifier()),
        () -> assertNotEquals(heliport.latLong(), h1.origin(), "works different and the data is different compared to -19"),
        () -> assertEquals(40.75438055555556, h1.origin().latitude()),
        () -> assertEquals(-74.00743611111112, h1.origin().longitude()),
        () -> assertEquals(40.7549, heliport.latitude()),
        () -> assertEquals(-74.0070638888889, heliport.longitude())
    );
  }

  @Test
  void testRunwaysAssociatedWithAHeliport() {
    ArincHeliport arincHeliport = ArincHeliport.builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.H)
        .heliportIdentifier("HPT1")
        .heliportIcaoRegion("K1")
        .latitude(40.)
        .longitude(-75.)
        .magneticVariation(0.)
        .build();
    ArincRunway runway09 = runway("RW09", 90., 40.);
    ArincRunway runway27 = runway("RW27", 270., 40.01);

    ArincTerminalAreaDatabase database = ArincDatabaseFactory.newTerminalAreaDatabase(
        List.of(),
        List.of(runway09, runway27),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(arincHeliport)
    );
    Heliport heliport = HeliportAssembler.standard(database).assemble(arincHeliport);
    Map<String, ? extends Runway> runways = heliport.runways().stream()
        .collect(Collectors.toMap(Runway::runwayIdentifier, Function.identity()));

    assertAll(
        () -> assertEquals(2, database.heliportsRunwaysAt("HPT1", "K1").size()),
        () -> assertEquals(runway09, database.heliportsRunwayAt("HPT1", "K1", "RW09").orElseThrow()),
        () -> assertEquals(2, runways.size()),
        () -> assertEquals("RW09", runways.get("RW09").runwayIdentifier()),
        () -> assertEquals("RW27", runways.get("RW27").runwayIdentifier())
    );
  }

  @Test
  void testRunwayLocalizersAreProvidedToCustomStrategy() {
    ArincHeliport arincHeliport = ArincHeliport.builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.H)
        .heliportIdentifier("HPT1")
        .heliportIcaoRegion("K1")
        .latitude(40.)
        .longitude(-75.)
        .magneticVariation(0.)
        .build();
    ArincRunway runway = runway("RW09", 90., 40.).toBuilder()
        .ilsMlsGlsIdentifier("IL09")
        .secondaryIlsMlsGlsIdentifier("IS09")
        .build();
    ArincLocalizerGlideSlope primary = localizer("IL09", 1);
    ArincLocalizerGlideSlope secondary = localizer("IS09", 2);

    ArincTerminalAreaDatabase database = ArincDatabaseFactory.newTerminalAreaDatabase(
        List.of(),
        List.of(runway),
        List.of(primary, secondary),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(arincHeliport)
    );
    HeliportAssemblyStrategy<List<CapturedRunway>, CapturedRunway, Void> strategy = new HeliportAssemblyStrategy<>() {
      @Override
      public List<CapturedRunway> convertHeliport(
          ArincHeliport port,
          List<CapturedRunway> convertedRunways,
          List<Void> convertedHelipads
      ) {
        return convertedRunways;
      }

      @Override
      public CapturedRunway convertRunway(
          ArincHeliport heliport,
          ArincRunway origin,
          ArincRunway reciprocal,
          ArincLocalizerGlideSlope primaryLocalizerGlideSlope,
          ArincLocalizerGlideSlope secondaryLocalizerGlideSlope
      ) {
        return new CapturedRunway(
            heliport,
            origin,
            reciprocal,
            primaryLocalizerGlideSlope,
            secondaryLocalizerGlideSlope
        );
      }

      @Override
      public Void convertHelipad(ArincHelipad pad) {
        throw new AssertionError("No helipads should be converted in this test.");
      }

      @Override
      public Optional<Void> convertToHelipad(ArincHeliport port) {
        return Optional.empty();
      }
    };

    List<CapturedRunway> captured = HeliportAssembler.usingStrategy(database, strategy).assemble(arincHeliport);

    assertAll(
        () -> assertEquals(1, captured.size()),
        () -> assertSame(arincHeliport, captured.get(0).heliport()),
        () -> assertSame(runway, captured.get(0).origin()),
        () -> assertNull(captured.get(0).reciprocal()),
        () -> assertSame(primary, captured.get(0).primaryLocalizerGlideSlope()),
        () -> assertSame(secondary, captured.get(0).secondaryLocalizerGlideSlope())
    );
  }

  private ArincRunway runway(String identifier, double bearing, double latitude) {
    return new ArincRunway.Builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.H)
        .airportIdentifier("HPT1")
        .airportIcaoRegion("K1")
        .subSectionCode("G")
        .runwayIdentifier(identifier)
        .runwayMagneticBearing(bearing)
        .runwayLength(5000)
        .latitude(latitude)
        .longitude(-75.0)
        .fileRecordNumber(1)
        .lastUpdateCycle("2501")
        .build();
  }

  private ArincLocalizerGlideSlope localizer(String identifier, int fileRecordNumber) {
    return new ArincLocalizerGlideSlope.Builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.H)
        .airportIdentifier("HPT1")
        .airportIcaoRegion("K1")
        .subSectionCode("I")
        .localizerIdentifier(identifier)
        .continuationRecordNumber("0")
        .runwayIdentifier("RW09")
        .fileRecordNumber(fileRecordNumber)
        .lastUpdateCycle("2501")
        .build();
  }

  private record CapturedRunway(
      ArincHeliport heliport,
      ArincRunway origin,
      ArincRunway reciprocal,
      ArincLocalizerGlideSlope primaryLocalizerGlideSlope,
      ArincLocalizerGlideSlope secondaryLocalizerGlideSlope
  ) {}

}

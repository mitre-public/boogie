package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.IsThisAPrimaryRecord;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;

/**
 * This data is different enough between versions to warrant two test inputs.
 */
public class TestHeliportAssembler {
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));
  private static final File arincTestFile2 = new File(System.getProperty("user.dir").concat("/src/test/resources/kjra_9vak5-and-friends"));
  private static final ArincFileParser fileParser = new ArincFileParser(ArincRecordParser.standard(ArincVersion.V19_NAV.specs()));
  private static final ArincFileParser v22Parser = new ArincFileParser(ArincRecordParser.standard(ArincVersion.V22_NAV.specs()));
  private static final ConvertingArincRecordConsumer testV22Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22_NAV);
  private static final ConvertingArincRecordConsumer testV18Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase19;
  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase22;
  private static HeliportAssembler<Heliport> assembler19;
  private static HeliportAssembler<Heliport> assembler22;

  @BeforeAll
  static void setup() {
    IsThisAPrimaryRecord isThisAPrimaryRecord = new IsThisAPrimaryRecord();
    fileParser.apply(arincTestFile).stream().filter(isThisAPrimaryRecord).forEach(testV18Consumer);
    v22Parser.apply(arincTestFile2).stream().filter(isThisAPrimaryRecord).forEach(testV22Consumer);

    arincTerminalAreaDatabase19 = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV18Consumer.arincAirports(),
        testV18Consumer.arincRunways(),
        testV18Consumer.arincLocalizerGlideSlopes(),
        testV18Consumer.arincNdbNavaids(),
        testV18Consumer.arincVhfNavaids(),
        testV18Consumer.arincWaypoints(),
        testV18Consumer.arincProcedureLegs(),
        testV18Consumer.arincGnssLandingSystems(),
        testV18Consumer.arincHelipads(), //no pads
        testV18Consumer.arincHeliports()
    );

    arincTerminalAreaDatabase22 = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV22Consumer.arincAirports(),
        testV22Consumer.arincRunways(),
        testV22Consumer.arincLocalizerGlideSlopes(),
        testV22Consumer.arincNdbNavaids(),
        testV22Consumer.arincVhfNavaids(),
        testV22Consumer.arincWaypoints(),
        testV22Consumer.arincProcedureLegs(),
        testV22Consumer.arincGnssLandingSystems(),
        testV22Consumer.arincHelipads(),
        testV22Consumer.arincHeliports()
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
    Heliport heliport = ports.get(0);
    Heliport heliport2 = ports.get(1);
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

}

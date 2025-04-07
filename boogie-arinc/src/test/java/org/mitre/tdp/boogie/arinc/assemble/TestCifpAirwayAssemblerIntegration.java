package org.mitre.tdp.boogie.arinc.assemble;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.arinc.EmbeddedCifpFile;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@Tag("CIFP")
@Tag("INTEGRATION")
class TestCifpAirwayAssemblerIntegration {

  private static Multimap<String, Airway> airwaysByName;

  @BeforeAll
  static void setup() {
    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        EmbeddedCifpFile.instance().arincNdbNavaids(),
        EmbeddedCifpFile.instance().arincVhfNavaids(),
        EmbeddedCifpFile.instance().arincWaypoints(),
        EmbeddedCifpFile.instance().arincAirports(),
        EmbeddedCifpFile.instance().arincHoldingPatterns()
    );

    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(arincFixDatabase);

    airwaysByName = assembler.assemble(EmbeddedCifpFile.instance().arincAirwayLegs()).collect(ArrayListMultimap::create, (m, i) -> m.put(i.airwayIdentifier(), i), Multimap::putAll);
  }

  @Test
  void testGlobalAirwayCountIsAccurate() {
    assertEquals(1550, airwaysByName.values().size());
  }

  @Test
  void testCountsByRouteType() {
    Map<String, Long> expectedCounts = newHashMap();
    expectedCounts.put("A", 60L);
    expectedCounts.put("B", 66L);
    expectedCounts.put("G", 40L);
    expectedCounts.put("H", 2L);
    expectedCounts.put("J", 255L);
    expectedCounts.put("L", 34L);
    expectedCounts.put("M", 31L);
    expectedCounts.put("N", 1L);
    expectedCounts.put("Q", 171L);
    expectedCounts.put("R", 46L);
    expectedCounts.put("T", 130L);
    expectedCounts.put("V", 638L);
    expectedCounts.put("W", 1L);
    expectedCounts.put("Y", 75L);

    Map<String, Long> actualCounts = airwaysByName.values().stream()
        .map(airway -> airway.airwayIdentifier().substring(0, 1))
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    assertEquals(expectedCounts, actualCounts);
  }

  @Test
  void testRatioOfInvalidLegsInAssembledAirways() {
    Predicate<Leg> validLeg = new PathTerminatorBasedLegValidator();

    Multimap<Leg, Airway> allLegs = airwaysByName.values().stream()
        .flatMap(airway -> airway.legs().stream().map(leg -> Pair.of(leg, airway)))
        .collect(ArrayListMultimap::create, (m, i) -> m.put(i.first(), i.second()), Multimap::putAll);

    Multimap<Leg, Airway> invalidLegs = allLegs.entries().stream()
        .filter(entry -> validLeg.negate().test(entry.getKey()))
        .collect(ArrayListMultimap::create, (m, i) -> m.put(i.getKey(), i.getValue()), Multimap::putAll);

    double invalidRatio = (double) invalidLegs.size() / (double) allLegs.size();

    assertEquals(0., invalidRatio, .001, "Expected less than .1% of airway legs to be invalid.");
  }
}

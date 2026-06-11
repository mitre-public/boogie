package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.arinc.EmbeddedLidoFile;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

@Tag("LIDO")
@Tag("INTEGRATION")
public class TestLidoAirwayAssemblerIntegration {

  private static Multimap<String, Airway> airwaysByName;

  @BeforeAll
  static void setup() {
    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        EmbeddedLidoFile.instance().arincNdbNavaids(),
        EmbeddedLidoFile.instance().arincVhfNavaids(),
        EmbeddedLidoFile.instance().arincWaypoints(),
        EmbeddedLidoFile.instance().arincAirports(),
        EmbeddedLidoFile.instance().arincHoldingPatterns(),
        EmbeddedLidoFile.instance().arincHeliports()
    );

    AirwayAssembler<Airway> assembler = AirwayAssembler.standard(arincFixDatabase);

    airwaysByName = assembler.assemble(EmbeddedLidoFile.instance().arincAirwayLegs()).collect(ArrayListMultimap::create, (m, i) -> m.put(i.airwayIdentifier(), i), Multimap::putAll);
  }

  @Test
  void testGlobalAirwayCountIsAccurate() {
    assertEquals(14588, airwaysByName.values().size());
  }

  @Test
  void testMaxAssembledSegmentsPerAirwayIdentifier() {
    int maxSegmentsPerIdentifier = airwaysByName.asMap().values().stream()
        .mapToInt(Collection::size)
        .max()
        .orElse(0);

    assertEquals(12, maxSegmentsPerIdentifier, "Observed in A424-22std.dat.gz for V16");
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

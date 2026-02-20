package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.dafif.TestObjects;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

public class AirwayAssemblerTest {

  static AirwayAssembler<Airway> assembler;

  @BeforeAll
  static void setUp() {
    Collection<DafifWaypoint> waypoints = Set.of(
        TestObjects.wptAdrivAA, TestObjects.wptCa400AA, TestObjects.wptMidvuAA,
        TestObjects.wptAndopAS, TestObjects.wptCodieAS, TestObjects.wptTvorAA);
    Collection<DafifNavaid> navaids = Set.of(TestObjects.navTVOR);
    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(waypoints, navaids);

    DafifTerminalAreaDatabase tad = DafifDatabaseFactory.newTerminalAreaDatabase(
        List.of(), List.of(), List.of(), List.of(), List.of());

    FixAssemblyStrategy<Fix> fixStrategy = FixAssemblyStrategy.standard(tad, fdb);
    assembler = AirwayAssembler.standard(fdb, fixStrategy);
  }

  @Test
  void testAssembleJ100ForwardOnly() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());

    assertEquals(1, airways.size(), "J100 forward-only segments should produce one airway");
    Airway j100 = airways.get(0);
    assertAll(
        () -> assertEquals("J100", j100.airwayIdentifier()),
        () -> assertEquals(4, j100.legs().size(), "3 segments should produce 4 legs (N+1)"),
        () -> assertEquals("ADRIV|CA400|MIDVU|ANDOP", fixSequence(j100))
    );
  }

  @Test
  void testAssembleJ100BothDirections() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100AllSegments).collect(Collectors.toList());

    assertEquals(2, airways.size(), "Both directions of J100 should produce two separate airways");
    List<String> identifiers = airways.stream().map(Airway::airwayIdentifier).sorted().collect(Collectors.toList());
    assertEquals(List.of("J100", "J100"), identifiers);
  }

  @Test
  void testForwardDirectionLegTypes() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());
    Airway j100 = airways.get(0);

    assertAll(
        () -> assertEquals(PathTerminator.IF, j100.legs().get(0).pathTerminator(), "Start leg should be IF"),
        () -> assertEquals(PathTerminator.TF, j100.legs().get(1).pathTerminator(), "Subsequent legs should be TF"),
        () -> assertEquals(0, j100.legs().get(0).sequenceNumber(), "Start leg seq should be 0"),
        () -> assertEquals(10, j100.legs().get(1).sequenceNumber(), "First segment leg seq should be 10")
    );
  }

  @Test
  void testAssembleV200() {
    List<Airway> airways = assembler.assemble(TestObjects.atsV200Segments).collect(Collectors.toList());

    assertEquals(1, airways.size(), "V200 single-direction segments should produce one airway");
    Airway v200 = airways.get(0);
    assertAll(
        () -> assertEquals("V200", v200.airwayIdentifier()),
        () -> assertEquals(3, v200.legs().size(), "2 segments should produce 3 legs (N+1)"),
        () -> assertEquals("ANDOP|CODIE|ANDOP", fixSequence(v200))
    );
  }

  @Test
  void testAssembleMultipleAirways() {
    List<DafifAirTrafficSegment> allSegments = new ArrayList<>();
    allSegments.addAll(TestObjects.atsJ100ForwardSegments);
    allSegments.addAll(TestObjects.atsV200Segments);

    List<Airway> airways = assembler.assemble(allSegments).collect(Collectors.toList());

    assertEquals(2, airways.size(), "Should produce two separate airways");
    List<String> identifiers = airways.stream().map(Airway::airwayIdentifier).sorted().collect(Collectors.toList());
    assertEquals(List.of("J100", "V200"), identifiers);
  }

  @Test
  void testLegRouteDistance() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());
    Airway j100 = airways.get(0);

    assertAll(
        () -> assertEquals(Optional.empty(), j100.legs().get(0).routeDistance(), "Start leg should have no distance"),
        () -> assertEquals(Optional.of(15.2), j100.legs().get(1).routeDistance()),
        () -> assertEquals(Optional.of(9.1), j100.legs().get(2).routeDistance()),
        () -> assertEquals(Optional.of(120.5), j100.legs().get(3).routeDistance())
    );
  }

  @Test
  void testStartLegHasFix() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());
    Airway j100 = airways.get(0);

    assertTrue(j100.legs().get(0).associatedFix().isPresent(), "Start leg should have an associated fix");
    assertEquals("ADRIV", j100.legs().get(0).associatedFix().get().fixIdentifier());
  }

  @Test
  void testNavaidFixResolution() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ300Segments).collect(Collectors.toList());

    assertEquals(1, airways.size());
    Airway j300 = airways.get(0);
    assertAll(
        () -> assertEquals("J300", j300.airwayIdentifier()),
        () -> assertEquals(2, j300.legs().size(), "1 segment should produce 2 legs (N+1)"),
        () -> assertEquals("ADRIV", j300.legs().get(0).associatedFix().get().fixIdentifier(),
            "Start fix should be waypoint ADRIV (desc E)"),
        () -> assertEquals("TVOR", j300.legs().get(1).associatedFix().get().fixIdentifier(),
            "End fix should be navaid TVOR resolved via WPT->NAV (desc V)")
    );
  }

  private String fixSequence(Airway airway) {
    return airway.legs().stream()
        .map(Leg::associatedFix)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(Fix::fixIdentifier)
        .collect(Collectors.joining("|"));
  }
}

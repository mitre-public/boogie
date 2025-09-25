package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.model.ProcedureFactory;
import org.mitre.tdp.boogie.model.ProcedureGraph;
import org.mitre.tdp.boogie.pathtermination.LegPathEstimate;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * This is a drop test to make sure that nothing blows up when we run at least JFK through this thing.
 */
public class LegPathEstimateTests {
  static LegPathEstimate estimator = LegPathEstimate.standard();
  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));
  static OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace> records;

  @BeforeAll
  static void setup() {
    try (
        FileInputStream fis = new FileInputStream(arincTestFile)) {
      records = OneshotRecordParser.standard(ArincVersion.V19).assembleFrom(fis);
    } catch (
        IOException e) {
      throw DemotedException.demote("Exception opening and parsing 424 file: " + arincTestFile, e);
    }
  }

  @Test
  void testLengths() {
    assertDoesNotThrow(this::measureThem);
  }

  private void measureThem() {
     records.procedures().stream()
        .filter(i -> i.airportIdentifier().equals("KJFK"))
        .map(p -> ProcedureFactory.newProcedureGraph(p))
        .map(p -> toList(p))
        .map(list -> list.stream().map(l -> estimator.estimateAll(l)).toList())
        .flatMap(Collection::stream)
        .toList();
  }

  private  List<List<Leg>> toList(ProcedureGraph graph) {
    List<Leg> legs = graph.entryLegs((l) -> true);
    Supplier<List<Leg>> approach = () -> graph.transitions().stream()
        .filter(i -> i.transitionType().equals(TransitionType.COMMON))
        .map(t -> t.legs().get(t.legs().size() - 1))
        .map(l -> (Leg) l)
        .toList();
    List<Leg> exits = graph.procedureType().equals(ProcedureType.APPROACH) ? approach.get() : graph.exitLegs((l) -> true);
    return Combinatorics.cartesianProduct(legs, exits).stream()
        .map(p -> graph.pathsBetween(p.first(), p.second()))
        .flatMap(List::stream)
        .toList();
  }


}

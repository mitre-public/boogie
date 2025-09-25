package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.model.ProcedureFactory;
import org.mitre.tdp.boogie.model.ProcedureGraph;
import org.mitre.tdp.boogie.pathtermination.LegPathEstimate;
import org.mitre.tdp.boogie.pathtermination.PathAndTermination;
import org.mitre.tdp.boogie.util.Combinatorics;

@Tag("LIDO")
@Tag("INTEGRATION")
public class LegPathEstimateLidoIntegrationTest {
  static LegPathEstimate estimator = LegPathEstimate.standard();
  static OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace> records;
  static int count;
  @BeforeAll
  static void setup() {
    try (InputStream is = EmbeddedLidoFile.getInputStream()) {
      records = OneshotRecordParser.standard(ArincVersion.V22).assembleFrom(is);
    } catch (
        IOException e) {
      throw DemotedException.demote("Exception parsing embedded CIFP file.", e);
    }
  }

  @Test
  void testAll() {
    assertAll(
        () -> assertEquals(100716, records.procedures().size()),
        () -> assertDoesNotThrow(this::measureThem),
        () -> assertEquals(174526, count)
    );
  }

  private void measureThem() {
    List<Map<Leg, PathAndTermination>> map = records.procedures().stream()
        .map(ProcedureFactory::newProcedureGraph)
        .map(this::toList)
        .map(list -> list.stream().map(l -> estimator.estimateAll(l)).toList())
        .flatMap(Collection::stream)
        .toList();
    count = map.size();
  }

  private  List<List<Leg>> toList(ProcedureGraph graph) {
    List<Leg> legs = graph.entryLegs((l) -> true);
    Supplier<List<Leg>> approach = () -> graph.transitions().stream()
        .filter(i -> i.transitionType().equals(TransitionType.COMMON))
        .map(t -> t.legs().get(t.legs().size() - 1))
        .map(l -> (Leg) l)
        .toList();
    //the graph does not link the final to the missed
    List<Leg> exits = graph.procedureType().equals(ProcedureType.APPROACH) ? approach.get() : graph.exitLegs((l) -> true);
    return Combinatorics.cartesianProduct(legs, exits).stream()
        .map(p -> graph.pathsBetween(p.first(), p.second()))
        .flatMap(List::stream)
        .toList();
  }
}

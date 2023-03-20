package org.mitre.tdp.boogie.convert.routes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.contract.routes.Procedure;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.model.BoogieLeg;
import org.mitre.tdp.boogie.model.BoogieProcedure;
import org.mitre.tdp.boogie.model.BoogieTransition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Range;

public class ProcedureConverterTest {
  private final static org.mitre.tdp.boogie.Fix fix = new BoogieFix.Builder()
      .fixIdentifier("DAVID")
      .fixRegion("DB")
      .latitude(50D)
      .longitude(50D)
      .publishedVariation(15D)
      .build();

  private final static org.mitre.tdp.boogie.Leg leg = new BoogieLeg.Builder()
      .pathTerminator(PathTerminator.TF)
      .sequenceNumber(10)
      .associatedFix(fix)
      .speedConstraint(Range.all())
      .altitudeConstraint(Range.all())
      .build();

  private final static org.mitre.tdp.boogie.Transition transition = new BoogieTransition.Builder()
      .airportIdentifier("KDCA")
      .airportRegion("K1")
      .legs(List.of(leg))
      .transitionIdentifier("ALL")
      .procedureType(ProcedureType.STAR)
      .procedureIdentifier("STAR1")
      .transitionType(TransitionType.COMMON)
      .build();

  private final static org.mitre.tdp.boogie.Procedure procedure = new BoogieProcedure.Builder()
      .procedureIdentifier("STAR1")
      .airportIdentifier("KDCA")
      .airportRegion("K1")
      .transitions(List.of(transition))
      .procedureType(ProcedureType.STAR)
      .requiredNavigationEquipage(RequiredNavigationEquipage.UNKNOWN)
      .build();

  private static final ConvertProcedure procedureConverter = ConvertProcedure.INSTANCE;

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  void testProcedureConvert() {
    Procedure converted = procedureConverter.apply(procedure);
    assertAll("Checking a few items",
        () -> assertNotNull(converted),
        () -> assertEquals("STAR1", converted.procedureIdentifier()),
        () -> assertEquals("STAR1", converted.transitions().stream().findFirst().orElseThrow().procedureIdentifier()),
        () -> assertEquals("K1", converted.airportRegion()),
        () -> assertEquals(ProcedureType.STAR, converted.procedureType()),
        () -> assertDoesNotThrow(() -> mapper.writeValueAsString(converted))
        );
  }
}

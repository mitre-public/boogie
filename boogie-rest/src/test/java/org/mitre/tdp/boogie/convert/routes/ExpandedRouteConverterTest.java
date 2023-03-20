package org.mitre.tdp.boogie.convert.routes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.RouteSummary;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.contract.routes.ExpandedRoute;
import org.mitre.tdp.boogie.model.BoogieFix;
import org.mitre.tdp.boogie.model.BoogieLeg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Range;

public class ExpandedRouteConverterTest {
  private static final RouteSummary routeSummary = new RouteSummary.Builder()
      .route("MDUP")
      .arrivalAirport("KBOS")
      .departureAirport("KDCA")
      .build();

  private final static org.mitre.tdp.boogie.Fix fix = new BoogieFix.Builder()
      .fixIdentifier("DAVID")
      .fixRegion("DB")
      .latitude(50D)
      .longitude(50D)
      .publishedVariation(15D)
      .modeledVariation(43d)
      .build();

  private final static org.mitre.tdp.boogie.Leg leg = new BoogieLeg.Builder()
      .pathTerminator(PathTerminator.TF)
      .sequenceNumber(10)
      .associatedFix(fix)
      .altitudeConstraint(Range.all())
      .speedConstraint(Range.all())
      .isFlyOverFix(false)
      .isPublishedHoldingFix(false)
      .build();

  private static final ExpandedRouteLeg expandedRouteLeg = new ExpandedRouteLeg("WHATEVER", ElementType.FIX, "DONKEY", leg);

  private static final org.mitre.tdp.boogie.alg.ExpandedRoute route = new org.mitre.tdp.boogie.alg.ExpandedRoute(routeSummary, List.of(expandedRouteLeg));
  private static final ConvertExpandedRoute converter = ConvertExpandedRoute.INSTANCE;

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  void testConvert() {
    ExpandedRoute converted = converter.apply(route);
    assertAll("Checking the objects non-null fields",
        () -> assertNotNull(converted),
        () -> assertDoesNotThrow(() -> mapper.writeValueAsString(converted)),
        () -> assertEquals("WHATEVER", converted.legs().stream().findFirst().orElseThrow().section()),
        () -> assertEquals("MDUP", converted.routeSummary().orElseThrow().route()),
        () -> assertEquals("DONKEY", converted.legs().stream().findFirst().orElseThrow().wildcards())
        );
  }
}

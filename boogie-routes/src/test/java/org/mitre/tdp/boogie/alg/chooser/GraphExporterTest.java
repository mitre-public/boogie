package org.mitre.tdp.boogie.alg.chooser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenGrapher;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class GraphExporterTest {

  @Test
  void testLatLong() {
    GraphExporter gex = GraphExporter.INSTANCE;
    assertAll(
        () -> assertTrue(gex.isValid(createLatLongId("4246N/07937W")), "4246N/07937W"),
        () -> assertTrue(gex.isValid(createLatLongId("4500N/09900W")), "4500N/09900W"),
        () -> assertTrue(gex.isValid(createLatLongId("5607N/11142W")), "5607N/11142W")
    );
  }

  private String createLatLongId(String latLong) {

    ResolvedToken token = RouteTokenResolver.latlong(null)
        .resolve(null, RouteToken.standard(latLong, 0), null)
        .resolvedTokens()
        .iterator()
        .next();

    Leg leg = TokenGrapher.standard()
        .graphRepresentationOf(token)
        .iterator()
        .next()
        .source();

    return GraphExporter.INSTANCE.legSignature(leg);
  }
}

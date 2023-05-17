package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class TestLatLonElement {

  @Test
  void testLatLonElementIcaoD() {

    RouteToken split = RouteToken.standard("53N140W", 0.);

    RouteToken split1 = RouteToken.faaIfrBuilder("50N140W", 0.)
        .wildcards("/")
        .build();

    LatLonElement element = LatLonElement.from(split);

    assertEquals(element.toLinkedLegs().size(), 1);

    LinkedLegs linked = element.toLinkedLegs().get(0);

    assertAll(
        () -> assertEquals(LatLong.of(53.0, -140.0), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(LatLong.of(53.0, -140.0), linked.target().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("53N140W", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("53N140W", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.target().pathTerminator())
    );

    element = LatLonElement.from(split1);
    LinkedLegs linked2 = element.toLinkedLegs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerminator.IF, linked2.source().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked2.target().pathTerminator())
    );
  }

  @Test
  void testLatLonElementIcaoDM() {

    RouteToken split = RouteToken.standard("5300N14000W", 0.);

    RouteToken split1 = RouteToken.faaIfrBuilder("5300N14000W", 0.)
        .wildcards("/")
        .build();

    LatLonElement element = LatLonElement.from(split);

    assertEquals(element.toLinkedLegs().size(), 1);

    LinkedLegs linked = element.toLinkedLegs().get(0);

    assertAll(
        () -> assertEquals(LatLong.of(53.0, -140.0), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(LatLong.of(53.0, -140.0), linked.target().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("5300N14000W", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("5300N14000W", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator())
    );

    element = LatLonElement.from(split1);
    LinkedLegs linked2 = element.toLinkedLegs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerminator.IF, linked2.source().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked2.target().pathTerminator())
    );
  }


  @Test
  void testLatLonElementFAA() {

    RouteToken split = RouteToken.standard("5300N/14000W", 0.);

    RouteToken split1 = RouteToken.faaIfrBuilder("5300N/14000W", 0.)
        .wildcards("/")
        .build();

    LatLonElement element = LatLonElement.from(split);

    assertEquals(element.toLinkedLegs().size(), 1);

    LinkedLegs linked = element.toLinkedLegs().get(0);

    assertAll(
        () -> assertEquals(LatLong.of(53.0, -140.0), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(LatLong.of(53.0, -140.0), linked.target().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("5300N/14000W", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("5300N/14000W", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator())
    );

    element = LatLonElement.from(split1);
    LinkedLegs linked2 = element.toLinkedLegs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerminator.IF, linked2.source().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked2.target().pathTerminator())
    );
  }
}

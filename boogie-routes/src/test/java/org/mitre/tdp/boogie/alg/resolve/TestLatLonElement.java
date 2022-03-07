package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestLatLonElement {

  @Test
  void testLatLonElementIcaoD() {
    SectionSplit split = SectionSplit.builder()
        .setValue("53N140W")
        .setWildcards("")
        .build();
    SectionSplit split1 = SectionSplit.builder()
        .setValue("50N140W")
        .setWildcards("/")
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
  void testLatLonElementIcaoDM() {
    SectionSplit split = SectionSplit.builder()
        .setValue("5300N14000W")
        .setWildcards("")
        .build();
    SectionSplit split1 = SectionSplit.builder()
        .setValue("5300N14000W")
        .setWildcards("/")
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
    SectionSplit split = SectionSplit.builder()
        .setValue("5300N/14000W")
        .setWildcards("")
        .build();
    SectionSplit split1 = SectionSplit.builder()
        .setValue("5300N/14000W")
        .setWildcards("/")
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

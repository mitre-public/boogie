package org.mitre.boogie.xml.fixtures;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.Heliport;

class CifpXmlReferencesTest {

  @Test
  void distinguishesAirportAndHeliportWithTheSameIdentifierAndRegion() {
    var refs = new CifpXmlReferences();
    var airport = new Airport();
    var heliport = new Heliport();
    refs.addPort("1AK5", "PA", airport);
    refs.addPort("1AK5", "PA", heliport);

    assertAll(
        () -> assertSame(airport, refs.port("P", "1AK5", "PA")),
        () -> assertSame(heliport, refs.port("H", "1AK5", "PA")),
        () -> assertThrows(IllegalArgumentException.class, () -> refs.port("1AK5", "PA"))
    );
  }
}

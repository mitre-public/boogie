package org.mitre.tdp.boogie.alg.resolve.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;

class TestLatLonElement {

   @Test
    void testLatLonElement() {
     String latLonId = "5300N/14000W";
     LatLonElement element = LatLonElement.from(latLonId, "");

     assertEquals(element.legs().size(), 1);

     LinkedLegs linked = element.legs().get(0);

     assertAll(
         () -> assertEquals(LatLong.of(53.0, -140.0), linked.source().leg().associatedFix().map(Fix::latLong).orElse(null)),
         () -> assertEquals(LatLong.of(53.0, -140.0), linked.target().leg().associatedFix().map(Fix::latLong).orElse(null)),

         () -> assertEquals("5300N/14000W", linked.source().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
         () -> assertEquals("5300N/14000W", linked.target().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

         () -> assertEquals(linked.source().leg().pathTerminator(), PathTerminator.DF),
         () -> assertEquals(linked.source().leg().pathTerminator(), PathTerminator.DF)
     );

     element = LatLonElement.from(latLonId, "/");
     LinkedLegs linked2 = element.legs().iterator().next();

     assertAll(
         () -> assertEquals(PathTerminator.IF, linked2.source().leg().pathTerminator()),
         () -> assertEquals(PathTerminator.IF, linked2.target().leg().pathTerminator())
     );
   }
}

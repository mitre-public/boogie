package org.mitre.boogie.xml.fixtures;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.xml.bind.JAXBContext;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

@Tag("CIFP")
@Tag("INTEGRATION")
@Tag("XML")
class CifpXmlFixtureTest {

  @Test
  void marshalsTheSupportedCifpRecordsWithUniqueIdsAndResolvableReferences() throws Exception {
    var fixture = CifpXmlFixture.load();
    var handler = new PublicationCounts();
    JAXBContext.newInstance(AeroPublication.class).createMarshaller().marshal(fixture.publication(), handler);

    assertAll(
        () -> assertEquals(13779, handler.count("airport")),
        () -> assertEquals(5992, handler.count("heliport")),
        () -> assertEquals(14308, handler.count("runway")),
        () -> assertEquals(1309, handler.count("localizerGlideslope")),
        () -> assertEquals(64887, handler.count("waypoint") + handler.count("terminalWaypoint")),
        () -> assertEquals(969, handler.count("ndb") + handler.count("terminalNdb")),
        () -> assertTrue(handler.count("vhfNavaid") >= 2066, "Includes separate collocated DME/TACAN components"),
        () -> assertEquals(19775, handler.count("airwayLeg")),
        () -> assertEquals(192352, handler.count("procedureLeg"), "Preserves repeated legs and missed approaches"),
        () -> assertEquals(37902, handler.count("airspaceSegment")),
        () -> assertEquals(6466 + fixture.source().arincHelipads().size(), handler.count("helipad")),
        () -> assertTrue(handler.references.size() > 10000, "Contains cross-record references"),
        () -> assertTrue(handler.duplicateIds.isEmpty(), () -> "Duplicate XML IDs: " + handler.duplicateIds),
        () -> assertTrue(handler.ids.containsAll(handler.references), "Every emitted reference has a target"),
        () -> assertEquals(2101, fixture.publication().getCycleDate()),
        () -> assertEquals("2021-01-28T00:00:00Z", fixture.publication().getStartOfValidity().toXMLFormat())
    );
  }

  private static final class PublicationCounts extends DefaultHandler {
    private static final Set<String> REFERENCE_ELEMENTS = Set.of(
        "fixRef", "recNavaidRef", "recommendedNavaidRef", "dmeTacanRef", "portRef", "runwayRef",
        "controlledAirspaceCenterRef");

    private final Map<String, Integer> counts = new HashMap<>();
    private final Set<String> ids = new HashSet<>();
    private final Set<String> duplicateIds = new HashSet<>();
    private final Set<String> references = new HashSet<>();
    private final StringBuilder reference = new StringBuilder();
    private boolean inReference;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
      counts.merge(localName, 1, Integer::sum);
      String id = attributes.getValue("referenceId");
      if (id != null && !ids.add(id)) {
        duplicateIds.add(id);
      }
      inReference = REFERENCE_ELEMENTS.contains(localName);
      reference.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) {
      if (inReference) {
        reference.append(ch, start, length);
      }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
      if (inReference) {
        references.add(reference.toString());
      }
      inReference = false;
    }

    int count(String element) {
      return counts.getOrDefault(element, 0);
    }
  }
}

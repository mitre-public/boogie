package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Dme;
import org.mitre.boogie.xml.v23_4.generated.EastWest;
import org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure;
import org.mitre.boogie.xml.v23_4.generated.Location;
import org.mitre.boogie.xml.v23_4.generated.Ndb;
import org.mitre.boogie.xml.v23_4.generated.NorthSouth;
import org.mitre.boogie.xml.v23_4.generated.StationDeclinationEWT;
import org.mitre.boogie.xml.v23_4.generated.TerminalWaypoint;
import org.mitre.boogie.xml.v23_4.generated.Vor;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

class DafifXmlPointsTest {

  @Test
  void preservesBothRunwayEndsAndSeparatesLandingThresholdFromPhysicalEnd() {
    var runway = runway().lowEndDisplacedThreshold(400).lowEndDisplacedThresholdElevation("00105")
        .lowEndLandingDistance(5600).highEndLandingDistance(6000).build();
    var supplement = DafifAddRunway.builder().airportIdentification("US00001")
        .lowEndRunwayIdentifier("09").highEndRunwayIdentifier("27").icaoCode("KAAA")
        .lowEndDisplacedThresholdDegreesLatitude(40.005).lowEndDisplacedThresholdDegreesLongitude(-70.005)
        .lowEndOverrunDistance(100).highEndOverrunDistance(200).cycleDate(202601).build();
    var records = records(List.of(airport()), List.of(runway), List.of(supplement), List.of(), List.of(), List.of());
    var refs = new DafifXmlReferences();
    var publication = convert(records, refs);

    assertEquals(1, publication.getAirports().getAirport().size());
    var airport = publication.getAirports().getAirport().get(0);
    assertEquals("KAAA", airport.getIdentifier());
    assertSame(airport, refs.airport("US00001"));
    assertSame(airport, refs.airportByIdentifier("KAAA"));
    assertEquals(2, airport.getRunway().size());
    var low = refs.runway("US00001", "09");
    var high = refs.runway("US00001", "27");
    assertNotEquals(low.getReferenceId(), high.getReferenceId());
    assertLocation(low.getLocation(), 40.005, -70.005);
    assertLocation(low.getRunwayEndLocation(), 40.0, -70.0);
    assertLocation(high.getLocation(), 40.01, -70.01);
    assertEquals(105, low.getLandingThresholdElevation());
    assertEquals(100, low.getRunwayEndElevation());
    assertEquals(400L, low.getDisplacedThresholdDistance());
    assertEquals(5600L, low.getLandingDistanceAvailable());
    assertEquals(6000L, high.getLandingDistanceAvailable());
  }

  @Test
  void splitsVorDmeCoordinatesAndPreservesFrequencyUnitsAndWaypointAlias() {
    var vorDme = navaid("ABC", 4).navaidFrequencyNav("112300M").navaidSlavedVariation("W01050120")
        .dmeDegreesLatitude(41.0).dmeDegreesLongitude(-71.0).build();
    var ndb = navaid("NDB", 5).navaidFrequencyNav("346000K").build();
    var alias = waypoint("ALIAS", "US").waypointPointNavaidFlag(true)
        .navaidIdentifier("ABC").navaidCountryCode("US").navaidType(4).navaidKeyCode(1).build();
    var refs = new DafifXmlReferences();
    convert(records(List.of(), List.of(), List.of(), List.of(), List.of(vorDme, ndb), List.of(alias)), refs);

    Vor vor = assertInstanceOf(Vor.class, refs.navaid("ABC", "US", 4, 1));
    Dme dme = assertInstanceOf(Dme.class, vor.getDmeTacanRef());
    assertLocation(vor.getLocation(), 40.0, -70.0);
    assertLocation(dme.getLocation(), 41.0, -71.0);
    assertNotEquals(vor.getReferenceId(), dme.getReferenceId());
    assertEquals(112.3, vor.getVorFrequency().getFrequencyValue().doubleValue(), 1e-9);
    assertEquals(FreqUnitOfMeasure.MEGA_HERTZ, vor.getVorFrequency().getFreqUnitOfMeasure());
    assertEquals(10.5, vor.getStationDeclination().getStationDeclinationValue().doubleValue(), 1e-9);
    assertEquals(StationDeclinationEWT.WEST, vor.getStationDeclination().getStationDeclinationEWT());
    Ndb targetNdb = assertInstanceOf(Ndb.class, refs.navaid("NDB", "US", 5, 1));
    assertEquals(346.0, targetNdb.getNdbFrequency().getFrequencyValue().doubleValue(), 1e-9);
    assertEquals(FreqUnitOfMeasure.KILO_HERTZ, targetNdb.getNdbFrequency().getFreqUnitOfMeasure());
    assertSame(vor, refs.waypointNavaid("ALIAS", "US"));
    assertNotNull(refs.waypoint("ALIAS", "US"));
  }

  @Test
  void combinesLocalizerAndGlideslopeWhilePreservingTheirDifferentLocations() {
    var localizer = ils("Z").navaidFrequency("110300M").degreesLatitude(40.02).degreesLongitude(-70.02)
        .ilsBearingCourse("90.0").ilsSlaveVariation("W011 0120").localizerWidth(5.0).build();
    var glideSlope = ils("G").navaidFrequency("335000M").degreesLatitude(40.003).degreesLongitude(-70.003)
        .ilsGlideSlopeAngle(3.0).thresholdCrossingHeight(50).build();
    var refs = new DafifXmlReferences();
    var publication = convert(records(List.of(airport()), List.of(runway().build()), List.of(),
        List.of(glideSlope, localizer), List.of(), List.of()), refs);

    var systems = publication.getAirports().getAirport().get(0).getLocalizerGlideslope();
    assertEquals(1, systems.size());
    var target = systems.get(0);
    assertLocation(target.getLocation(), 40.02, -70.02);
    assertLocation(target.getGlideslopeLocation(), 40.003, -70.003);
    assertEquals(3.0, target.getApproachAngle().doubleValue(), 1e-9);
    assertEquals(110.3, target.getLocalizerGlideslopeFrequency().getFrequencyValue().doubleValue(), 1e-9);
    assertEquals(11.0, target.getStationDeclination().getStationDeclinationValue().doubleValue(), 1e-9);
    assertEquals(StationDeclinationEWT.WEST, target.getStationDeclination().getStationDeclinationEWT());
    assertSame(refs.runway("US00001", "09"), target.getRunwayRef());
    assertSame(target, refs.localizer("US00001", "IABC"));
  }

  @Test
  void preservesLocalizersWithTheSameIdentifierOnDifferentRunwaysWithoutAnAmbiguousReference() {
    var low = ils("Z").runwayIdentifier("09").degreesLatitude(40.02).degreesLongitude(-70.02).build();
    var high = ils("Z").runwayIdentifier("27").degreesLatitude(40.03).degreesLongitude(-70.03).build();
    var refs = new DafifXmlReferences();
    var publication = convert(records(List.of(airport()), List.of(runway().build()), List.of(),
        List.of(low, high), List.of(), List.of()), refs);

    var localizers = publication.getAirports().getAirport().get(0).getLocalizerGlideslope();
    assertEquals(2, localizers.size());
    assertNotEquals(localizers.get(0).getReferenceId(), localizers.get(1).getReferenceId());
    assertSame(refs.runway("US00001", "09"), localizers.get(0).getRunwayRef());
    assertSame(refs.runway("US00001", "27"), localizers.get(1).getRunwayRef());
    assertNull(refs.localizer("US00001", "IABC"));
  }

  @Test
  void keepsSameNamedWaypointsFromDifferentCountriesAndUsesAirportAssociation() {
    var terminal = waypoint("SAME", "US").icaoCode("KAAA").waypointUsageCode("T").build();
    var enroute = waypoint("SAME", "CA").icaoCode("CY").degreesLatitude(50.0).degreesLongitude(-80.0).build();
    var refs = new DafifXmlReferences();
    var publication = convert(records(List.of(airport()), List.of(), List.of(), List.of(), List.of(),
        List.of(terminal, enroute)), refs);

    var terminalTarget = refs.waypoint("SAME", "US");
    var enrouteTarget = refs.waypoint("SAME", "CA");
    assertInstanceOf(TerminalWaypoint.class, terminalTarget);
    assertSame(terminalTarget, publication.getAirports().getAirport().get(0).getTerminalWaypoint().get(0));
    assertSame(enrouteTarget, publication.getEnrouteWaypoints().getWaypoint().get(0));
    assertNotEquals(terminalTarget.getReferenceId(), enrouteTarget.getReferenceId());
    assertLocation(enrouteTarget.getLocation(), 50.0, -80.0);
  }

  private static AeroPublication convert(DafifXmlFixture.Records records, DafifXmlReferences refs) {
    var publication = new AeroPublication();
    DafifXmlPoints.populate(records, publication, refs);
    return publication;
  }

  private static DafifXmlFixture.Records records(List<DafifAirport> airports, List<DafifRunway> runways,
                                                List<DafifAddRunway> supplements, List<DafifIls> ils,
                                                List<DafifNavaid> navaids, List<DafifWaypoint> waypoints) {
    return new DafifXmlFixture.Records(airports, runways, supplements, ils, navaids, waypoints,
        List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
  }

  private static DafifAirport airport() {
    return DafifAirport.builder().airportIdentification("US00001").icaoCode("KAAA").name("Test Airport")
        .airportType("C").elevation(100).degreesLatitude(40.0).degreesLongitude(-70.0)
        .mageneticVariation("W010000 0124").cycleDate(202601).build();
  }

  private static DafifRunway.Builder runway() {
    return DafifRunway.builder().airportIdentification("US00001").lowEndIdentifier("09").highEndIdentifier("27")
        .length(6000).width(100).surface("ASP").lowEndMagneticHeading(90.0).highEndMagneticHeading(270.0)
        .lowEndDegreesLatitude(40.0).lowEndDegreesLongitude(-70.0).lowEndElevation("00100")
        .highEndDegreesLatitude(40.01).highEndDegreesLongitude(-70.01).highEndElevation("00110")
        .lowEndLightingSystem(List.of()).highEndLightingSystem(List.of()).cycleDate(202601);
  }

  private static DafifNavaid.Builder navaid(String identifier, int type) {
    return DafifNavaid.builder().navaidIdentifier(identifier).countryCode("US").navaidType(type).navaidKeyCode(1)
        .icaoRegion("K1").degreesLatitude(40.0).degreesLongitude(-70.0).magneticVariation("W010000 0124")
        .ilsNavaidElevation("00100").dmeElevation("00110").cycleDate(202601);
  }

  private static DafifWaypoint.Builder waypoint(String identifier, String country) {
    return DafifWaypoint.builder().waypointIdentifier(identifier).countryCode(country).icaoCode("K1")
        .waypointType("I").waypointUsageCode("B").degreesLatitude(40.0).degreesLongitude(-70.0)
        .magneticVariation(-10.0).waypointPointNavaidFlag(false).cycleDate(202601);
  }

  private static DafifIls.Builder ils(String component) {
    return DafifIls.builder().airportIdentification("US00001").runwayIdentifier("09").componentType(component)
        .ilsNavaidIdentifier("IABC").ilsNavaidElevation("00100").ilsMlsCategory("1")
        .magneticVariation("W010000 0124").cycleDate(202601);
  }

  private static void assertLocation(Location location, double latitude, double longitude) {
    var lat = location.getLatitude();
    double actualLatitude = lat.getDeg() + lat.getMin() / 60.0 + lat.getSec() / 3600.0 + lat.getHSec() / 360000.0;
    if (lat.getNorthSouth() == NorthSouth.SOUTH) {
      actualLatitude = -actualLatitude;
    }
    var lon = location.getLongitude();
    double actualLongitude = lon.getDeg() + lon.getMin() / 60.0 + lon.getSec() / 3600.0 + lon.getHSec() / 360000.0;
    if (lon.getEastWest() == EastWest.WEST) {
      actualLongitude = -actualLongitude;
    }
    assertEquals(latitude, actualLatitude, 0.000002);
    assertEquals(longitude, actualLongitude, 0.000002);
  }
}

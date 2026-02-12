package org.mitre.tdp.boogie.dafif;

 import java.util.List;

 import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
 import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

public class TestObjects {
  public static final DafifNavaid aa = DafifNavaid.builder()
      .navaidIdentifier("AA")
      .countryCode("UI")
      .navaidType(5)
      .navaidSlavedVariation(null)
      .cycleDate(201907)
      .degreesLatitude(47.009053)
      .degreesLongitude(-96.815183)
      .build();

  public static final DafifNavaid abr = DafifNavaid.builder()
      .navaidIdentifier("ABR")
      .navaidType(4)
      .navaidSlavedVariation("E00700188")
      .cycleDate(202110)
      .degreesLatitude(45.417356)
      .degreesLongitude(-98.368719)
      .build();

  public static final DafifWaypoint fakeAA = DafifWaypoint.builder()
      .waypointIdentifier("AA")
      .countryCode("UI")
      .waypointType("NR")
      .waypointPointNavaidFlag(true)
      .degreesLatitude(47.009053)
      .degreesLongitude(-96.815183)
      .build();

  public static final DafifAirport fakeAirport = DafifAirport.builder()
      .airportIdentification("FAKE1")
      .icaoCode("IDK1")
      .faaHostCountryIdentifier("AAA")
      .cycleDate(202301)
      .degreesLatitude(12.35)
      .degreesLongitude(23.34)
      .magVarOfRecord("E002065")
      .cycleDate(202110)
      .build();

  public static final DafifRunway rwy1129 = DafifRunway.builder()
      .lowEndIdentifier("11")
      .highEndIdentifier("29")
      .airportIdentification("FAKE1")
      .highEndMagneticHeading(295.2)
      .lowEndMagneticHeading(115.1)
      .length(9310)
      .width(152)
      .surface("ASP")
      .pavementClassificationNumber("071FAWT")
      .highEndGeodeticLatitude("N12305415")
      .highEndDegreesLatitude(12.508132)
      .highEndGeodeticLongitude("W070002137")
      .highEndDegreesLongitude(-70.018742)
      .highEndElevation("69")
      .highEndSlope("-0.4")
      .highEndTDZE("U")
      .highEndDisplacedThreshold(95)
      .highEndDisplacedThresholdElevation("69")
      .highEndLightingSystem(List.of(12, 6, 49))
      .lowEndGeodeticLatitude("N12311655")
      .lowEndDegreesLatitude(12.514902)
      .lowEndGeodeticLongitude("W070013827")
      .lowEndDegreesLongitude(-70.024611)
      .lowEndElevation("18")
      .lowEndSlope("0.4")
      .lowEndTDZE("U")
      .lowEndDisplacedThreshold(260)
      .lowEndDisplacedThresholdElevation("18")
      .lowEndLightingSystem(List.of(12, 6, 49))
      .trueHeadingHighEnd(284.2)
      .trueHeadingLowEnd(104.0)
      .highEndLandingDistance(8960)
      .highEndRunwayDistance(9040)
      .highEndTakeOffDistance(9040)
      .highEndAccelerateStopDistance(9040)
      .lowEndLandingDistance(8960)
      .lowEndRunwayDistance(9230)
      .lowEndTakeOffDistance(9230)
      .lowEndAccelerateStopDistance(9230)
      .cycleDate(202404)
      .coordinatePrecision(1)
      .build();

  public static final DafifAddRunway addRunway = DafifAddRunway.builder()
      .airportIdentification("FAKE1")
      .highEndRunwayIdentifier("29")
      .lowEndRunwayIdentifier("11")
      .icaoCode("IDK1")
      .highEndDisplacedThresholdGeodeticLatitude("N12295464")
      .highEndDisplacedThresholdDegreesLatitude(12.498511)
      .highEndDisplacedThresholdGeodeticLongitude("W070001147")
      .highEndDisplacedThresholdDegreesLongitude(-70.003186)
      .highEndOverrunDistance(186)
      .highEndOverrunSurface("ASP")
      .highEndOverrunGeodeticLatitude("N12301657")
      .highEndOverrunDegreesLatitude(12.504604)
      .highEndOverrunGeodeticLongitude("W070014349")
      .highEndOverrunDegreesLongitude(-70.028749)
      .lowEndDisplacedThresholdGeodeticLatitude("N12301550")
      .lowEndDisplacedThresholdDegreesLatitude(12.504306)
      .lowEndDisplacedThresholdGeodeticLongitude("W070013897")
      .lowEndDisplacedThresholdDegreesLongitude(-70.027492)
      .lowEndOverrunSurface("U")
      .cycleDate(202403)
      .build();

  public static final  DafifIls fakeIls = DafifIls.builder()
      .runwayIdentifier("11")
      .airportIdentification("FAKE1")
      .ilsNavaidIdentifier("I234")
      .countryCode("AAA")
      .cycleDate(202110)
      .degreesLatitude(12.32)
      .degreesLongitude(23.32)
      .build();
}

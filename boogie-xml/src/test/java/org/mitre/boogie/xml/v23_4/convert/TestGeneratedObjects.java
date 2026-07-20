package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.DatatypeFactory;

import org.mitre.boogie.xml.v23_4.generated.*;

final class TestGeneratedObjects {
  private TestGeneratedObjects() {}

  static Location newLocation(double lat, double lon) {
    Latitude latitude = new Latitude();
    latitude.setDeg((int) Math.abs(lat));
    latitude.setMin((int) ((Math.abs(lat) - Math.abs((int) lat)) * 60));
    latitude.setSec(0);
    latitude.setHSec(0);
    latitude.setNorthSouth(lat >= 0 ? NorthSouth.NORTH : NorthSouth.SOUTH);
    latitude.setDecimalDegreesLatitude(BigDecimal.valueOf(lat));

    Longitude longitude = new Longitude();
    longitude.setDeg((int) Math.abs(lon));
    longitude.setMin((int) ((Math.abs(lon) - Math.abs((int) lon)) * 60));
    longitude.setSec(0);
    longitude.setHSec(0);
    longitude.setEastWest(lon >= 0 ? EastWest.EAST : EastWest.WEST);
    longitude.setDecimalDegreesLongitude(BigDecimal.valueOf(lon));

    Location location = new Location();
    location.setLatitude(latitude);
    location.setLongitude(longitude);
    return location;
  }

  static Bearing newBearing(double value, boolean isTrueBearing) {
    Bearing bearing = new Bearing();
    bearing.setBearingValue(BigDecimal.valueOf(value));
    bearing.setIsTrueBearing(isTrueBearing);
    return bearing;
  }

  static HelipadDimensions newHelipadDimensions(int longSide, int shortSide, int diameter) {
    HelipadDimensions dims = new HelipadDimensions();
    dims.setPadLengthLongSide(longSide);
    dims.setPadLengthShortSide(shortSide);
    dims.setPadDiameter(diameter);
    return dims;
  }

  static Runway newValidRunway() {
    Runway runway = new Runway();
    runway.setRecordType(RecordType.STANDARD);
    runway.setCycleDate("2501");
    runway.setIcaoCode("K6");
    runway.setIdentifier("RW09L");
    runway.setLocation(newLocation(38.9, -77.0));

    RunwayIdentifier id = new RunwayIdentifier();
    id.setRunwayNumber(9);
    id.setRunwayLeftRightCenterType(RunwayLeftRightCenterType.LEFT);
    runway.setRunwayIdentifier(id);

    runway.setRunwayLength(10000L);
    runway.setRunwayWidth(150L);
    runway.setRunwayBearing(newBearing(90.5, true));
    runway.setRunwayTrueBearing(BigDecimal.valueOf(90.5));
    runway.setRunwayGradient(BigDecimal.valueOf(0.3));
    runway.setLtpEllipsoidHeight(BigDecimal.valueOf(100.5));
    runway.setLandingThresholdElevation(313);
    runway.setDisplacedThresholdDistance(1000L);
    runway.setRunwayEndLocation(newLocation(38.91, -77.01));
    runway.setRunwayEndElevation(310);
    runway.setStopway(500L);
    runway.setThresholdCrossingHeight(50L);
    runway.setTouchDownZoneElevation(312);
    runway.setStarterExtension(200);
    runway.setTakeOffRunwayAvailable(9500L);
    runway.setTakeOffDistanceAvailable(9800L);
    runway.setAccelerateStopDistanceAvailable(10000L);
    runway.setLandingDistanceAvailable(9000L);
    runway.setSurfaceCode(RunwaySurfaceCode.HARD);
    runway.setRunwayUsageIndicator(RunwayUsageIndicator.TAKEOFF_AND_LANDING);
    runway.setIsWithoutLocation(false);
    runway.setIsDerivedLocation(false);
    runway.setRunwayDescription("Main runway");

    Runway.RunwayAccuracy accuracy = new Runway.RunwayAccuracy();
    accuracy.setRunwayAccuracyCompliance(RunwayAccuracyCompliance.COMPLIANT);
    accuracy.setLandingThresholdElevationAccuracyCompliance(LandingThresholdElevationAccuracyCompliance.COMPLIANT);
    runway.setRunwayAccuracy(accuracy);

    return runway;
  }

  static AirportGate newValidAirportGate() {
    AirportGate gate = new AirportGate();
    gate.setRecordType(RecordType.STANDARD);
    gate.setCycleDate("2501");
    gate.setIcaoCode("K6");
    gate.setIdentifier("A12");
    gate.setName("Gate A12");
    gate.setLocation(newLocation(38.9, -77.0));
    return gate;
  }

  static Frequency newFrequency(double value, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure unit) {
    Frequency freq = new Frequency();
    freq.setFrequencyValue(BigDecimal.valueOf(value));
    freq.setFreqUnitOfMeasure(unit);
    return freq;
  }

  static StationDeclination newStationDeclination(double value, StationDeclinationEWT ewt) {
    StationDeclination sd = new StationDeclination();
    sd.setStationDeclinationValue(BigDecimal.valueOf(value));
    sd.setStationDeclinationEWT(ewt);
    return sd;
  }

  static Vor newValidVor() {
    Vor vor = new Vor();
    vor.setRecordType(RecordType.STANDARD);
    vor.setCycleDate("2501");
    vor.setIcaoCode("K6");
    vor.setIdentifier("DCA");
    vor.setLocation(newLocation(38.85, -77.04));
    vor.setVorFrequency(newFrequency(113.1, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ));
    VhfNavaidClass navaidClass = new VhfNavaidClass();
    navaidClass.setVhfNavaidCoverage(VhfNavaidCoverage.HIGH);
    navaidClass.setVhfNavaidWeatherInfo(NavaidWeatherInfo.AUTOMATED);
    navaidClass.setIsNotCollocated(false);
    navaidClass.setIsBiased(false);
    navaidClass.setIsNoVoice(false);
    vor.setNavaidClass(navaidClass);
    vor.setStationDeclination(newStationDeclination(3.5, StationDeclinationEWT.WEST));
    vor.setFigureOfMerit(FigureOfMerit.HIGH_ALT);
    vor.setFrequencyProtection(200L);
    vor.setNavaidSynchronization(NavaidSynchronization.SYNCHRONOUS);
    vor.setVorVoice(NavaidVoice.VOICE_IDENT);
    vor.setVorRangePower(VorRangePower.NM_130_FEET_60000_LEGACY);
    vor.setPortRef("PORT-DCA");
    vor.setDmeTacanRef("DME-DCA");
    vor.setElevation(300);
    vor.setIsVFRCheckpoint(false);
    return vor;
  }

  static Dme newValidDme() {
    Dme dme = new Dme();
    dme.setRecordType(RecordType.STANDARD);
    dme.setCycleDate("2501");
    dme.setIcaoCode("K6");
    dme.setIdentifier("DME1");
    dme.setLocation(newLocation(38.85, -77.04));
    dme.setFrequency(newFrequency(110.3, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ));

    VhfNavaidClass navaidClass = new VhfNavaidClass();
    navaidClass.setVhfNavaidCoverage(VhfNavaidCoverage.TERMINAL);
    navaidClass.setVhfNavaidWeatherInfo(NavaidWeatherInfo.AUTOMATED);
    navaidClass.setIsNotCollocated(false);
    navaidClass.setIsBiased(false);
    navaidClass.setIsNoVoice(false);
    dme.setNavaidClass(navaidClass);

    dme.setPortRef("PORT-DME1");
    dme.setElevation(300);
    dme.setIsVFRCheckpoint(false);
    dme.setIlsDmeBias(BigDecimal.valueOf(0.25));
    dme.setIsIlsComponent(true);
    dme.setIlsDmeLocation(IlsDmeLocation.COLLOCATED_LOCALIZER);
    dme.setDmeExpandedServiceVolume(DmeExpandedServiceVolume.NM_130_FEET_18000);
    dme.setDmeOperationalServiceVolume("T");
    dme.setIsRouteInappropriateDme(false);
    dme.setIsPaired(false);
    dme.setIsMlsP(false);
    return dme;
  }

  static Tacan newValidTacan() {
    Tacan tacan = new Tacan();
    tacan.setRecordType(RecordType.STANDARD);
    tacan.setCycleDate("2501");
    tacan.setIcaoCode("K6");
    tacan.setIdentifier("TAC1");
    tacan.setLocation(newLocation(38.86, -77.05));
    tacan.setFrequency(newFrequency(112.0, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ));

    VhfNavaidClass navaidClass = new VhfNavaidClass();
    navaidClass.setVhfNavaidCoverage(VhfNavaidCoverage.TERMINAL);
    navaidClass.setVhfNavaidWeatherInfo(NavaidWeatherInfo.AUTOMATED);
    navaidClass.setIsNotCollocated(false);
    navaidClass.setIsBiased(false);
    navaidClass.setIsNoVoice(false);
    tacan.setNavaidClass(navaidClass);

    tacan.setPortRef("PORT-TAC1");
    tacan.setElevation(310);
    tacan.setIsVFRCheckpoint(false);
    tacan.setStationDeclination(newStationDeclination(2.0, StationDeclinationEWT.EAST));
    tacan.setIlsDmeBias(BigDecimal.valueOf(0.5));
    tacan.setIsIlsComponent(false);
    tacan.setIlsDmeLocation(IlsDmeLocation.NOT_COLLOCATED);
    tacan.setDmeExpandedServiceVolume(DmeExpandedServiceVolume.NM_130_FEET_60000);
    tacan.setDmeOperationalServiceVolume("H");
    tacan.setIsRouteInappropriateDme(false);
    tacan.setIsPaired(true);
    tacan.setIsMlsP(false);
    return tacan;
  }

  static MilitaryTacan newValidMilitaryTacan() {
    MilitaryTacan tacan = new MilitaryTacan();
    tacan.setRecordType(RecordType.STANDARD);
    tacan.setCycleDate("2501");
    tacan.setIcaoCode("K6");
    tacan.setIdentifier("MTAC1");
    tacan.setLocation(newLocation(38.87, -77.06));
    tacan.setFrequency(newFrequency(114.0, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ));

    VhfNavaidClass navaidClass = new VhfNavaidClass();
    navaidClass.setVhfNavaidCoverage(VhfNavaidCoverage.TERMINAL);
    navaidClass.setVhfNavaidWeatherInfo(NavaidWeatherInfo.AUTOMATED);
    navaidClass.setIsNotCollocated(false);
    navaidClass.setIsBiased(false);
    navaidClass.setIsNoVoice(false);
    tacan.setNavaidClass(navaidClass);

    tacan.setPortRef("PORT-MTAC1");
    tacan.setElevation(320);
    tacan.setIsVFRCheckpoint(false);
    tacan.setStationDeclination(newStationDeclination(4.0, StationDeclinationEWT.WEST));
    tacan.setIlsDmeBias(BigDecimal.valueOf(0.75));
    tacan.setIsIlsComponent(false);
    tacan.setIlsDmeLocation(IlsDmeLocation.COLLOCATED_GLIDE_SLOPE);
    tacan.setDmeExpandedServiceVolume(DmeExpandedServiceVolume.NM_130_FEET_18000);
    tacan.setDmeOperationalServiceVolume("T");
    tacan.setIsRouteInappropriateDme(false);
    tacan.setIsPaired(false);
    tacan.setIsMlsP(false);
    return tacan;
  }

  static Ndb newValidNdb() {
    Ndb ndb = new Ndb();
    ndb.setRecordType(RecordType.STANDARD);
    ndb.setCycleDate("2501");
    ndb.setIcaoCode("K6");
    ndb.setIdentifier("DC");
    ndb.setLocation(newLocation(38.86, -77.05));
    ndb.setNdbFrequency(newFrequency(382.0, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.KILO_HERTZ));
    NdbNavaidClass ndbClass = new NdbNavaidClass();
    ndbClass.setIsBfoRequired(false);
    ndbClass.setNdbNavaidCoverage(org.mitre.boogie.xml.v23_4.generated.NdbNavaidCoverage.HIGH_POWER_NDB);
    ndbClass.setNdbNavaidIfMarker(NdbNavaidIfMarkerInfo.OUTER_MARKER);
    ndbClass.setNdbNavaidType(org.mitre.boogie.xml.v23_4.generated.NdbNavaidType.NDB);
    ndbClass.setNdbNavaidWeatherInfo(NavaidWeatherInfo.AUTOMATED);
    ndbClass.setIsNoVoice(true);
    ndb.setNdbClass(ndbClass);
    ndb.setTypeOfEmission(400L);
    ndb.setRepetitionRate(1020L);
    ndb.setNavaidNdbEmissionType(NavaidNdbEmissionType.CARRIER_KEYED);
    ndb.setNdbVoice(NavaidVoice.NO_VOICE_IDENT);
    ndb.setDmeTacanRef("DME-DC");
    ndb.setElevation(250);
    ndb.setIsVFRCheckpoint(true);
    return ndb;
  }

  static PortCommunication newValidPortCommunication() {
    PortCommunication comm = new PortCommunication();
    comm.setRecordType(RecordType.STANDARD);
    comm.setCycleDate("2501");
    comm.setLocation(newLocation(38.85, -77.04));
    comm.setCallSign("DCA TWR");
    comm.setCommunicationClass(CommunicationClass.ATCF);
    comm.setCommunicationType(CommunicationType.TWR);
    comm.setFrequencyUnits(FrequencyUnits.VHF_NON_STANDARD);
    comm.setRadarService(Radar.PRIMARY_OR_SECONDARY);
    comm.setH24Indicator(H24Indicator.CONTINUOUS);
    comm.setModulation(Modulation.AM_FREQ);
    comm.setSequenceNumber(1);
    comm.setSignalEmission(SignalEmission.USB_CARRIER_UNK);
    comm.setTransmitFrequency(newFrequency(120.75, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ));
    comm.setReceiveFrequency(newFrequency(120.75, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ));
    comm.setTransmitterSiteElevation(300);
    comm.setCommunicationDistance(25L);
    comm.setRemoteFacilityRef("REMOTE-FAC-1");
    comm.setDistanceDescription(DistanceDescription.OUT_TO_SPECIFIED_DISTANCE);
    return comm;
  }

  static LocalizerGlideslope newValidLocalizerGlideslope() {
    LocalizerGlideslope lg = new LocalizerGlideslope();
    lg.setRecordType(RecordType.STANDARD);
    lg.setCycleDate("2501");
    lg.setIcaoCode("K6");
    lg.setIdentifier("IDCA");
    lg.setLocation(newLocation(38.85, -77.04));
    lg.setLocalizerGlideslopeFrequency(newFrequency(110.3, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.MEGA_HERTZ));
    lg.setApproachAngle(BigDecimal.valueOf(3.0));
    lg.setApproachCourseBearing(newBearing(9.5, true));
    lg.setCategory(PrecisionApproachCategory.ILS_MLS_GLS_CAT_1);
    RunwayIdentifier lgRunway = new RunwayIdentifier();
    lgRunway.setRunwayNumber(1);
    lgRunway.setRunwayLeftRightCenterType(RunwayLeftRightCenterType.CENTER);
    lg.setRunwayIdentifier(lgRunway);
    lg.getApproachRouteIdent().add("I01C");
    lg.setGlideslopeBeamWidth(BigDecimal.valueOf(1.4));
    lg.setGlideslopeHeightAtLandingThreshold(50L);
    lg.setGlideslopeLocation(newLocation(38.851, -77.041));
    lg.setGlideslopePosition(300L);
    lg.setLocalizerPosition(1000L);
    lg.setLocalizerPositionReference(LocalizerAzimuthPositionReference.BEYOND_STOP_END);
    lg.setLocalizerTrueBearing(BigDecimal.valueOf(9.5));
    lg.setLocalizerTrueBearingSource(TrueBearingSource.OFFICIAL);
    lg.setLocalizerWidth(BigDecimal.valueOf(5.0));
    lg.setStationDeclination(newStationDeclination(3.5, StationDeclinationEWT.WEST));
    lg.setIlsBackCourse(IlsBackCourse.UNUSABLE);
    lg.setIlsDmeLocation(IlsDmeLocation.COLLOCATED_LOCALIZER);
    VhfNavaidClass lgNavaidClass = new VhfNavaidClass();
    lgNavaidClass.setVhfNavaidCoverage(VhfNavaidCoverage.TERMINAL);
    lgNavaidClass.setVhfNavaidWeatherInfo(NavaidWeatherInfo.AUTOMATED);
    lgNavaidClass.setIsNotCollocated(false);
    lgNavaidClass.setIsBiased(false);
    lgNavaidClass.setIsNoVoice(true);
    lg.setNavaidClass(lgNavaidClass);
    lg.setSupportingFacilityReference("SUPP-FAC-IDCA");
    lg.setRunwayRef("RWY-01C");
    lg.setElevation(313);
    lg.setIsVFRCheckpoint(false);
    return lg;
  }

  static AirportHeliportLocalizerMarker newValidLocalizerMarker() {
    AirportHeliportLocalizerMarker marker = new AirportHeliportLocalizerMarker();
    marker.setRecordType(RecordType.STANDARD);
    marker.setCycleDate("2501");
    marker.setIcaoCode("K6");
    marker.setIdentifier("OM01C");
    marker.setLocation(newLocation(38.84, -77.05));
    marker.setElevation(280);
    marker.setLocatorFacilityCharacteristics("LOM");
    marker.setLocatorFrequency(newFrequency(350.0, org.mitre.boogie.xml.v23_4.generated.FreqUnitOfMeasure.KILO_HERTZ));
    marker.setMarkerType(MarkerType.OM);
    marker.setMinorAxisBearing(BigDecimal.valueOf(180.0));
    RunwayIdentifier markerRunway = new RunwayIdentifier();
    markerRunway.setRunwayNumber(1);
    markerRunway.setRunwayLeftRightCenterType(RunwayLeftRightCenterType.CENTER);
    marker.setRunwayIdentifier(markerRunway);
    LocatorClass locatorClass = new LocatorClass();
    locatorClass.setAhLocalizerMarkerLocatorFacility1(AhLocalizerMarkerLocatorFacility1.NDB);
    locatorClass.setAhLocalizerMarkerLocatorFacility2(AhLocalizerMarkerLocatorFacility2.OUTER_MARKER);
    locatorClass.setAhLocalizerMarkerLocatorCoverage(AhLocalizerMarkerLocatorCoverage.HIGH_POWER_NDB);
    locatorClass.setAhLocalizerMarkerLocatorAddInfo(AhLocalizerMarkerLocatorAddInfo.NO_VOICE_ON_FREQUENCY);
    locatorClass.setAhLocalizerMarkerWeatherInfo(NavaidWeatherInfo.AUTOMATED);
    locatorClass.setAhLocalizerMarkerLocatorCollocation(AhLocalizerMarkerLocatorCollocation.LOCATOR_MARKER_COLLOCATED);
    marker.setLocalizerRef("LOC-IDCA");
    marker.setLocatorClass(locatorClass);
    return marker;
  }

  static Sector newSector(long altitude, long bearingBegin, long bearingEnd, long radius) {
    Sector sector = new Sector();
    sector.setSectorAltitude(altitude);
    sector.setSectorBearingBegin(bearingBegin);
    sector.setSectorBearingEnd(bearingEnd);
    sector.setSectorRadius(radius);
    return sector;
  }

  static Msa newValidMsa() {
    Msa msa = new Msa();
    msa.setRecordType(RecordType.STANDARD);
    msa.setCycleDate("2501");
    msa.setCenterFix("DCA");
    msa.setIcaoCode("K6");
    msa.setMagneticTrueIndicator(MagneticTrueIndicator.MAGNETIC);
    msa.setMultipleCode("A");
    msa.setCenterFixRef("FIX-DCA");
    msa.getSector().add(newSector(2500, 0, 180, 25));
    msa.getSector().add(newSector(3000, 180, 360, 25));
    return msa;
  }

  static TaaSectorDetails newTaaSectorDetails(long altitude, long bearingBegin, long bearingEnd, long radius, long radiusStart, long radiusEnd, boolean procedureTurn) {
    TaaSectorDetails sd = new TaaSectorDetails();
    sd.setSectorAltitude(altitude);
    sd.setSectorBearingBegin(bearingBegin);
    sd.setSectorBearingEnd(bearingEnd);
    sd.setSectorRadius(radius);
    sd.setSectorRadiusStart(radiusStart);
    sd.setSectorRadiusEnd(radiusEnd);
    sd.setProcedureTurn(procedureTurn);
    return sd;
  }

  static Taa newValidTaa() {
    Taa taa = new Taa();
    taa.setRecordType(RecordType.STANDARD);
    taa.setCycleDate("2501");
    taa.setTaaFixPositionIndicator(TaaSectorIdentifier.STRAIGHT_IN_OR_CENTER_FIX);
    taa.setMagneticTrueIndicator(MagneticTrueIndicator.MAGNETIC);
    taa.getApproachTypeIdentifier().add("I01C");
    taa.setTaaIafWaypointRef("WPT-IAF-1");
    taa.setSectorBearingWaypointRef("WPT-SECTOR-1");
    taa.getSectorTaaDetails().add(newTaaSectorDetails(2500, 0, 180, 25, 0, 25, false));
    taa.getSectorTaaDetails().add(newTaaSectorDetails(3000, 180, 360, 25, 0, 25, true));
    taa.setReferenceId("TAA-DCA-1");
    return taa;
  }

  static Gls newValidGls() {
    Gls gls = new Gls();
    gls.setRecordType(RecordType.STANDARD);
    gls.setCycleDate("2501");
    gls.setIcaoCode("K6");
    gls.setIdentifier("G01C");
    gls.setLocation(newLocation(38.85, -77.04));
    gls.setGlsChannel(21000);
    gls.setApproachAngle(BigDecimal.valueOf(3.0));
    gls.setApproachCourseBearing(newBearing(9.5, true));
    gls.setCategory(PrecisionApproachCategory.ILS_MLS_GLS_CAT_1);
    RunwayIdentifier glsRunway = new RunwayIdentifier();
    glsRunway.setRunwayNumber(1);
    glsRunway.setRunwayLeftRightCenterType(RunwayLeftRightCenterType.CENTER);
    gls.setRunwayIdentifier(glsRunway);
    gls.setServiceVolumeRadius(20L);
    gls.setStationElevationWgs84(313);
    gls.setStationType("LAAS");
    gls.setTdmaSlots("01");
    gls.setThresholdCrossingHeight(50L);
    gls.setRunwayRef("RWY-01C");
    gls.setElevation(313);
    gls.setIsVFRCheckpoint(false);
    return gls;
  }

  static Helipad newValidHelipad() {
    Helipad helipad = new Helipad();
    helipad.setRecordType(RecordType.STANDARD);
    helipad.setCycleDate("2501");
    helipad.setIcaoCode("K6");
    helipad.setIdentifier("H1");
    helipad.setName("Helipad 1");
    helipad.setLocation(newLocation(38.9, -77.0));

    helipad.setElevation(300);
    helipad.setElevationType(ElevationType.LANDING_THRESHOLD);
    helipad.setHelipadShape(HelipadShape.CIRCLE);
    helipad.setSurfaceCode(RunwaySurfaceCode.HARD);
    helipad.setHelicopterPerformanceReq(HelicopterPerformanceReq.MULTI_ENGINE);
    helipad.setIsElevated(false);
    helipad.setIsWithoutLocation(false);
    helipad.setIsDerivedLocation(false);
    helipad.setHelipadMaximumRotorDiameter(50);
    helipad.setHelipadOrientation(newBearing(180.0, true));
    helipad.setHelipadIdentifierOrientation(newBearing(90.0, false));
    helipad.getPreferredApproachBearing().add(newBearing(270.0, true));
    helipad.getPreferredApproachBearing().add(newBearing(90.0, true));
    helipad.setHelipadTlofDimensions(newHelipadDimensions(30, 20, 25));
    helipad.setHelipadFatoDimensions(newHelipadDimensions(40, 30, 35));
    helipad.setSafetyDimensions(newHelipadDimensions(50, 40, 45));
    return helipad;
  }

  static Course newCourse(double value, boolean isTrue) {
    Course course = new Course();
    course.setCourseValue(BigDecimal.valueOf(value));
    course.setIsTrue(isTrue);
    return course;
  }

  static AirwayLeg newValidAirwayLeg(long seqNum, String fixIdent) {
    AirwayLeg leg = new AirwayLeg();
    leg.setRecordType(RecordType.STANDARD);
    leg.setCycleDate("2501");
    leg.setSequenceNumber(seqNum);
    leg.setFixIdent(fixIdent);
    leg.setFixRef("FIX-" + fixIdent);
    leg.setRecNavaidIdent("DCA");
    leg.setRecNavaidRef("NAVAID-DCA");
    leg.setCruiseTableRef("CRUISE-TBL-1");
    leg.setAirwayRouteType(EnrouteAirwayRouteType.OFFICIALLY_DESIGNATED_EXCEPT_RNAV_HELICOPTER);
    leg.setLevel(Level.HIGH_ALT);
    leg.setLegDirectionRestriction(EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD);
    leg.setOutboundCourse(newCourse(90.5, true));
    leg.setInboundCourse(newCourse(270.5, false));
    leg.setRouteDistanceFrom(BigDecimal.valueOf(125.3));
    leg.setRho(BigDecimal.valueOf(50.0));
    leg.setTheta(BigDecimal.valueOf(180.0));
    leg.setRnp(BigDecimal.valueOf(2.0));
    leg.setVerticalScaleFactor(100L);
    return leg;
  }

  static HoldingPattern newValidHoldingPattern() {
    HoldingPattern hp = new HoldingPattern();
    hp.setRecordType(RecordType.STANDARD);
    hp.setCycleDate("2501");
    hp.setIsEnroute(true);
    hp.setArcRadius(BigDecimal.valueOf(4.0));
    hp.setHoldingPatternName("HOLD01");
    hp.setHoldingSpeed(230L);
    hp.setHoldingTime(DatatypeFactory.newDefaultInstance().newDuration("PT4M"));
    hp.setHoldingDistance(BigDecimal.valueOf(5.0));
    hp.setInboundHoldingCourse(newCourse(270.0, false));
    hp.setLegInboundOutboundIndicator(LegInboundOutboundIndicator.INBOUND);
    hp.setTurnDirection(Turn.RIGHT);
    hp.setVerticalScaleFactor(100L);
    hp.setFixIdentifier("FIX01");
    hp.setFixRef("FIX-FIX01");
    hp.setPortRef("PORT-K6");
    hp.setMultipleIndicator(BigInteger.ONE);
    hp.setInboundCourseNavaid("NAVAID-DCA");
    hp.setInboundCourseTheta(BigDecimal.valueOf(180.0));

    HoldRvsmMinimumMaximumAltitudeConstraint holdMinMax = new HoldRvsmMinimumMaximumAltitudeConstraint();
    RouteMinimumAltitude holdMin = new RouteMinimumAltitude();
    holdMin.setAltitude(5000);
    RouteMaximumAltitude holdMax = new RouteMaximumAltitude();
    holdMax.setAltitude(8000);
    holdMinMax.setMinimumAltitude(holdMin);
    holdMinMax.setMaximumAltitude(holdMax);
    hp.setHoldMinMaxAltitudes(holdMinMax);

    HoldRvsmMinimumMaximumAltitudeConstraint rvsmMinMax = new HoldRvsmMinimumMaximumAltitudeConstraint();
    RouteMinimumAltitude rvsmMin = new RouteMinimumAltitude();
    rvsmMin.setAltitude(10000);
    RouteMaximumAltitude rvsmMax = new RouteMaximumAltitude();
    rvsmMax.setAltitude(18000);
    rvsmMinMax.setMinimumAltitude(rvsmMin);
    rvsmMinMax.setMaximumAltitude(rvsmMax);
    hp.setRvsmMinMaxLevels(rvsmMinMax);

    HoldingUses uses = new HoldingUses();
    uses.setIsOnHigh(true);
    uses.setIsOnLow(false);
    uses.setIsOnSid(true);
    uses.setIsOnStar(false);
    uses.setIsOnApproach(true);
    uses.setIsOnMissedApproach(false);
    hp.setHoldingUses(uses);

    return hp;
  }

  static Airway newValidAirway() {
    Airway airway = new Airway();
    airway.setIdentifier("J60");
    airway.setRecordType(RecordType.STANDARD);
    airway.setCustomerCode("USA");
    airway.setAirwayRouteType(EnrouteAirwayRouteType.OFFICIALLY_DESIGNATED_EXCEPT_RNAV_HELICOPTER);
    airway.setQualifier1(AirwayQualifier1.GNSS_REQUIRED);
    airway.getAirwayLeg().add(newValidAirwayLeg(10, "WYPT1"));
    airway.getAirwayLeg().add(newValidAirwayLeg(20, "WYPT2"));
    return airway;
  }

  static SidLeg newValidSidLeg(long seqNum) {
    SidLeg leg = new SidLeg();
    leg.setRecordType(RecordType.STANDARD);
    leg.setCycleDate("2501");
    leg.setSequenceNumber(seqNum);
    leg.setPathAndTermination(PathAndTermination.TF);
    return leg;
  }

  static StarLeg newValidStarLeg(long seqNum) {
    StarLeg leg = new StarLeg();
    leg.setRecordType(RecordType.STANDARD);
    leg.setCycleDate("2501");
    leg.setSequenceNumber(seqNum);
    leg.setPathAndTermination(PathAndTermination.TF);
    return leg;
  }

  static ApproachLeg newValidApproachLeg(long seqNum) {
    ApproachLeg leg = new ApproachLeg();
    leg.setRecordType(RecordType.STANDARD);
    leg.setCycleDate("2501");
    leg.setSequenceNumber(seqNum);
    leg.setPathAndTermination(PathAndTermination.TF);
    return leg;
  }

  static Constraint newConstraint(int altitude, boolean isFlightLevel) {
    Constraint c = new Constraint();
    c.setAltitude(altitude);
    c.setIsFlightLevel(isFlightLevel);
    return c;
  }

  static SpeedLimit newSpeedLimit(long at, long atOrAbove, long atOrBelow) {
    SpeedLimit sl = new SpeedLimit();
    sl.setAt(at);
    sl.setAtOrAbove(atOrAbove);
    sl.setAtOrBelow(atOrBelow);
    return sl;
  }

  static AltitudeConstraint newAltitudeConstraint() {
    AltitudeConstraint ac = new AltitudeConstraint();
    ac.setAt(newConstraint(5000, false));
    ac.setAtOrAbove(newConstraint(40, true));
    ac.setAtOrBelow(newConstraint(80, true));
    return ac;
  }

  static AltitudeTermination newAltitudeTermination() {
    AltitudeTermination at = new AltitudeTermination();
    at.setTerminationType(AltitudeTerminationType.LEG_TERMINATION);
    at.setAltitude(newConstraint(60, true));
    return at;
  }

  static ProcedureLeg.RaceTrackAltitude newRaceTrackAltitude() {
    ProcedureLeg.RaceTrackAltitude rt = new ProcedureLeg.RaceTrackAltitude();
    rt.setAtOrAbove(newConstraint(3000, false));
    return rt;
  }

  static MagneticVariation newMagneticVariation() {
    MagneticVariation mv = new MagneticVariation();
    mv.setMagneticVariationEWT(MagneticVariationEWT.WEST);
    mv.setMagneticVariationValue(BigDecimal.valueOf(3.5));
    return mv;
  }

  static ProcedureWaypointDescription newProcedureWaypointDescription() {
    ProcedureWaypointDescription wpd = new ProcedureWaypointDescription();
    wpd.setIsEssential(true);
    wpd.setIsFlyOver(false);
    wpd.setIsHolding(true);
    wpd.setIsNoProcedureTurn(false);
    wpd.setIsPhantomFix(false);
    wpd.setIsSourceProvidedEnrouteWaypoint(true);
    wpd.setIsTaaProcedureTurn(false);
    wpd.setIsAtcCompulsoryReportingPoint(true);
    return wpd;
  }

  static ApproachWaypointDescription newApproachWaypointDescription() {
    ApproachWaypointDescription awd = new ApproachWaypointDescription();
    awd.setIsFacf(true);
    awd.setIsMissedApproachPoint(false);
    awd.setIsFaf(true);
    awd.setIsFinalEndPoint(false);
    awd.setIsFixTurningFinalApproach(true);
    awd.setIsInitialApproachFix(false);
    awd.setIsIntermediateApproachFix(true);
    awd.setStepDownFix(StepDownFix.UNNAMED_IN_FINAL_APPROACH_SEGMENT);
    return awd;
  }

  private static void populateProcedureLegFields(ProcedureLeg leg) {
    leg.setFixIdent("WYPT1");
    leg.setFixRef("FIX-WYPT1");
    leg.setRecNavaidIdent("DCA");
    leg.setRecNavaidRef("NAVAID-DCA");
    leg.setTurnDirection(TurnDirection.LEFT);
    leg.setIsTurnDirectionValid(true);
    leg.setArcRadius(BigDecimal.valueOf(5.0));
    leg.setAtcIndicator(AtcIndicator.MODIFIED_OR_ASSIGNED);
    leg.setDistance(BigDecimal.valueOf(10.5));
    leg.setLegDistance(BigDecimal.valueOf(12.3));
    leg.setLegInboundIndicator("A");
    leg.setLegInboundOutboundIndicator(LegInboundOutboundIndicator.INBOUND);
    leg.setRho(BigDecimal.valueOf(50.0));
    leg.setRnp(BigDecimal.valueOf(2.0));
    leg.setTheta(BigDecimal.valueOf(180.0));
    leg.setCourse(newCourse(90.5, true));
    leg.setCenterFix("CTR01");
    leg.setCenterFixRef("CTRFIX-01");
    leg.setTransitionsAltitudeLevel(18000);
    leg.setVerticalScaleFactor(100L);
    leg.setHoldTime(DatatypeFactory.newDefaultInstance().newDuration("PT4M"));
    leg.setProcedureDesignMagVar(newMagneticVariation());
    leg.setSpeedLimit(newSpeedLimit(250L, 200L, 300L));
    leg.setAltitudeConstraint(newAltitudeConstraint());
    leg.setAltitudeTermination(newAltitudeTermination());
    leg.setRaceTrackAltitude(newRaceTrackAltitude());
    leg.setWaypointDescriptor(newProcedureWaypointDescription());
  }

  static SidLeg newFullSidLeg(long seqNum) {
    SidLeg leg = new SidLeg();
    leg.setRecordType(RecordType.STANDARD);
    leg.setCycleDate("2501");
    leg.setSequenceNumber(seqNum);
    leg.setPathAndTermination(PathAndTermination.TF);
    populateProcedureLegFields(leg);
    leg.setIsEngineOutDisarmPoint(true);
    leg.setIsInitialDepartureFix(false);
    leg.setIsQuietClimbRestorePoint(true);
    return leg;
  }

  static StarLeg newFullStarLeg(long seqNum) {
    StarLeg leg = new StarLeg();
    leg.setRecordType(RecordType.STANDARD);
    leg.setCycleDate("2501");
    leg.setSequenceNumber(seqNum);
    leg.setPathAndTermination(PathAndTermination.TF);
    populateProcedureLegFields(leg);
    leg.setVerticalAngle(BigDecimal.valueOf(-3.0));
    return leg;
  }

  static ApproachLeg newFullApproachLeg(long seqNum) {
    ApproachLeg leg = new ApproachLeg();
    leg.setRecordType(RecordType.STANDARD);
    leg.setCycleDate("2501");
    leg.setSequenceNumber(seqNum);
    leg.setPathAndTermination(PathAndTermination.TF);
    populateProcedureLegFields(leg);
    leg.setVerticalAngle(BigDecimal.valueOf(-3.0));
    leg.setGlideSlopeCrossingAltitude(200);
    leg.setApproachWaypointDescription(newApproachWaypointDescription());
    return leg;
  }

  static RouteQualifications newRouteQualifications() {
    RouteQualifications rq = new RouteQualifications();
    rq.setIsDmeReq(true);
    rq.setIsGnssReq(false);
    rq.setIsRadarReq(true);
    rq.setIsFmsReq(false);
    rq.setIsConventional(true);
    return rq;
  }

  static SidRunwayTransition newValidSidRunwayTransition() {
    SidRunwayTransition srt = new SidRunwayTransition();
    srt.setIdentifier("RW09L");
    srt.setRnavPbnNavSpec(RnavPbnNavSpec.RNAV_1);
    srt.setRnpPbnNavSpec(RnpPbnNavSpec.RNP_1);
    srt.setRouteQualifications(newRouteQualifications());
    srt.setIsFromRunway(true);
    srt.getProcedureLeg().add(newValidSidLeg(10));
    srt.getProcedureLeg().add(newValidSidLeg(20));
    return srt;
  }

  static SidEnrouteTransition newValidSidEnrouteTransition() {
    SidEnrouteTransition set = new SidEnrouteTransition();
    set.setIdentifier("GREKI");
    set.setRnavPbnNavSpec(RnavPbnNavSpec.RNAV_1);
    set.setRouteQualifications(newRouteQualifications());
    set.getProcedureLeg().add(newValidSidLeg(30));
    return set;
  }

  static StarRunwayTransition newValidStarRunwayTransition() {
    StarRunwayTransition srt = new StarRunwayTransition();
    srt.setIdentifier("RW01C");
    srt.setRnavPbnNavSpec(RnavPbnNavSpec.RNAV_1);
    srt.setRouteQualifications(newRouteQualifications());
    srt.setIsToRunway(true);
    srt.getProcedureLeg().add(newValidStarLeg(10));
    return srt;
  }

  static StarEnrouteTransition newValidStarEnrouteTransition() {
    StarEnrouteTransition set = new StarEnrouteTransition();
    set.setIdentifier("FRDMM");
    set.setRnavPbnNavSpec(RnavPbnNavSpec.RNAV_1);
    set.setRouteQualifications(newRouteQualifications());
    set.getProcedureLeg().add(newValidStarLeg(10));
    return set;
  }

  static FinalApproach newValidFinalApproach() {
    FinalApproach fa = new FinalApproach();
    fa.setIdentifier("I01C");
    fa.setQualifier1(ApproachQualifier1.DME_REQUIRED);
    fa.setIsRemoteAltimeterRestricted(false);
    fa.setBaroVnavNotAuthorized(true);
    fa.setProcedureTch(50L);
    fa.setGlideSlopeInterceptAltitude(2000);
    fa.getProcedureLeg().add(newValidApproachLeg(10));
    fa.getProcedureLeg().add(newValidApproachLeg(20));
    return fa;
  }

  static ApproachTransition newValidApproachTransition() {
    ApproachTransition at = new ApproachTransition();
    at.setIdentifier("GNDLF");
    at.setQualifier1(ApproachQualifier1.DME_REQUIRED);
    at.setMultipleIndicator("A");
    at.setIsTfOverlay(false);
    at.getProcedureLeg().add(newValidApproachLeg(10));
    return at;
  }

  static MissedApproach newValidMissedApproach() {
    MissedApproach ma = new MissedApproach();
    ma.setIdentifier("MA01C");
    ma.setQualifier1(ApproachQualifier1.DME_REQUIRED);
    ma.getProcedureLeg().add(newValidApproachLeg(10));
    return ma;
  }

  static Sid newValidSid() {
    Sid sid = new Sid();
    sid.setIdentifier("RNAV1");
    sid.setReferenceId("SID-RNAV1");
    sid.setRecordType(RecordType.STANDARD);
    sid.setIsRnav(true);
    sid.setIsHelicopterOnlyProcedure(false);
    sid.setIsMilitary(false);
    sid.setIsSpecial(false);
    sid.setIsVorDmeRnav(false);
    sid.setRnavPbnNavSpec(RnavPbnNavSpec.RNAV_1);
    sid.setIsEngineOut(false);
    sid.setIsVector(true);
    sid.setIsPInS(false);
    sid.setIsPInSProceedVisually(false);
    sid.setIsPInSProceedVfr(false);
    sid.setProcedureName("RNAV ONE DEPARTURE");
    sid.setLongIdent("RNAV1.DEP");
    sid.getSidRunwayTransition().add(newValidSidRunwayTransition());
    sid.getSidEnrouteTransition().add(newValidSidEnrouteTransition());
    return sid;
  }

  static Star newValidStar() {
    Star star = new Star();
    star.setIdentifier("FRDMM3");
    star.setReferenceId("STAR-FRDMM3");
    star.setRecordType(RecordType.STANDARD);
    star.setIsRnav(true);
    star.setIsHelicopterOnlyProcedure(false);
    star.setIsMilitary(false);
    star.setIsSpecial(false);
    star.setIsVorDmeRnav(false);
    star.setRnavPbnNavSpec(RnavPbnNavSpec.RNAV_1);
    star.setIsContinuousDescent(true);
    star.setProcedureName("FREEDOM THREE ARRIVAL");
    star.getStarEnrouteTransition().add(newValidStarEnrouteTransition());
    star.getStarRunwayTransition().add(newValidStarRunwayTransition());
    return star;
  }

  static Approach newValidApproach() {
    Approach approach = new Approach();
    approach.setIdentifier("I01C");
    approach.setReferenceId("APR-I01C");
    approach.setRecordType(RecordType.STANDARD);
    approach.setIsRnav(false);
    approach.setIsHelicopterOnlyProcedure(false);
    approach.setIsMilitary(false);
    approach.setIsSpecial(false);
    approach.setApproachRouteType(ApproachRouteType.ILS);
    approach.setGnssFmsIndicator(GnssFmsIndicator.FMS_OVERLAY_AUTHORIZED);
    approach.setIsRnavVisual(false);
    approach.setIsPInSProceedVisually(false);
    approach.setIsPInSProceedVfr(false);
    approach.setIsLocalizerBackcourse(false);
    approach.setIsPreferredProcedure(true);
    approach.setCategoryARadius(BigDecimal.valueOf(1.0));
    approach.setCategoryBRadius(BigDecimal.valueOf(1.5));
    approach.setCategoryCRadius(BigDecimal.valueOf(2.0));
    approach.setCategoryDRadius(BigDecimal.valueOf(2.5));
    approach.setProcedureName("ILS RWY 01C");
    approach.getApproachTransition().add(newValidApproachTransition());
    approach.setFinalApproach(newValidFinalApproach());
    approach.getMissedApproach().add(newValidMissedApproach());
    return approach;
  }

  static Airport newValidAirport() {
    Airport airport = new Airport();
    airport.setRecordType(RecordType.STANDARD);
    airport.setCycleDate("2501");
    airport.setIcaoCode("K6");
    airport.setIdentifier("KDCA");
    airport.setName("Ronald Reagan Washington National");
    airport.setLocation(newLocation(38.852, -77.037));
    airport.setElevation(15);
    airport.setLongestRunway(6869L);
    airport.setLongestRunwaySurfaceCode(RunwaySurfaceCode.HARD);
    airport.setMagneticVariation(newMagneticVariation());
    airport.getRunway().add(newValidRunway());
    airport.getAirportGate().add(newValidAirportGate());
    airport.getHelipad().add(newValidHelipad());
    TerminalProcedures terminalProcedures = new TerminalProcedures();
    terminalProcedures.getSid().add(newValidSid());
    terminalProcedures.getStar().add(newValidStar());
    terminalProcedures.getApproach().add(newValidApproach());
    airport.setTerminalProcedures(terminalProcedures);
    return airport;
  }

  static Heliport newValidHeliport() {
    Heliport heliport = new Heliport();
    heliport.setRecordType(RecordType.STANDARD);
    heliport.setCycleDate("2501");
    heliport.setIcaoCode("K6");
    heliport.setIdentifier("6N5");
    heliport.setName("Penn Yan Heliport");
    heliport.setLocation(newLocation(40.748, -73.997));
    heliport.setElevation(30);
    heliport.setHeliportType(HeliportType.HOSPITAL);
    heliport.setMagneticVariation(newMagneticVariation());
    heliport.getHelipad().add(newValidHelipad());
    return heliport;
  }
}

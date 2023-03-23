package org.mitre.tdp.boogie.arinc.assemble;

import static org.mitre.tdp.boogie.util.Declinations.declination;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.arinc.utils.AiracCycle;
import org.mitre.tdp.boogie.model.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

/**
 * Collection of converter methods for transferring from {@link ArincModel} classes to implementations of various Boogie interfaces.
 */
public final class ArincToBoogieConverterFactory {

  private ArincToBoogieConverterFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class");
  }

  public static FixAssembler<BoogieFix> newFixAssembler() {
    return new FixAssembler<>(
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom,
        ArincToBoogieConverterFactory::newFixFrom
    );
  }

  public static AirportAssembler<BoogieAirport, BoogieRunway> newAirportAssembler(TerminalAreaDatabase terminalAreaDatabase) {
    return new AirportAssembler<>(terminalAreaDatabase,
        ArincToBoogieConverterFactory::newAirportFrom,
        ArincToBoogieConverterFactory::newRunwayFrom);
  }

  public static AirwayAssembler<BoogieAirway, BoogieFix, BoogieLeg> newAirwayAssembler(FixDatabase fixDatabase) {
    return new AirwayAssembler<>(
        fixDatabase,
        newFixAssembler(),
        ArincToBoogieConverterFactory::newAirwayLegFrom,
        ArincToBoogieConverterFactory::newAirwayFrom

    );
  }

  public static ProcedureAssembler<BoogieProcedure, BoogieTransition, BoogieLeg, BoogieFix> newProcedureAssembler(TerminalAreaDatabase terminalAreaDatabase, FixDatabase fixDatabase) {
    return new ProcedureAssembler<>(
        terminalAreaDatabase,
        fixDatabase,
        newFixAssembler(),
        ArincToBoogieConverterFactory::newProcedureLegFrom,
        ArincToBoogieConverterFactory::newTransitionFrom,
        ArincToBoogieConverterFactory::newProcedureFrom
        );
  }

  static BoogieFix newFixFrom(ArincWaypoint waypoint) {

    double modeledDeclination = declination(
        waypoint.latitude(),
        waypoint.longitude(),
        null,
        AiracCycle.startDate(waypoint.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(waypoint.waypointIdentifier())
        .fixRegion(waypoint.waypointIcaoRegion())
        .latitude(waypoint.latitude())
        .longitude(waypoint.longitude())
        .publishedVariation(waypoint.magneticVariation().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  static BoogieFix newFixFrom(ArincNdbNavaid ndbNavaid) {
    double modeledDeclination = declination(
        ndbNavaid.latitude(),
        ndbNavaid.longitude(),
        null,
        AiracCycle.startDate(ndbNavaid.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(ndbNavaid.ndbIdentifier())
        .fixRegion(ndbNavaid.ndbIcaoRegion())
        .latitude(ndbNavaid.latitude())
        .longitude(ndbNavaid.longitude())
        .publishedVariation(ndbNavaid.magneticVariation().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  static BoogieFix newFixFrom(ArincVhfNavaid vhfNavaid) {
    double modeledDeclination = declination(
        vhfNavaid.latitude(),
        vhfNavaid.longitude(),
        vhfNavaid.dmeElevation().orElse(null),
        AiracCycle.startDate(vhfNavaid.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(vhfNavaid.vhfIdentifier())
        .fixRegion(vhfNavaid.vhfIcaoRegion())
        .latitude(vhfNavaid.latitude())
        .longitude(vhfNavaid.longitude())
        .elevation(vhfNavaid.dmeElevation().orElse(null))
        .publishedVariation(vhfNavaid.stationDeclination().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  static BoogieFix newFixFrom(ArincAirport airport) {
    double modeledDeclination = declination(
        airport.latitude(),
        airport.longitude(),
        airport.airportElevation().orElse(null),
        AiracCycle.startDate(airport.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(airport.airportIdentifier())
        .fixRegion(airport.airportIcaoRegion())
        .latitude(airport.latitude())
        .longitude(airport.longitude())
        .elevation(airport.airportElevation().orElse(null))
        .publishedVariation(airport.magneticVariation().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  static BoogieFix newFixFrom(ArincRunway runway) {
    double modeledDeclination = declination(
        runway.latitude(),
        runway.longitude(),
        runway.landingThresholdElevation().map(Integer::doubleValue).orElse(null),
        AiracCycle.startDate(runway.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(runway.runwayIdentifier())
        .fixRegion(runway.airportIcaoRegion())
        .latitude(runway.latitude())
        .longitude(runway.longitude())
        .elevation(runway.landingThresholdElevation().map(Integer::doubleValue).orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  static BoogieFix newFixFrom(ArincGnssLandingSystem gls) {
    double modeledDeclination = declination(
        gls.stationLatitude(),
        gls.stationLongitude(),
        Optional.ofNullable(gls.stationElevation()).filter(Optional::isPresent).map(Optional::get).orElse(null),
        AiracCycle.startDate(gls.lastUpdatedCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(gls.glsRefPathIdentifier())
        .fixRegion(gls.airportIcaoRegion())
        .latitude(gls.stationLatitude())
        .longitude(gls.stationLongitude())
        .elevation(Optional.ofNullable(gls.stationElevation()).filter(Optional::isPresent).map(Optional::get).orElse(null))
        .modeledVariation(modeledDeclination)
        .publishedVariation(gls.magneticVariation())
        .build();
  }

  static BoogieFix newFixFrom(ArincLocalizerGlideSlope localizerGlideSlope) {

    double modeledDeclination = declination(
        localizerGlideSlope.localizerLatitude().orElseGet(() -> localizerGlideSlope.glideSlopeLatitude().orElseThrow(IllegalStateException::new)),
        localizerGlideSlope.localizerLongitude().orElseGet(() -> localizerGlideSlope.glideSlopeLongitude().orElseThrow(IllegalStateException::new)),
        localizerGlideSlope.glideSlopeElevation().orElse(null),
        AiracCycle.startDate(localizerGlideSlope.lastUpdateCycle())
    );

    return new BoogieFix.Builder()
        .fixIdentifier(localizerGlideSlope.localizerIdentifier())
        .fixRegion(localizerGlideSlope.airportIcaoRegion())
        .latitude(localizerGlideSlope.localizerLatitude().orElseGet(() -> localizerGlideSlope.glideSlopeLatitude().orElseThrow(IllegalStateException::new)))
        .longitude(localizerGlideSlope.localizerLongitude().orElseGet(() -> localizerGlideSlope.glideSlopeLongitude().orElseThrow(IllegalStateException::new)))
        .elevation(localizerGlideSlope.glideSlopeElevation().orElse(null))
        .publishedVariation(localizerGlideSlope.stationDeclination().orElse(null))
        .modeledVariation(modeledDeclination)
        .build();
  }

  static BoogieAirport newAirportFrom(ArincAirport arincAirport, List<BoogieRunway> runways) {
    return new BoogieAirport.Builder()
        .airportIdentifier(arincAirport.airportIdentifier())
        .airportRegion(arincAirport.airportIcaoRegion())
        .latitude(arincAirport.latitude())
        .longitude(arincAirport.longitude())
        .elevation(arincAirport.airportElevation().orElse(null))
        .publishedVariation(arincAirport.magneticVariation().orElse(null))
        .modeledVariation(magneticVariationAt(arincAirport))
        .runways(runways)
        .build();
  }

  static double magneticVariationAt(ArincAirport arincAirport) {
    return declination(
        arincAirport.latitude(),
        arincAirport.longitude(),
        arincAirport.airportElevation().orElse(null),
        AiracCycle.startDate(arincAirport.lastUpdateCycle())
    );
  }

  /**
   * Generates a new concrete {@link Runway} instance using the provided context from airport, arrival/departure runway ends, and
   * including context from the localizer/glideslope(s) at the runway.
   */
  static BoogieRunway newRunwayFrom(
      ArincAirport arincAirport,
      ArincRunway arrivalEnd,
      @Nullable ArincRunway departureEnd,
      @Nullable ArincLocalizerGlideSlope primaryLocalizer,
      @Nullable ArincLocalizerGlideSlope secondaryLocalizer) {

    MagneticVariation variation = new MagneticVariation(
        arincAirport.magneticVariation().orElse(null),
        magneticVariationAt(arincAirport)
    );

    return new BoogieRunway.Builder()
        .airportIdentifier(arrivalEnd.airportIdentifier())
        .airportRegion(arrivalEnd.airportIcaoRegion())
        .runwayIdentifier(arrivalEnd.runwayIdentifier())
        .length(arrivalEnd.runwayLength().map(Integer::doubleValue).orElse(null))
        .width(arrivalEnd.runwayWidth().map(Integer::doubleValue).orElse(null))
        .arrivalRunwayEnd(LatLong.of(arrivalEnd.latitude(), arrivalEnd.longitude()))
        .departureRunwayEnd(Optional.ofNullable(departureEnd).map(end -> LatLong.of(end.latitude(), end.longitude())).orElse(null))
        .trueCourse(arrivalEnd.runwayMagneticBearing().map(variation::magneticToTrue).orElseGet(() ->
            Optional.ofNullable(departureEnd).map(end -> Spherical.courseBtw(
                arrivalEnd.latitude(),
                arrivalEnd.longitude(),
                end.latitude(),
                end.longitude()
            )).map(Course::inDegrees).orElse(null)
        ))
        .ilsGlsMls1(Optional.ofNullable(primaryLocalizer).map(ArincLocalizerGlideSlope::localizerIdentifier).orElse(null))
        .ilsGlsMls2(Optional.ofNullable(secondaryLocalizer).map(ArincLocalizerGlideSlope::localizerIdentifier).orElse(null))
        .landingThresholdElevation(arrivalEnd.landingThresholdElevation().orElse(null))
        .departureRunwayEndElevation(Optional.ofNullable(departureEnd).flatMap(ArincRunway::landingThresholdElevation).orElse(null))
        .build();
  }

  static BoogieLeg newAirwayLegFrom(ArincAirwayLeg arincAirwayLeg, @Nullable Fix associatedFix, @Nullable Fix recommendedNavaid) {
    return new BoogieLeg.Builder()
        .associatedFix(associatedFix)
        .recommendedNavaid(recommendedNavaid)
        .pathTerminator(PathTerminator.TF)
        .sequenceNumber(arincAirwayLeg.sequenceNumber())
        .altitudeConstraint(AirwayAltitudeRange.INSTANCE.apply(
            arincAirwayLeg.minAltitude1().orElse(null),
            arincAirwayLeg.minAltitude2().orElse(null),
            arincAirwayLeg.maxAltitude().orElse(null))
        )
        .outboundMagneticCourse(arincAirwayLeg.outboundMagneticCourse().orElse(null))
        .theta(arincAirwayLeg.theta().orElse(null))
        .rho(arincAirwayLeg.rho().orElse(null))
        .rnp(arincAirwayLeg.rnp().orElse(null))
        .routeDistance(arincAirwayLeg.routeDistance().orElse(null))
        .holdTime(arincAirwayLeg.holdTime().orElse(null))
        .isPublishedHoldingFix(false)
        .isFlyOverFix(false)
        .build();
  }

  static BoogieAirway newAirwayFrom(ArincAirwayLeg arincAirwayLeg, List<BoogieLeg> legs) {
    return new BoogieAirway.Builder()
        .airwayIdentifier(arincAirwayLeg.routeIdentifier())
        .airwayRegion(arincAirwayLeg.customerAreaCode().name())
        .legs(legs)
        .build();
  }

  static BoogieLeg newProcedureLegFrom(ArincProcedureLeg arincProcedureLeg, @Nullable Fix associatedFix, @Nullable Fix recommendedNavaid, @Nullable Fix centerFix) {
    return new BoogieLeg.Builder()
        .associatedFix(associatedFix)
        .recommendedNavaid(recommendedNavaid)
        .centerFix(centerFix)
        .pathTerminator(arincProcedureLeg.pathTerm())
        .sequenceNumber(arincProcedureLeg.sequenceNumber())
        .speedConstraint(arincProcedureLeg.speedLimitDescription()
            .map(s -> SpeedLimitToRange.INSTANCE.apply(s, arincProcedureLeg.speedLimit().map(Integer::doubleValue).orElse(null))).orElse(Range.all()))
        .altitudeConstraint(arincProcedureLeg.altitudeDescription()
            .map(s -> AltitudeLimitToRange.INSTANCE.apply(s, arincProcedureLeg.minAltitude1().orElse(null), arincProcedureLeg.minAltitude2().orElse(null))).orElse(Range.all()))
        .outboundMagneticCourse(arincProcedureLeg.outboundMagneticCourse().orElse(null))
        .theta(arincProcedureLeg.theta().orElse(null))
        .rho(arincProcedureLeg.rho().orElse(null))
        .rnp(arincProcedureLeg.rnp().orElse(null))
        .verticalAngle(arincProcedureLeg.verticalAngle().orElse(null))
        .routeDistance(arincProcedureLeg.routeDistance().orElse(null))
        .holdTime(arincProcedureLeg.holdTime().orElse(null))
        .turnDirection(arincProcedureLeg.turnDirection().map(ArincToBoogieConverterFactory::toTurnDirection).orElse(TurnDirection.either()))
        .isPublishedHoldingFix(IsPublishedHoldingFix.INSTANCE.test(arincProcedureLeg))
        .isFlyOverFix(IsFlyOverFix.INSTANCE.test(arincProcedureLeg))
        .build();
  }

  /**
   * Generates a new {@link BoogieTransition} from:
   * <br>
   * 1. The given representative 424 leg
   * 2. The inferred type of transition {@link TransitionType}
   * 3. The collection of legs associated with that transition.
   * <br>
   * This method performs some additional standardization on the transition names mapping:
   * <br>
   * 1. {@link TransitionType#MISSED} -> "MISSED"
   * 2. Using the {@link StandardizedTransitionName} for the rest
   */
  static BoogieTransition newTransitionFrom(
      ArincProcedureLeg representative,
      TransitionType transitionType,
      List<BoogieLeg> legs
  ) {
    return new BoogieTransition.Builder()
        .procedureIdentifier(representative.sidStarIdentifier())
        .airportIdentifier(representative.airportIdentifier())
        .airportRegion(representative.airportIcaoRegion())
        .transitionIdentifier(TransitionType.MISSED.equals(transitionType)
            ? "MISSED"
            : StandardizedTransitionName.INSTANCE.apply(representative.transitionIdentifier().orElse(null)))
        .procedureType(toProcedureType(representative.subSectionCode().orElseThrow(IllegalStateException::new)))
        .transitionType(transitionType)
        .legs(legs)
        .build();
  }

  static BoogieProcedure newProcedureFrom(
      ArincProcedureLeg representative,
      RequiredNavigationEquipage requiredNavigationEquipage,
      List<BoogieTransition> transitions
  ) {
    Preconditions.checkArgument(representative.subSectionCode().isPresent());
    return new BoogieProcedure.Builder()
        .procedureIdentifier(representative.sidStarIdentifier())
        .airportIdentifier(representative.airportIdentifier())
        .airportRegion(representative.airportIcaoRegion())
        .procedureType(toProcedureType(representative.subSectionCode().get()))
        .requiredNavigationEquipage(requiredNavigationEquipage)
        .transitions(transitions)
        .build();
  }

  /**
   * The subSection within the database determines whether the procedure is a SID/STAR/APPROACH record.
   */
  static ProcedureType toProcedureType(String subSectionCode) {
    if ("F".equals(subSectionCode)) {
      return ProcedureType.APPROACH;
    } else if ("E".equals(subSectionCode)) {
      return ProcedureType.STAR;
    } else if ("D".equals(subSectionCode)) {
      return ProcedureType.SID;
    } else {
      throw new IllegalArgumentException("Unable to classify subSectionCode as a ProcedureType: ".concat(subSectionCode));
    }
  }

  static TurnDirection toTurnDirection(org.mitre.tdp.boogie.arinc.v18.field.TurnDirection turnDirection) {
    switch (turnDirection) {
      case L:
        return TurnDirection.left();
      case R:
        return TurnDirection.right();
      default:
        return TurnDirection.either();
    }
  }

  static BoogieLeg newProcedureLegFrom(ArincAirwayLeg arincAirwayLeg, Fix associatedFix, @Nullable Fix recommendedNavaid) {
    return new BoogieLeg.Builder()
        .associatedFix(associatedFix)
        .recommendedNavaid(recommendedNavaid)
        .pathTerminator(PathTerminator.TF)
        .sequenceNumber(arincAirwayLeg.sequenceNumber())
        .speedConstraint(Range.all())
        // we're making some choices here... for bi-directional airways each direction of travel can have a different minimums in min1/min2
        .altitudeConstraint(
            arincAirwayLeg.maxAltitude().isPresent()
                // use range if max is present [min1, max]
                ? AltitudeLimitToRange.INSTANCE.apply("B", arincAirwayLeg.maxAltitude().orElse(null), arincAirwayLeg.minAltitude1().orElse(null))
                // use at or above if only min1 is present
                : AltitudeLimitToRange.INSTANCE.apply("+", arincAirwayLeg.minAltitude1().orElse(null), null))
        .outboundMagneticCourse(arincAirwayLeg.outboundMagneticCourse().orElse(null))
        .theta(arincAirwayLeg.theta().orElse(null))
        .rho(arincAirwayLeg.rho().orElse(null))
        .rnp(arincAirwayLeg.rnp().orElse(null))
        .routeDistance(arincAirwayLeg.routeDistance().orElse(null))
        .holdTime(arincAirwayLeg.holdTime().orElse(null))
        .isPublishedHoldingFix(arincAirwayLeg.waypointDescription().map(IsPublishedHoldingFix.INSTANCE::isPublishedHoldingFix).orElse(false))
        .isFlyOverFix(arincAirwayLeg.waypointDescription().map(IsFlyOverFix.INSTANCE::isFlyOverFix).orElse(false))
        .build();
  }
}

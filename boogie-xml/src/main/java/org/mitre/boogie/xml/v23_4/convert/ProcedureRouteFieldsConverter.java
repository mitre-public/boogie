package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;

import org.mitre.boogie.xml.v23_4.generated.ApproachRoute;
import org.mitre.boogie.xml.v23_4.generated.ApproachTransition;
import org.mitre.boogie.xml.v23_4.generated.FinalApproach;
import org.mitre.boogie.xml.v23_4.generated.MissedApproach;
import org.mitre.boogie.xml.v23_4.generated.ProcedureRoute;
import org.mitre.boogie.xml.v23_4.generated.RouteQualifications;
import org.mitre.boogie.xml.v23_4.generated.SidCommonRoute;
import org.mitre.boogie.xml.v23_4.generated.SidEnrouteTransition;
import org.mitre.boogie.xml.v23_4.generated.SidRunwayTransition;
import org.mitre.boogie.xml.v23_4.generated.StarCommonRoute;
import org.mitre.boogie.xml.v23_4.generated.StarEnrouteTransition;
import org.mitre.boogie.xml.v23_4.generated.StarRunwayTransition;

/**
 * Extracts subtype-specific fields from a concrete {@link ProcedureRoute} into a {@link ProcedureRouteFields} record.
 *
 * <p>The generated JAXB class hierarchy has multiple levels of intermediate abstract types which share
 * fields (e.g. {@link org.mitre.boogie.xml.v23_4.generated.RunwayTransition} and {@link org.mitre.boogie.xml.v23_4.generated.CommonRoute}
 * both carry routeQualifications/rnavPbnNavSpec/rnpPbnNavSpec). This class dispatches on the concrete
 * leaf type and delegates shared field extraction to helper methods so the caller doesn't need nested
 * instanceof chains.
 */
final class ProcedureRouteFieldsConverter {

  private ProcedureRouteFieldsConverter() {
  }

  static ProcedureRouteFields convert(ProcedureRoute route) {
    if (route instanceof SidRunwayTransition srt) {
      return applySidStarRouteFields(srt.getRouteQualifications(), srt.getRnavPbnNavSpec(), srt.getRnpPbnNavSpec())
          .isFromRunway(srt.isIsFromRunway())
          .build();
    } else if (route instanceof StarRunwayTransition srt) {
      return applySidStarRouteFields(srt.getRouteQualifications(), srt.getRnavPbnNavSpec(), srt.getRnpPbnNavSpec())
          .isToRunway(srt.isIsToRunway())
          .build();
    } else if (route instanceof SidCommonRoute scr) {
      return applySidStarRouteFields(scr.getRouteQualifications(), scr.getRnavPbnNavSpec(), scr.getRnpPbnNavSpec())
          .isFromRunway(scr.isIsFromRunway())
          .build();
    } else if (route instanceof StarCommonRoute scr) {
      return applySidStarRouteFields(scr.getRouteQualifications(), scr.getRnavPbnNavSpec(), scr.getRnpPbnNavSpec())
          .isToRunway(scr.isIsToRunway())
          .build();
    } else if (route instanceof SidEnrouteTransition set) {
      return applySidStarRouteFields(set.getRouteQualifications(), set.getRnavPbnNavSpec(), set.getRnpPbnNavSpec())
          .build();
    } else if (route instanceof StarEnrouteTransition set) {
      return applySidStarRouteFields(set.getRouteQualifications(), set.getRnavPbnNavSpec(), set.getRnpPbnNavSpec())
          .build();
    } else if (route instanceof ApproachTransition at) {
      return applyApproachRouteFields(at)
          .multipleIndicator(at.getMultipleIndicator())
          .taaRef(Optional.ofNullable(at.getTaaRef()).map(Object::toString).orElse(null))
          .isTfOverlay(at.isIsTfOverlay())
          .build();
    } else if (route instanceof FinalApproach fa) {
      return applyFinalApproachFields(applyApproachRouteFields(fa), fa)
          .build();
    } else if (route instanceof MissedApproach ma) {
      return applyApproachRouteFields(ma)
          .build();
    }
    return ProcedureRouteFields.builder().build();
  }

  private static ProcedureRouteFields.Builder applySidStarRouteFields(RouteQualifications quals, Object rnavSpec, Object rnpSpec) {
    Optional<RouteQualifications> rq = Optional.ofNullable(quals);
    return ProcedureRouteFields.builder()
        .rnavPbnNavSpec(Optional.ofNullable(rnavSpec).map(Enum.class::cast).map(Enum::name).orElse(null))
        .rnpPbnNavSpec(Optional.ofNullable(rnpSpec).map(Enum.class::cast).map(Enum::name).orElse(null))
        .isDmeReq(rq.map(RouteQualifications::isIsDmeReq).orElse(null))
        .isGnssReq(rq.map(RouteQualifications::isIsGnssReq).orElse(null))
        .isRadarReq(rq.map(RouteQualifications::isIsRadarReq).orElse(null))
        .isFmsReq(rq.map(RouteQualifications::isIsFmsReq).orElse(null))
        .isConventional(rq.map(RouteQualifications::isIsConventional).orElse(null));
  }

  private static ProcedureRouteFields.Builder applyApproachRouteFields(ApproachRoute route) {
    return ProcedureRouteFields.builder()
        .qualifier1(Optional.ofNullable(route.getQualifier1()).map(Enum::name).orElse(null))
        .qualifier2(Optional.ofNullable(route.getQualifier2()).map(Enum::name).orElse(null));
  }

  private static ProcedureRouteFields.Builder applyFinalApproachFields(ProcedureRouteFields.Builder builder, FinalApproach fa) {
    return builder
        .fasBlockProvided(Optional.ofNullable(fa.getFasBlockProvided()).map(Enum::name).orElse(null))
        .fasBlockProvidedLevelOfServiceName(Optional.ofNullable(fa.getFasBlockProvidedLevelOfServiceName()).map(Enum::name).orElse(null))
        .isRemoteAltimeterRestricted(fa.isIsRemoteAltimeterRestricted())
        .lnavAuthorizedForSbas(Optional.ofNullable(fa.getLnavAuthorizedForSbas()).map(Enum::name).orElse(null))
        .lnavLevelOfServiceName(Optional.ofNullable(fa.getLnavLevelOfServiceName()).map(Enum::name).orElse(null))
        .lnavVnavAuthorizedForSbas(Optional.ofNullable(fa.getLnavVnavAuthorizedForSbas()).map(Enum::name).orElse(null))
        .lnavVnavLevelOfServiceName(Optional.ofNullable(fa.getLnavVnavLevelOfServiceName()).map(Enum::name).orElse(null))
        .procedureTch(fa.getProcedureTch())
        .glideSlopeInterceptAltitude(fa.getGlideSlopeInterceptAltitude())
        .baroVnavNotAuthorized(fa.isBaroVnavNotAuthorized())
        .taaRef(Optional.ofNullable(fa.getTaaRef()).map(Object::toString).orElse(null));
  }
}

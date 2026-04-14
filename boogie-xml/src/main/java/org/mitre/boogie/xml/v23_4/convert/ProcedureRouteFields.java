package org.mitre.boogie.xml.v23_4.convert;

import org.mitre.boogie.xml.model.ArincTransition;

/**
 * Carrier for subtype-specific fields extracted from a concrete {@link org.mitre.boogie.xml.v23_4.generated.ProcedureRoute}
 * implementation. Fields that don't apply to the source route type will be null.
 *
 * @see ProcedureRouteFieldsConverter
 */
record ProcedureRouteFields(
    // RunwayTransition / CommonRoute / EnrouteTransition (SID/STAR routes)
    String rnavPbnNavSpec,
    String rnpPbnNavSpec,
    // RouteQualifications (flattened)
    Boolean isDmeReq,
    Boolean isGnssReq,
    Boolean isRadarReq,
    Boolean isFmsReq,
    Boolean isConventional,
    // SidRunwayTransition / SidCommonRoute
    Boolean isFromRunway,
    // StarRunwayTransition / StarCommonRoute
    Boolean isToRunway,
    // ApproachRoute
    String qualifier1,
    String qualifier2,
    // ApproachTransition
    String multipleIndicator,
    Boolean isTfOverlay,
    // ApproachTransition / FinalApproach (shared)
    String taaRef,
    // FinalApproach
    String fasBlockProvided,
    String fasBlockProvidedLevelOfServiceName,
    Boolean isRemoteAltimeterRestricted,
    String lnavAuthorizedForSbas,
    String lnavLevelOfServiceName,
    String lnavVnavAuthorizedForSbas,
    String lnavVnavLevelOfServiceName,
    Long procedureTch,
    Integer glideSlopeInterceptAltitude,
    Boolean baroVnavNotAuthorized
) {

  void applyTo(ArincTransition.Builder builder) {
    builder
        .rnavPbnNavSpec(rnavPbnNavSpec)
        .rnpPbnNavSpec(rnpPbnNavSpec)
        .isDmeReq(isDmeReq)
        .isGnssReq(isGnssReq)
        .isRadarReq(isRadarReq)
        .isFmsReq(isFmsReq)
        .isConventional(isConventional)
        .isFromRunway(isFromRunway)
        .isToRunway(isToRunway)
        .qualifier1(qualifier1)
        .qualifier2(qualifier2)
        .multipleIndicator(multipleIndicator)
        .isTfOverlay(isTfOverlay)
        .taaRef(taaRef)
        .fasBlockProvided(fasBlockProvided)
        .fasBlockProvidedLevelOfServiceName(fasBlockProvidedLevelOfServiceName)
        .isRemoteAltimeterRestricted(isRemoteAltimeterRestricted)
        .lnavAuthorizedForSbas(lnavAuthorizedForSbas)
        .lnavLevelOfServiceName(lnavLevelOfServiceName)
        .lnavVnavAuthorizedForSbas(lnavVnavAuthorizedForSbas)
        .lnavVnavLevelOfServiceName(lnavVnavLevelOfServiceName)
        .procedureTch(procedureTch)
        .glideSlopeInterceptAltitude(glideSlopeInterceptAltitude)
        .baroVnavNotAuthorized(baroVnavNotAuthorized);
  }

  static Builder builder() {
    return new Builder();
  }

  static final class Builder {
    private String rnavPbnNavSpec;
    private String rnpPbnNavSpec;
    private Boolean isDmeReq;
    private Boolean isGnssReq;
    private Boolean isRadarReq;
    private Boolean isFmsReq;
    private Boolean isConventional;
    private Boolean isFromRunway;
    private Boolean isToRunway;
    private String qualifier1;
    private String qualifier2;
    private String multipleIndicator;
    private Boolean isTfOverlay;
    private String taaRef;
    private String fasBlockProvided;
    private String fasBlockProvidedLevelOfServiceName;
    private Boolean isRemoteAltimeterRestricted;
    private String lnavAuthorizedForSbas;
    private String lnavLevelOfServiceName;
    private String lnavVnavAuthorizedForSbas;
    private String lnavVnavLevelOfServiceName;
    private Long procedureTch;
    private Integer glideSlopeInterceptAltitude;
    private Boolean baroVnavNotAuthorized;

    private Builder() {
    }

    Builder rnavPbnNavSpec(String rnavPbnNavSpec) {
      this.rnavPbnNavSpec = rnavPbnNavSpec;
      return this;
    }

    Builder rnpPbnNavSpec(String rnpPbnNavSpec) {
      this.rnpPbnNavSpec = rnpPbnNavSpec;
      return this;
    }

    Builder isDmeReq(Boolean isDmeReq) {
      this.isDmeReq = isDmeReq;
      return this;
    }

    Builder isGnssReq(Boolean isGnssReq) {
      this.isGnssReq = isGnssReq;
      return this;
    }

    Builder isRadarReq(Boolean isRadarReq) {
      this.isRadarReq = isRadarReq;
      return this;
    }

    Builder isFmsReq(Boolean isFmsReq) {
      this.isFmsReq = isFmsReq;
      return this;
    }

    Builder isConventional(Boolean isConventional) {
      this.isConventional = isConventional;
      return this;
    }

    Builder isFromRunway(Boolean isFromRunway) {
      this.isFromRunway = isFromRunway;
      return this;
    }

    Builder isToRunway(Boolean isToRunway) {
      this.isToRunway = isToRunway;
      return this;
    }

    Builder qualifier1(String qualifier1) {
      this.qualifier1 = qualifier1;
      return this;
    }

    Builder qualifier2(String qualifier2) {
      this.qualifier2 = qualifier2;
      return this;
    }

    Builder multipleIndicator(String multipleIndicator) {
      this.multipleIndicator = multipleIndicator;
      return this;
    }

    Builder isTfOverlay(Boolean isTfOverlay) {
      this.isTfOverlay = isTfOverlay;
      return this;
    }

    Builder taaRef(String taaRef) {
      this.taaRef = taaRef;
      return this;
    }

    Builder fasBlockProvided(String fasBlockProvided) {
      this.fasBlockProvided = fasBlockProvided;
      return this;
    }

    Builder fasBlockProvidedLevelOfServiceName(String fasBlockProvidedLevelOfServiceName) {
      this.fasBlockProvidedLevelOfServiceName = fasBlockProvidedLevelOfServiceName;
      return this;
    }

    Builder isRemoteAltimeterRestricted(Boolean isRemoteAltimeterRestricted) {
      this.isRemoteAltimeterRestricted = isRemoteAltimeterRestricted;
      return this;
    }

    Builder lnavAuthorizedForSbas(String lnavAuthorizedForSbas) {
      this.lnavAuthorizedForSbas = lnavAuthorizedForSbas;
      return this;
    }

    Builder lnavLevelOfServiceName(String lnavLevelOfServiceName) {
      this.lnavLevelOfServiceName = lnavLevelOfServiceName;
      return this;
    }

    Builder lnavVnavAuthorizedForSbas(String lnavVnavAuthorizedForSbas) {
      this.lnavVnavAuthorizedForSbas = lnavVnavAuthorizedForSbas;
      return this;
    }

    Builder lnavVnavLevelOfServiceName(String lnavVnavLevelOfServiceName) {
      this.lnavVnavLevelOfServiceName = lnavVnavLevelOfServiceName;
      return this;
    }

    Builder procedureTch(Long procedureTch) {
      this.procedureTch = procedureTch;
      return this;
    }

    Builder glideSlopeInterceptAltitude(Integer glideSlopeInterceptAltitude) {
      this.glideSlopeInterceptAltitude = glideSlopeInterceptAltitude;
      return this;
    }

    Builder baroVnavNotAuthorized(Boolean baroVnavNotAuthorized) {
      this.baroVnavNotAuthorized = baroVnavNotAuthorized;
      return this;
    }

    ProcedureRouteFields build() {
      return new ProcedureRouteFields(
          rnavPbnNavSpec,
          rnpPbnNavSpec,
          isDmeReq,
          isGnssReq,
          isRadarReq,
          isFmsReq,
          isConventional,
          isFromRunway,
          isToRunway,
          qualifier1,
          qualifier2,
          multipleIndicator,
          isTfOverlay,
          taaRef,
          fasBlockProvided,
          fasBlockProvidedLevelOfServiceName,
          isRemoteAltimeterRestricted,
          lnavAuthorizedForSbas,
          lnavLevelOfServiceName,
          lnavVnavAuthorizedForSbas,
          lnavVnavLevelOfServiceName,
          procedureTch,
          glideSlopeInterceptAltitude,
          baroVnavNotAuthorized
      );
    }
  }
}

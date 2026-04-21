package org.mitre.boogie.xml.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;

/**
 * Data class for representing structured/parsed content from within a procedure route/transition record in the
 * ARINC 424 XML schema.
 *
 * <p>This class flattens the XML class hierarchy ({@code ProcedureRoute → RunwayTransition/CommonRoute/EnrouteTransition
 * /ApproachRoute → ApproachTransition/FinalApproach/MissedApproach} and the SID/STAR specific subtypes) into a single
 * model type. Fields from subclass-specific types will be null when the source transition is not of that type.
 */
public final class ArincTransition {

  // A424ObjectWithId / A424Base
  private final ArincBaseInfo baseInfo;
  private final String identifier;

  // Discriminator for the concrete XML transition type
  private final String transitionType;

  // Contained legs
  private final List<ArincProcedureLeg> legs;

  // ProcedureRoute fields
  private final Integer transitionAltitudeOrLevel;
  private final Boolean transitionAltitudeOrLevelIsFlightLevel;
  private final String msaRef;
  private final Boolean isAtcAssignedOnly;

  // ProcedureDesignAircraftCategories (flattened)
  private final Boolean isCategoryA;
  private final Boolean isCategoryB;
  private final Boolean isCategoryC;
  private final Boolean isCategoryD;
  private final Boolean isCategoryE;
  private final Boolean isCategoryHelicopter;

  // ProcedureDesignAircraftTypes (flattened)
  private final Boolean isTypeJet;
  private final Boolean isTypeTurbojet;
  private final Boolean isTypeTurboprop;
  private final Boolean isTypeProp;
  private final Boolean isTypePiston;
  private final Boolean isTypeNonJets;
  private final Boolean isTypeNotLimited;
  private final Boolean isTypeNonTurbojets;

  // RunwayTransition / CommonRoute / EnrouteTransition (SID/STAR routes)
  private final String rnavPbnNavSpec;
  private final String rnpPbnNavSpec;

  // RouteQualifications (flattened)
  private final Boolean isDmeReq;
  private final Boolean isGnssReq;
  private final Boolean isRadarReq;
  private final Boolean isFmsReq;
  private final Boolean isConventional;

  // SidRunwayTransition / SidCommonRoute
  private final Boolean isFromRunway;

  // StarRunwayTransition / StarCommonRoute
  private final Boolean isToRunway;

  // Procedure Routes
  private final String qualifier1;
  private final String qualifier2;

  // ApproachTransition
  private final String multipleIndicator;
  private final Boolean isTfOverlay;

  // ApproachTransition / FinalApproach (shared)
  private final String taaRef;

  // FinalApproach
  private final String fasBlockProvided;
  private final String fasBlockProvidedLevelOfServiceName;
  private final Boolean isRemoteAltimeterRestricted;
  private final String lnavAuthorizedForSbas;
  private final String lnavLevelOfServiceName;
  private final String lnavVnavAuthorizedForSbas;
  private final String lnavVnavLevelOfServiceName;
  private final Long procedureTch;
  private final Integer glideSlopeInterceptAltitude;
  private final Boolean baroVnavNotAuthorized;

  private ArincTransition(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.identifier = builder.identifier;
    this.transitionType = builder.transitionType;
    this.legs = builder.legs;
    this.transitionAltitudeOrLevel = builder.transitionAltitudeOrLevel;
    this.transitionAltitudeOrLevelIsFlightLevel = builder.transitionAltitudeOrLevelIsFlightLevel;
    this.msaRef = builder.msaRef;
    this.isAtcAssignedOnly = builder.isAtcAssignedOnly;
    this.isCategoryA = builder.isCategoryA;
    this.isCategoryB = builder.isCategoryB;
    this.isCategoryC = builder.isCategoryC;
    this.isCategoryD = builder.isCategoryD;
    this.isCategoryE = builder.isCategoryE;
    this.isCategoryHelicopter = builder.isCategoryHelicopter;
    this.isTypeJet = builder.isTypeJet;
    this.isTypeTurbojet = builder.isTypeTurbojet;
    this.isTypeTurboprop = builder.isTypeTurboprop;
    this.isTypeProp = builder.isTypeProp;
    this.isTypePiston = builder.isTypePiston;
    this.isTypeNonJets = builder.isTypeNonJets;
    this.isTypeNotLimited = builder.isTypeNotLimited;
    this.isTypeNonTurbojets = builder.isTypeNonTurbojets;
    this.rnavPbnNavSpec = builder.rnavPbnNavSpec;
    this.rnpPbnNavSpec = builder.rnpPbnNavSpec;
    this.isDmeReq = builder.isDmeReq;
    this.isGnssReq = builder.isGnssReq;
    this.isRadarReq = builder.isRadarReq;
    this.isFmsReq = builder.isFmsReq;
    this.isConventional = builder.isConventional;
    this.isFromRunway = builder.isFromRunway;
    this.isToRunway = builder.isToRunway;
    this.qualifier1 = builder.qualifier1;
    this.qualifier2 = builder.qualifier2;
    this.multipleIndicator = builder.multipleIndicator;
    this.isTfOverlay = builder.isTfOverlay;
    this.taaRef = builder.taaRef;
    this.fasBlockProvided = builder.fasBlockProvided;
    this.fasBlockProvidedLevelOfServiceName = builder.fasBlockProvidedLevelOfServiceName;
    this.isRemoteAltimeterRestricted = builder.isRemoteAltimeterRestricted;
    this.lnavAuthorizedForSbas = builder.lnavAuthorizedForSbas;
    this.lnavLevelOfServiceName = builder.lnavLevelOfServiceName;
    this.lnavVnavAuthorizedForSbas = builder.lnavVnavAuthorizedForSbas;
    this.lnavVnavLevelOfServiceName = builder.lnavVnavLevelOfServiceName;
    this.procedureTch = builder.procedureTch;
    this.glideSlopeInterceptAltitude = builder.glideSlopeInterceptAltitude;
    this.baroVnavNotAuthorized = builder.baroVnavNotAuthorized;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ArincBaseInfo baseInfo() {
    return baseInfo;
  }

  public Optional<String> identifier() {
    return Optional.ofNullable(identifier);
  }

  public String transitionType() {
    return transitionType;
  }

  public List<ArincProcedureLeg> legs() {
    return Optional.ofNullable(legs).orElseGet(Collections::emptyList);
  }

  public Optional<Integer> transitionAltitudeOrLevel() {
    return Optional.ofNullable(transitionAltitudeOrLevel);
  }

  public Optional<Boolean> transitionAltitudeOrLevelIsFlightLevel() {
    return Optional.ofNullable(transitionAltitudeOrLevelIsFlightLevel);
  }

  public Optional<String> msaRef() {
    return Optional.ofNullable(msaRef);
  }

  public Optional<Boolean> isAtcAssignedOnly() {
    return Optional.ofNullable(isAtcAssignedOnly);
  }

  public Optional<Boolean> isCategoryA() {
    return Optional.ofNullable(isCategoryA);
  }

  public Optional<Boolean> isCategoryB() {
    return Optional.ofNullable(isCategoryB);
  }

  public Optional<Boolean> isCategoryC() {
    return Optional.ofNullable(isCategoryC);
  }

  public Optional<Boolean> isCategoryD() {
    return Optional.ofNullable(isCategoryD);
  }

  public Optional<Boolean> isCategoryE() {
    return Optional.ofNullable(isCategoryE);
  }

  public Optional<Boolean> isCategoryHelicopter() {
    return Optional.ofNullable(isCategoryHelicopter);
  }

  public Optional<Boolean> isTypeJet() {
    return Optional.ofNullable(isTypeJet);
  }

  public Optional<Boolean> isTypeTurbojet() {
    return Optional.ofNullable(isTypeTurbojet);
  }

  public Optional<Boolean> isTypeTurboprop() {
    return Optional.ofNullable(isTypeTurboprop);
  }

  public Optional<Boolean> isTypeProp() {
    return Optional.ofNullable(isTypeProp);
  }

  public Optional<Boolean> isTypePiston() {
    return Optional.ofNullable(isTypePiston);
  }

  public Optional<Boolean> isTypeNonJets() {
    return Optional.ofNullable(isTypeNonJets);
  }

  public Optional<Boolean> isTypeNotLimited() {
    return Optional.ofNullable(isTypeNotLimited);
  }

  public Optional<Boolean> isTypeNonTurbojets() {
    return Optional.ofNullable(isTypeNonTurbojets);
  }

  public Optional<String> rnavPbnNavSpec() {
    return Optional.ofNullable(rnavPbnNavSpec);
  }

  public Optional<String> rnpPbnNavSpec() {
    return Optional.ofNullable(rnpPbnNavSpec);
  }

  public Optional<Boolean> isDmeReq() {
    return Optional.ofNullable(isDmeReq);
  }

  public Optional<Boolean> isGnssReq() {
    return Optional.ofNullable(isGnssReq);
  }

  public Optional<Boolean> isRadarReq() {
    return Optional.ofNullable(isRadarReq);
  }

  public Optional<Boolean> isFmsReq() {
    return Optional.ofNullable(isFmsReq);
  }

  public Optional<Boolean> isConventional() {
    return Optional.ofNullable(isConventional);
  }

  public Optional<Boolean> isFromRunway() {
    return Optional.ofNullable(isFromRunway);
  }

  public Optional<Boolean> isToRunway() {
    return Optional.ofNullable(isToRunway);
  }

  public Optional<String> qualifier1() {
    return Optional.ofNullable(qualifier1);
  }

  public Optional<String> qualifier2() {
    return Optional.ofNullable(qualifier2);
  }

  public Optional<String> multipleIndicator() {
    return Optional.ofNullable(multipleIndicator);
  }

  public Optional<Boolean> isTfOverlay() {
    return Optional.ofNullable(isTfOverlay);
  }

  public Optional<String> taaRef() {
    return Optional.ofNullable(taaRef);
  }

  public Optional<String> fasBlockProvided() {
    return Optional.ofNullable(fasBlockProvided);
  }

  public Optional<String> fasBlockProvidedLevelOfServiceName() {
    return Optional.ofNullable(fasBlockProvidedLevelOfServiceName);
  }

  public Optional<Boolean> isRemoteAltimeterRestricted() {
    return Optional.ofNullable(isRemoteAltimeterRestricted);
  }

  public Optional<String> lnavAuthorizedForSbas() {
    return Optional.ofNullable(lnavAuthorizedForSbas);
  }

  public Optional<String> lnavLevelOfServiceName() {
    return Optional.ofNullable(lnavLevelOfServiceName);
  }

  public Optional<String> lnavVnavAuthorizedForSbas() {
    return Optional.ofNullable(lnavVnavAuthorizedForSbas);
  }

  public Optional<String> lnavVnavLevelOfServiceName() {
    return Optional.ofNullable(lnavVnavLevelOfServiceName);
  }

  public Optional<Long> procedureTch() {
    return Optional.ofNullable(procedureTch);
  }

  public Optional<Integer> glideSlopeInterceptAltitude() {
    return Optional.ofNullable(glideSlopeInterceptAltitude);
  }

  public Optional<Boolean> baroVnavNotAuthorized() {
    return Optional.ofNullable(baroVnavNotAuthorized);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincTransition that = (ArincTransition) o;
    return Objects.equals(baseInfo, that.baseInfo)
        && Objects.equals(identifier, that.identifier)
        && Objects.equals(transitionType, that.transitionType)
        && Objects.equals(legs, that.legs)
        && Objects.equals(transitionAltitudeOrLevel, that.transitionAltitudeOrLevel)
        && Objects.equals(transitionAltitudeOrLevelIsFlightLevel, that.transitionAltitudeOrLevelIsFlightLevel)
        && Objects.equals(msaRef, that.msaRef)
        && Objects.equals(isAtcAssignedOnly, that.isAtcAssignedOnly)
        && Objects.equals(isCategoryA, that.isCategoryA)
        && Objects.equals(isCategoryB, that.isCategoryB)
        && Objects.equals(isCategoryC, that.isCategoryC)
        && Objects.equals(isCategoryD, that.isCategoryD)
        && Objects.equals(isCategoryE, that.isCategoryE)
        && Objects.equals(isCategoryHelicopter, that.isCategoryHelicopter)
        && Objects.equals(isTypeJet, that.isTypeJet)
        && Objects.equals(isTypeTurbojet, that.isTypeTurbojet)
        && Objects.equals(isTypeTurboprop, that.isTypeTurboprop)
        && Objects.equals(isTypeProp, that.isTypeProp)
        && Objects.equals(isTypePiston, that.isTypePiston)
        && Objects.equals(isTypeNonJets, that.isTypeNonJets)
        && Objects.equals(isTypeNotLimited, that.isTypeNotLimited)
        && Objects.equals(isTypeNonTurbojets, that.isTypeNonTurbojets)
        && Objects.equals(rnavPbnNavSpec, that.rnavPbnNavSpec)
        && Objects.equals(rnpPbnNavSpec, that.rnpPbnNavSpec)
        && Objects.equals(isDmeReq, that.isDmeReq)
        && Objects.equals(isGnssReq, that.isGnssReq)
        && Objects.equals(isRadarReq, that.isRadarReq)
        && Objects.equals(isFmsReq, that.isFmsReq)
        && Objects.equals(isConventional, that.isConventional)
        && Objects.equals(isFromRunway, that.isFromRunway)
        && Objects.equals(isToRunway, that.isToRunway)
        && Objects.equals(qualifier1, that.qualifier1)
        && Objects.equals(qualifier2, that.qualifier2)
        && Objects.equals(multipleIndicator, that.multipleIndicator)
        && Objects.equals(isTfOverlay, that.isTfOverlay)
        && Objects.equals(taaRef, that.taaRef)
        && Objects.equals(fasBlockProvided, that.fasBlockProvided)
        && Objects.equals(fasBlockProvidedLevelOfServiceName, that.fasBlockProvidedLevelOfServiceName)
        && Objects.equals(isRemoteAltimeterRestricted, that.isRemoteAltimeterRestricted)
        && Objects.equals(lnavAuthorizedForSbas, that.lnavAuthorizedForSbas)
        && Objects.equals(lnavLevelOfServiceName, that.lnavLevelOfServiceName)
        && Objects.equals(lnavVnavAuthorizedForSbas, that.lnavVnavAuthorizedForSbas)
        && Objects.equals(lnavVnavLevelOfServiceName, that.lnavVnavLevelOfServiceName)
        && Objects.equals(procedureTch, that.procedureTch)
        && Objects.equals(glideSlopeInterceptAltitude, that.glideSlopeInterceptAltitude)
        && Objects.equals(baroVnavNotAuthorized, that.baroVnavNotAuthorized);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, identifier, transitionType, legs, transitionAltitudeOrLevel,
        transitionAltitudeOrLevelIsFlightLevel, msaRef, isAtcAssignedOnly, isCategoryA, isCategoryB,
        isCategoryC, isCategoryD, isCategoryE, isCategoryHelicopter, isTypeJet, isTypeTurbojet,
        isTypeTurboprop, isTypeProp, isTypePiston, isTypeNonJets, isTypeNotLimited, isTypeNonTurbojets,
        rnavPbnNavSpec, rnpPbnNavSpec, isDmeReq, isGnssReq, isRadarReq, isFmsReq, isConventional,
        isFromRunway, isToRunway, qualifier1, qualifier2, multipleIndicator,
        isTfOverlay, taaRef, fasBlockProvided, fasBlockProvidedLevelOfServiceName,
        isRemoteAltimeterRestricted, lnavAuthorizedForSbas, lnavLevelOfServiceName,
        lnavVnavAuthorizedForSbas, lnavVnavLevelOfServiceName, procedureTch,
        glideSlopeInterceptAltitude, baroVnavNotAuthorized);
  }

  @Override
  public String toString() {
    return "ArincTransition{" +
        "identifier='" + identifier + '\'' +
        ", transitionType='" + transitionType + '\'' +
        ", legs=" + (legs != null ? legs.size() : 0) +
        ", transitionAltitudeOrLevel=" + transitionAltitudeOrLevel +
        ", qualifier1='" + qualifier1 + '\'' +
        ", qualifier2='" + qualifier2 + '\'' +
        '}';
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private String identifier;
    private String transitionType;
    private List<ArincProcedureLeg> legs;
    private Integer transitionAltitudeOrLevel;
    private Boolean transitionAltitudeOrLevelIsFlightLevel;
    private String msaRef;
    private Boolean isAtcAssignedOnly;
    private Boolean isCategoryA;
    private Boolean isCategoryB;
    private Boolean isCategoryC;
    private Boolean isCategoryD;
    private Boolean isCategoryE;
    private Boolean isCategoryHelicopter;
    private Boolean isTypeJet;
    private Boolean isTypeTurbojet;
    private Boolean isTypeTurboprop;
    private Boolean isTypeProp;
    private Boolean isTypePiston;
    private Boolean isTypeNonJets;
    private Boolean isTypeNotLimited;
    private Boolean isTypeNonTurbojets;
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

    public Builder baseInfo(ArincBaseInfo baseInfo) {
      this.baseInfo = baseInfo;
      return this;
    }

    public Builder identifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder transitionType(String transitionType) {
      this.transitionType = transitionType;
      return this;
    }

    public Builder legs(List<ArincProcedureLeg> legs) {
      this.legs = legs;
      return this;
    }

    public Builder transitionAltitudeOrLevel(Integer transitionAltitudeOrLevel) {
      this.transitionAltitudeOrLevel = transitionAltitudeOrLevel;
      return this;
    }

    public Builder transitionAltitudeOrLevelIsFlightLevel(Boolean transitionAltitudeOrLevelIsFlightLevel) {
      this.transitionAltitudeOrLevelIsFlightLevel = transitionAltitudeOrLevelIsFlightLevel;
      return this;
    }

    public Builder msaRef(String msaRef) {
      this.msaRef = msaRef;
      return this;
    }

    public Builder isAtcAssignedOnly(Boolean isAtcAssignedOnly) {
      this.isAtcAssignedOnly = isAtcAssignedOnly;
      return this;
    }

    public Builder isCategoryA(Boolean isCategoryA) {
      this.isCategoryA = isCategoryA;
      return this;
    }

    public Builder isCategoryB(Boolean isCategoryB) {
      this.isCategoryB = isCategoryB;
      return this;
    }

    public Builder isCategoryC(Boolean isCategoryC) {
      this.isCategoryC = isCategoryC;
      return this;
    }

    public Builder isCategoryD(Boolean isCategoryD) {
      this.isCategoryD = isCategoryD;
      return this;
    }

    public Builder isCategoryE(Boolean isCategoryE) {
      this.isCategoryE = isCategoryE;
      return this;
    }

    public Builder isCategoryHelicopter(Boolean isCategoryHelicopter) {
      this.isCategoryHelicopter = isCategoryHelicopter;
      return this;
    }

    public Builder isTypeJet(Boolean isTypeJet) {
      this.isTypeJet = isTypeJet;
      return this;
    }

    public Builder isTypeTurbojet(Boolean isTypeTurbojet) {
      this.isTypeTurbojet = isTypeTurbojet;
      return this;
    }

    public Builder isTypeTurboprop(Boolean isTypeTurboprop) {
      this.isTypeTurboprop = isTypeTurboprop;
      return this;
    }

    public Builder isTypeProp(Boolean isTypeProp) {
      this.isTypeProp = isTypeProp;
      return this;
    }

    public Builder isTypePiston(Boolean isTypePiston) {
      this.isTypePiston = isTypePiston;
      return this;
    }

    public Builder isTypeNonJets(Boolean isTypeNonJets) {
      this.isTypeNonJets = isTypeNonJets;
      return this;
    }

    public Builder isTypeNotLimited(Boolean isTypeNotLimited) {
      this.isTypeNotLimited = isTypeNotLimited;
      return this;
    }

    public Builder isTypeNonTurbojets(Boolean isTypeNonTurbojets) {
      this.isTypeNonTurbojets = isTypeNonTurbojets;
      return this;
    }

    public Builder rnavPbnNavSpec(String rnavPbnNavSpec) {
      this.rnavPbnNavSpec = rnavPbnNavSpec;
      return this;
    }

    public Builder rnpPbnNavSpec(String rnpPbnNavSpec) {
      this.rnpPbnNavSpec = rnpPbnNavSpec;
      return this;
    }

    public Builder isDmeReq(Boolean isDmeReq) {
      this.isDmeReq = isDmeReq;
      return this;
    }

    public Builder isGnssReq(Boolean isGnssReq) {
      this.isGnssReq = isGnssReq;
      return this;
    }

    public Builder isRadarReq(Boolean isRadarReq) {
      this.isRadarReq = isRadarReq;
      return this;
    }

    public Builder isFmsReq(Boolean isFmsReq) {
      this.isFmsReq = isFmsReq;
      return this;
    }

    public Builder isConventional(Boolean isConventional) {
      this.isConventional = isConventional;
      return this;
    }

    public Builder isFromRunway(Boolean isFromRunway) {
      this.isFromRunway = isFromRunway;
      return this;
    }

    public Builder isToRunway(Boolean isToRunway) {
      this.isToRunway = isToRunway;
      return this;
    }

    public Builder qualifier1(String qualifier1) {
      this.qualifier1 = qualifier1;
      return this;
    }

    public Builder qualifier2(String qualifier2) {
      this.qualifier2 = qualifier2;
      return this;
    }

    public Builder multipleIndicator(String multipleIndicator) {
      this.multipleIndicator = multipleIndicator;
      return this;
    }

    public Builder isTfOverlay(Boolean isTfOverlay) {
      this.isTfOverlay = isTfOverlay;
      return this;
    }

    public Builder taaRef(String taaRef) {
      this.taaRef = taaRef;
      return this;
    }

    public Builder fasBlockProvided(String fasBlockProvided) {
      this.fasBlockProvided = fasBlockProvided;
      return this;
    }

    public Builder fasBlockProvidedLevelOfServiceName(String fasBlockProvidedLevelOfServiceName) {
      this.fasBlockProvidedLevelOfServiceName = fasBlockProvidedLevelOfServiceName;
      return this;
    }

    public Builder isRemoteAltimeterRestricted(Boolean isRemoteAltimeterRestricted) {
      this.isRemoteAltimeterRestricted = isRemoteAltimeterRestricted;
      return this;
    }

    public Builder lnavAuthorizedForSbas(String lnavAuthorizedForSbas) {
      this.lnavAuthorizedForSbas = lnavAuthorizedForSbas;
      return this;
    }

    public Builder lnavLevelOfServiceName(String lnavLevelOfServiceName) {
      this.lnavLevelOfServiceName = lnavLevelOfServiceName;
      return this;
    }

    public Builder lnavVnavAuthorizedForSbas(String lnavVnavAuthorizedForSbas) {
      this.lnavVnavAuthorizedForSbas = lnavVnavAuthorizedForSbas;
      return this;
    }

    public Builder lnavVnavLevelOfServiceName(String lnavVnavLevelOfServiceName) {
      this.lnavVnavLevelOfServiceName = lnavVnavLevelOfServiceName;
      return this;
    }

    public Builder procedureTch(Long procedureTch) {
      this.procedureTch = procedureTch;
      return this;
    }

    public Builder glideSlopeInterceptAltitude(Integer glideSlopeInterceptAltitude) {
      this.glideSlopeInterceptAltitude = glideSlopeInterceptAltitude;
      return this;
    }

    public Builder baroVnavNotAuthorized(Boolean baroVnavNotAuthorized) {
      this.baroVnavNotAuthorized = baroVnavNotAuthorized;
      return this;
    }

    public ArincTransition build() {
      return new ArincTransition(this);
    }
  }
}

package org.mitre.boogie.xml.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;

/**
 * Data class for representing structured/parsed content from within a procedure record in the
 * ARINC 424 XML schema.
 *
 * <p>This class flattens the XML class hierarchy ({@code Procedure → SidStar → Sid/Star} and
 * {@code Procedure → Approach}) into a single model type. Fields from subclass-specific types
 * will be null when the source procedure is not of that type.
 */
public final class ArincProcedure {

  // A424ObjectWithId / A424Base
  private final ArincBaseInfo baseInfo;
  private final String identifier;

  // Discriminator for the concrete XML procedure type
  private final String procedureType;

  // Contained transitions
  private final List<ArincTransition> transitions;

  // Procedure fields
  private final String recordType;
  private final Boolean isRnav;
  private final Boolean isHelicopterOnlyProcedure;
  private final Boolean isMilitary;
  private final Boolean isSpecial;
  private final String procedureDesignMagVarEwt;
  private final BigDecimal procedureDesignMagVarValue;
  private final String longIdent;
  private final String procedureName;
  private final String referenceId;

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

  // SidStar fields
  private final Boolean isVorDmeRnav;
  private final String rnavPbnNavSpec;
  private final String rnpPbnNavSpec;

  // Sid fields
  private final Boolean isEngineOut;
  private final Boolean isVector;
  private final Boolean isPInS;

  // Sid / Approach shared
  private final Boolean isPInSProceedVisually;
  private final Boolean isPInSProceedVfr;

  // Star fields
  private final Boolean isContinuousDescent;

  // Approach fields
  private final String approachRouteType;
  private final String gnssFmsIndicator;
  private final String approachPointRef;
  private final BigDecimal categoryARadius;
  private final BigDecimal categoryBRadius;
  private final BigDecimal categoryCRadius;
  private final BigDecimal categoryDRadius;
  private final Boolean isRnavVisual;
  private final Boolean isLocalizerBackcourse;
  private final Boolean isPreferredProcedure;

  private ArincProcedure(Builder builder) {
    this.baseInfo = builder.baseInfo;
    this.identifier = builder.identifier;
    this.procedureType = builder.procedureType;
    this.transitions = builder.transitions;
    this.recordType = builder.recordType;
    this.isRnav = builder.isRnav;
    this.isHelicopterOnlyProcedure = builder.isHelicopterOnlyProcedure;
    this.isMilitary = builder.isMilitary;
    this.isSpecial = builder.isSpecial;
    this.procedureDesignMagVarEwt = builder.procedureDesignMagVarEwt;
    this.procedureDesignMagVarValue = builder.procedureDesignMagVarValue;
    this.longIdent = builder.longIdent;
    this.procedureName = builder.procedureName;
    this.referenceId = builder.referenceId;
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
    this.isVorDmeRnav = builder.isVorDmeRnav;
    this.rnavPbnNavSpec = builder.rnavPbnNavSpec;
    this.rnpPbnNavSpec = builder.rnpPbnNavSpec;
    this.isEngineOut = builder.isEngineOut;
    this.isVector = builder.isVector;
    this.isPInS = builder.isPInS;
    this.isPInSProceedVisually = builder.isPInSProceedVisually;
    this.isPInSProceedVfr = builder.isPInSProceedVfr;
    this.isContinuousDescent = builder.isContinuousDescent;
    this.approachRouteType = builder.approachRouteType;
    this.gnssFmsIndicator = builder.gnssFmsIndicator;
    this.approachPointRef = builder.approachPointRef;
    this.categoryARadius = builder.categoryARadius;
    this.categoryBRadius = builder.categoryBRadius;
    this.categoryCRadius = builder.categoryCRadius;
    this.categoryDRadius = builder.categoryDRadius;
    this.isRnavVisual = builder.isRnavVisual;
    this.isLocalizerBackcourse = builder.isLocalizerBackcourse;
    this.isPreferredProcedure = builder.isPreferredProcedure;
  }

  public static Builder builder() {
    return new Builder();
  }

  public ArincBaseInfo baseInfo() {
    return baseInfo;
  }

  public String identifier() {
    return identifier;
  }

  public String procedureType() {
    return procedureType;
  }

  public List<ArincTransition> transitions() {
    return transitions == null ? List.of() : transitions;
  }

  public Optional<String> recordType() {
    return Optional.ofNullable(recordType);
  }

  public Optional<Boolean> isRnav() {
    return Optional.ofNullable(isRnav);
  }

  public Optional<Boolean> isHelicopterOnlyProcedure() {
    return Optional.ofNullable(isHelicopterOnlyProcedure);
  }

  public Optional<Boolean> isMilitary() {
    return Optional.ofNullable(isMilitary);
  }

  public Optional<Boolean> isSpecial() {
    return Optional.ofNullable(isSpecial);
  }

  public Optional<String> procedureDesignMagVarEwt() {
    return Optional.ofNullable(procedureDesignMagVarEwt);
  }

  public Optional<BigDecimal> procedureDesignMagVarValue() {
    return Optional.ofNullable(procedureDesignMagVarValue);
  }

  public Optional<String> longIdent() {
    return Optional.ofNullable(longIdent);
  }

  public Optional<String> procedureName() {
    return Optional.ofNullable(procedureName);
  }

  public Optional<String> referenceId() {
    return Optional.ofNullable(referenceId);
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

  public Optional<Boolean> isVorDmeRnav() {
    return Optional.ofNullable(isVorDmeRnav);
  }

  public Optional<String> rnavPbnNavSpec() {
    return Optional.ofNullable(rnavPbnNavSpec);
  }

  public Optional<String> rnpPbnNavSpec() {
    return Optional.ofNullable(rnpPbnNavSpec);
  }

  public Optional<Boolean> isEngineOut() {
    return Optional.ofNullable(isEngineOut);
  }

  public Optional<Boolean> isVector() {
    return Optional.ofNullable(isVector);
  }

  public Optional<Boolean> isPInS() {
    return Optional.ofNullable(isPInS);
  }

  public Optional<Boolean> isPInSProceedVisually() {
    return Optional.ofNullable(isPInSProceedVisually);
  }

  public Optional<Boolean> isPInSProceedVfr() {
    return Optional.ofNullable(isPInSProceedVfr);
  }

  public Optional<Boolean> isContinuousDescent() {
    return Optional.ofNullable(isContinuousDescent);
  }

  public Optional<String> approachRouteType() {
    return Optional.ofNullable(approachRouteType);
  }

  public Optional<String> gnssFmsIndicator() {
    return Optional.ofNullable(gnssFmsIndicator);
  }

  public Optional<String> approachPointRef() {
    return Optional.ofNullable(approachPointRef);
  }

  public Optional<BigDecimal> categoryARadius() {
    return Optional.ofNullable(categoryARadius);
  }

  public Optional<BigDecimal> categoryBRadius() {
    return Optional.ofNullable(categoryBRadius);
  }

  public Optional<BigDecimal> categoryCRadius() {
    return Optional.ofNullable(categoryCRadius);
  }

  public Optional<BigDecimal> categoryDRadius() {
    return Optional.ofNullable(categoryDRadius);
  }

  public Optional<Boolean> isRnavVisual() {
    return Optional.ofNullable(isRnavVisual);
  }

  public Optional<Boolean> isLocalizerBackcourse() {
    return Optional.ofNullable(isLocalizerBackcourse);
  }

  public Optional<Boolean> isPreferredProcedure() {
    return Optional.ofNullable(isPreferredProcedure);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArincProcedure that = (ArincProcedure) o;
    return Objects.equals(baseInfo, that.baseInfo)
        && Objects.equals(identifier, that.identifier)
        && Objects.equals(procedureType, that.procedureType)
        && Objects.equals(transitions, that.transitions)
        && Objects.equals(recordType, that.recordType)
        && Objects.equals(isRnav, that.isRnav)
        && Objects.equals(isHelicopterOnlyProcedure, that.isHelicopterOnlyProcedure)
        && Objects.equals(isMilitary, that.isMilitary)
        && Objects.equals(isSpecial, that.isSpecial)
        && Objects.equals(procedureDesignMagVarEwt, that.procedureDesignMagVarEwt)
        && Objects.equals(procedureDesignMagVarValue, that.procedureDesignMagVarValue)
        && Objects.equals(longIdent, that.longIdent)
        && Objects.equals(procedureName, that.procedureName)
        && Objects.equals(referenceId, that.referenceId)
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
        && Objects.equals(isVorDmeRnav, that.isVorDmeRnav)
        && Objects.equals(rnavPbnNavSpec, that.rnavPbnNavSpec)
        && Objects.equals(rnpPbnNavSpec, that.rnpPbnNavSpec)
        && Objects.equals(isEngineOut, that.isEngineOut)
        && Objects.equals(isVector, that.isVector)
        && Objects.equals(isPInS, that.isPInS)
        && Objects.equals(isPInSProceedVisually, that.isPInSProceedVisually)
        && Objects.equals(isPInSProceedVfr, that.isPInSProceedVfr)
        && Objects.equals(isContinuousDescent, that.isContinuousDescent)
        && Objects.equals(approachRouteType, that.approachRouteType)
        && Objects.equals(gnssFmsIndicator, that.gnssFmsIndicator)
        && Objects.equals(approachPointRef, that.approachPointRef)
        && Objects.equals(categoryARadius, that.categoryARadius)
        && Objects.equals(categoryBRadius, that.categoryBRadius)
        && Objects.equals(categoryCRadius, that.categoryCRadius)
        && Objects.equals(categoryDRadius, that.categoryDRadius)
        && Objects.equals(isRnavVisual, that.isRnavVisual)
        && Objects.equals(isLocalizerBackcourse, that.isLocalizerBackcourse)
        && Objects.equals(isPreferredProcedure, that.isPreferredProcedure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseInfo, identifier, procedureType, transitions, recordType, isRnav,
        isHelicopterOnlyProcedure, isMilitary, isSpecial, procedureDesignMagVarEwt,
        procedureDesignMagVarValue, longIdent, procedureName, referenceId, isCategoryA,
        isCategoryB, isCategoryC, isCategoryD, isCategoryE, isCategoryHelicopter, isTypeJet,
        isTypeTurbojet, isTypeTurboprop, isTypeProp, isTypePiston, isTypeNonJets,
        isTypeNotLimited, isTypeNonTurbojets, isVorDmeRnav, rnavPbnNavSpec, rnpPbnNavSpec,
        isEngineOut, isVector, isPInS, isPInSProceedVisually, isPInSProceedVfr,
        isContinuousDescent, approachRouteType, gnssFmsIndicator, approachPointRef,
        categoryARadius, categoryBRadius, categoryCRadius, categoryDRadius, isRnavVisual,
        isLocalizerBackcourse, isPreferredProcedure);
  }

  @Override
  public String toString() {
    return "ArincProcedure{" +
        "baseInfo=" + baseInfo +
        ", identifier='" + identifier + '\'' +
        ", procedureType='" + procedureType + '\'' +
        ", transitions=" + transitions +
        ", recordType='" + recordType + '\'' +
        ", isRnav=" + isRnav +
        ", isHelicopterOnlyProcedure=" + isHelicopterOnlyProcedure +
        ", isMilitary=" + isMilitary +
        ", isSpecial=" + isSpecial +
        ", procedureDesignMagVarEwt='" + procedureDesignMagVarEwt + '\'' +
        ", procedureDesignMagVarValue=" + procedureDesignMagVarValue +
        ", longIdent='" + longIdent + '\'' +
        ", procedureName='" + procedureName + '\'' +
        ", referenceId='" + referenceId + '\'' +
        ", isCategoryA=" + isCategoryA +
        ", isCategoryB=" + isCategoryB +
        ", isCategoryC=" + isCategoryC +
        ", isCategoryD=" + isCategoryD +
        ", isCategoryE=" + isCategoryE +
        ", isCategoryHelicopter=" + isCategoryHelicopter +
        ", isTypeJet=" + isTypeJet +
        ", isTypeTurbojet=" + isTypeTurbojet +
        ", isTypeTurboprop=" + isTypeTurboprop +
        ", isTypeProp=" + isTypeProp +
        ", isTypePiston=" + isTypePiston +
        ", isTypeNonJets=" + isTypeNonJets +
        ", isTypeNotLimited=" + isTypeNotLimited +
        ", isTypeNonTurbojets=" + isTypeNonTurbojets +
        ", isVorDmeRnav=" + isVorDmeRnav +
        ", rnavPbnNavSpec='" + rnavPbnNavSpec + '\'' +
        ", rnpPbnNavSpec='" + rnpPbnNavSpec + '\'' +
        ", isEngineOut=" + isEngineOut +
        ", isVector=" + isVector +
        ", isPInS=" + isPInS +
        ", isPInSProceedVisually=" + isPInSProceedVisually +
        ", isPInSProceedVfr=" + isPInSProceedVfr +
        ", isContinuousDescent=" + isContinuousDescent +
        ", approachRouteType='" + approachRouteType + '\'' +
        ", gnssFmsIndicator='" + gnssFmsIndicator + '\'' +
        ", approachPointRef='" + approachPointRef + '\'' +
        ", categoryARadius=" + categoryARadius +
        ", categoryBRadius=" + categoryBRadius +
        ", categoryCRadius=" + categoryCRadius +
        ", categoryDRadius=" + categoryDRadius +
        ", isRnavVisual=" + isRnavVisual +
        ", isLocalizerBackcourse=" + isLocalizerBackcourse +
        ", isPreferredProcedure=" + isPreferredProcedure +
        '}';  
  }

  public static final class Builder {
    private ArincBaseInfo baseInfo;
    private String identifier;
    private String procedureType;
    private List<ArincTransition> transitions;
    private String recordType;
    private Boolean isRnav;
    private Boolean isHelicopterOnlyProcedure;
    private Boolean isMilitary;
    private Boolean isSpecial;
    private String procedureDesignMagVarEwt;
    private BigDecimal procedureDesignMagVarValue;
    private String longIdent;
    private String procedureName;
    private String referenceId;
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
    private Boolean isVorDmeRnav;
    private String rnavPbnNavSpec;
    private String rnpPbnNavSpec;
    private Boolean isEngineOut;
    private Boolean isVector;
    private Boolean isPInS;
    private Boolean isPInSProceedVisually;
    private Boolean isPInSProceedVfr;
    private Boolean isContinuousDescent;
    private String approachRouteType;
    private String gnssFmsIndicator;
    private String approachPointRef;
    private BigDecimal categoryARadius;
    private BigDecimal categoryBRadius;
    private BigDecimal categoryCRadius;
    private BigDecimal categoryDRadius;
    private Boolean isRnavVisual;
    private Boolean isLocalizerBackcourse;
    private Boolean isPreferredProcedure;

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

    public Builder procedureType(String procedureType) {
      this.procedureType = procedureType;
      return this;
    }

    public Builder transitions(List<ArincTransition> transitions) {
      this.transitions = transitions;
      return this;
    }

    public Builder recordType(String recordType) {
      this.recordType = recordType;
      return this;
    }

    public Builder isRnav(Boolean isRnav) {
      this.isRnav = isRnav;
      return this;
    }

    public Builder isHelicopterOnlyProcedure(Boolean isHelicopterOnlyProcedure) {
      this.isHelicopterOnlyProcedure = isHelicopterOnlyProcedure;
      return this;
    }

    public Builder isMilitary(Boolean isMilitary) {
      this.isMilitary = isMilitary;
      return this;
    }

    public Builder isSpecial(Boolean isSpecial) {
      this.isSpecial = isSpecial;
      return this;
    }

    public Builder procedureDesignMagVarEwt(String procedureDesignMagVarEwt) {
      this.procedureDesignMagVarEwt = procedureDesignMagVarEwt;
      return this;
    }

    public Builder procedureDesignMagVarValue(BigDecimal procedureDesignMagVarValue) {
      this.procedureDesignMagVarValue = procedureDesignMagVarValue;
      return this;
    }

    public Builder longIdent(String longIdent) {
      this.longIdent = longIdent;
      return this;
    }

    public Builder procedureName(String procedureName) {
      this.procedureName = procedureName;
      return this;
    }

    public Builder referenceId(String referenceId) {
      this.referenceId = referenceId;
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

    public Builder isVorDmeRnav(Boolean isVorDmeRnav) {
      this.isVorDmeRnav = isVorDmeRnav;
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

    public Builder isEngineOut(Boolean isEngineOut) {
      this.isEngineOut = isEngineOut;
      return this;
    }

    public Builder isVector(Boolean isVector) {
      this.isVector = isVector;
      return this;
    }

    public Builder isPInS(Boolean isPInS) {
      this.isPInS = isPInS;
      return this;
    }

    public Builder isPInSProceedVisually(Boolean isPInSProceedVisually) {
      this.isPInSProceedVisually = isPInSProceedVisually;
      return this;
    }

    public Builder isPInSProceedVfr(Boolean isPInSProceedVfr) {
      this.isPInSProceedVfr = isPInSProceedVfr;
      return this;
    }

    public Builder isContinuousDescent(Boolean isContinuousDescent) {
      this.isContinuousDescent = isContinuousDescent;
      return this;
    }

    public Builder approachRouteType(String approachRouteType) {
      this.approachRouteType = approachRouteType;
      return this;
    }

    public Builder gnssFmsIndicator(String gnssFmsIndicator) {
      this.gnssFmsIndicator = gnssFmsIndicator;
      return this;
    }

    public Builder approachPointRef(String approachPointRef) {
      this.approachPointRef = approachPointRef;
      return this;
    }

    public Builder categoryARadius(BigDecimal categoryARadius) {
      this.categoryARadius = categoryARadius;
      return this;
    }

    public Builder categoryBRadius(BigDecimal categoryBRadius) {
      this.categoryBRadius = categoryBRadius;
      return this;
    }

    public Builder categoryCRadius(BigDecimal categoryCRadius) {
      this.categoryCRadius = categoryCRadius;
      return this;
    }

    public Builder categoryDRadius(BigDecimal categoryDRadius) {
      this.categoryDRadius = categoryDRadius;
      return this;
    }

    public Builder isRnavVisual(Boolean isRnavVisual) {
      this.isRnavVisual = isRnavVisual;
      return this;
    }

    public Builder isLocalizerBackcourse(Boolean isLocalizerBackcourse) {
      this.isLocalizerBackcourse = isLocalizerBackcourse;
      return this;
    }

    public Builder isPreferredProcedure(Boolean isPreferredProcedure) {
      this.isPreferredProcedure = isPreferredProcedure;
      return this;
    }

    public ArincProcedure build() {
      return new ArincProcedure(this);
    }
  }
}

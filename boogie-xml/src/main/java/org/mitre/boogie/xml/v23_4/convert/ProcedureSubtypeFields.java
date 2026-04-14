package org.mitre.boogie.xml.v23_4.convert;

import java.math.BigDecimal;
import java.util.List;

import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincTransition;

/**
 * Carrier for subtype-specific fields extracted from a concrete {@link org.mitre.boogie.xml.v23_4.generated.Procedure}
 * implementation ({@link org.mitre.boogie.xml.v23_4.generated.Sid}, {@link org.mitre.boogie.xml.v23_4.generated.Star},
 * {@link org.mitre.boogie.xml.v23_4.generated.Approach}). Fields that don't apply to the source procedure type will be null.
 *
 * @see ProcedureSubtypeFieldsConverter
 */
record ProcedureSubtypeFields(
    // Converted transitions
    List<ArincTransition> transitions,
    // SidStar fields
    Boolean isVorDmeRnav,
    String rnavPbnNavSpec,
    String rnpPbnNavSpec,
    // Sid fields
    Boolean isEngineOut,
    Boolean isVector,
    Boolean isPInS,
    // Sid / Approach shared
    Boolean isPInSProceedVisually,
    Boolean isPInSProceedVfr,
    // Star fields
    Boolean isContinuousDescent,
    // Approach fields
    String approachRouteType,
    String gnssFmsIndicator,
    String approachPointRef,
    BigDecimal categoryARadius,
    BigDecimal categoryBRadius,
    BigDecimal categoryCRadius,
    BigDecimal categoryDRadius,
    Boolean isRnavVisual,
    Boolean isLocalizerBackcourse,
    Boolean isPreferredProcedure
) {

  void applyTo(ArincProcedure.Builder builder) {
    builder
        .transitions(transitions)
        .isVorDmeRnav(isVorDmeRnav)
        .rnavPbnNavSpec(rnavPbnNavSpec)
        .rnpPbnNavSpec(rnpPbnNavSpec)
        .isEngineOut(isEngineOut)
        .isVector(isVector)
        .isPInS(isPInS)
        .isPInSProceedVisually(isPInSProceedVisually)
        .isPInSProceedVfr(isPInSProceedVfr)
        .isContinuousDescent(isContinuousDescent)
        .approachRouteType(approachRouteType)
        .gnssFmsIndicator(gnssFmsIndicator)
        .approachPointRef(approachPointRef)
        .categoryARadius(categoryARadius)
        .categoryBRadius(categoryBRadius)
        .categoryCRadius(categoryCRadius)
        .categoryDRadius(categoryDRadius)
        .isRnavVisual(isRnavVisual)
        .isLocalizerBackcourse(isLocalizerBackcourse)
        .isPreferredProcedure(isPreferredProcedure);
  }

  static Builder builder() {
    return new Builder();
  }

  static final class Builder {
    private List<ArincTransition> transitions;
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

    Builder transitions(List<ArincTransition> transitions) {
      this.transitions = transitions;
      return this;
    }

    Builder isVorDmeRnav(Boolean isVorDmeRnav) {
      this.isVorDmeRnav = isVorDmeRnav;
      return this;
    }

    Builder rnavPbnNavSpec(String rnavPbnNavSpec) {
      this.rnavPbnNavSpec = rnavPbnNavSpec;
      return this;
    }

    Builder rnpPbnNavSpec(String rnpPbnNavSpec) {
      this.rnpPbnNavSpec = rnpPbnNavSpec;
      return this;
    }

    Builder isEngineOut(Boolean isEngineOut) {
      this.isEngineOut = isEngineOut;
      return this;
    }

    Builder isVector(Boolean isVector) {
      this.isVector = isVector;
      return this;
    }

    Builder isPInS(Boolean isPInS) {
      this.isPInS = isPInS;
      return this;
    }

    Builder isPInSProceedVisually(Boolean isPInSProceedVisually) {
      this.isPInSProceedVisually = isPInSProceedVisually;
      return this;
    }

    Builder isPInSProceedVfr(Boolean isPInSProceedVfr) {
      this.isPInSProceedVfr = isPInSProceedVfr;
      return this;
    }

    Builder isContinuousDescent(Boolean isContinuousDescent) {
      this.isContinuousDescent = isContinuousDescent;
      return this;
    }

    Builder approachRouteType(String approachRouteType) {
      this.approachRouteType = approachRouteType;
      return this;
    }

    Builder gnssFmsIndicator(String gnssFmsIndicator) {
      this.gnssFmsIndicator = gnssFmsIndicator;
      return this;
    }

    Builder approachPointRef(String approachPointRef) {
      this.approachPointRef = approachPointRef;
      return this;
    }

    Builder categoryARadius(BigDecimal categoryARadius) {
      this.categoryARadius = categoryARadius;
      return this;
    }

    Builder categoryBRadius(BigDecimal categoryBRadius) {
      this.categoryBRadius = categoryBRadius;
      return this;
    }

    Builder categoryCRadius(BigDecimal categoryCRadius) {
      this.categoryCRadius = categoryCRadius;
      return this;
    }

    Builder categoryDRadius(BigDecimal categoryDRadius) {
      this.categoryDRadius = categoryDRadius;
      return this;
    }

    Builder isRnavVisual(Boolean isRnavVisual) {
      this.isRnavVisual = isRnavVisual;
      return this;
    }

    Builder isLocalizerBackcourse(Boolean isLocalizerBackcourse) {
      this.isLocalizerBackcourse = isLocalizerBackcourse;
      return this;
    }

    Builder isPreferredProcedure(Boolean isPreferredProcedure) {
      this.isPreferredProcedure = isPreferredProcedure;
      return this;
    }

    ProcedureSubtypeFields build() {
      return new ProcedureSubtypeFields(
          transitions,
          isVorDmeRnav,
          rnavPbnNavSpec,
          rnpPbnNavSpec,
          isEngineOut,
          isVector,
          isPInS,
          isPInSProceedVisually,
          isPInSProceedVfr,
          isContinuousDescent,
          approachRouteType,
          gnssFmsIndicator,
          approachPointRef,
          categoryARadius,
          categoryBRadius,
          categoryCRadius,
          categoryDRadius,
          isRnavVisual,
          isLocalizerBackcourse,
          isPreferredProcedure
      );
    }
  }
}

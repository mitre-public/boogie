package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Objects;

public final class ArincWaypointType implements Serializable {
  private final boolean isAirwayIntersection;
  private final boolean isArcCenter;
  private final boolean isBackmarker;
  private final boolean isControlledAirspaceIntersection;
  private final boolean isEnroute;
  private final boolean isFacf;
  private final boolean isFaf;
  private final boolean isFirOrFraEntryPoint;
  private final boolean isFirOrFraExitPoint;
  private final boolean isFirUirFix;
  private final boolean isForApproach;
  private final boolean isForSid;
  private final boolean isForStar;
  private final boolean isForMultipleProcedures;
  private final boolean isFullDegreeLatFix;
  private final boolean isHalfDegreeLatFix;
  private final boolean isHelicopterOnlyAirwayFix;
  private final boolean isInitialApproachFix;
  private final boolean isInitialDepartureFix;
  private final boolean isInnerMarker;
  private final boolean isIntermediateApproachFix;
  private final boolean isIntersectionDmeFix;
  private final boolean isMiddleMarker;
  private final boolean isMissedApproachPoint;
  private final boolean isNdb;
  private final boolean isOceanicGateway;
  private final boolean isOffRoute;
  private final boolean isOuterMarker;
  private final boolean isRequiredOffRoute;
  private final boolean isRfLegNotProcedureFix;
  private final boolean isSourceProvidedEnroute;
  private final boolean isStepDownFix;
  private final boolean isUncharted;
  private final boolean isUnnamed;
  private final boolean isVfr;

  private ArincWaypointType(Builder builder) {
    this.isAirwayIntersection = builder.isAirwayIntersection;
    this.isArcCenter = builder.isArcCenter;
    this.isBackmarker = builder.isBackmarker;
    this.isControlledAirspaceIntersection = builder.isControlledAirspaceIntersection;
    this.isEnroute = builder.isEnroute;
    this.isFacf = builder.isFacf;
    this.isFaf = builder.isFaf;
    this.isFirOrFraEntryPoint = builder.isFirOrFraEntryPoint;
    this.isFirOrFraExitPoint = builder.isFirOrFraExitPoint;
    this.isFirUirFix = builder.isFirUirFix;
    this.isForApproach = builder.isForApproach;
    this.isForSid = builder.isForSid;
    this.isForStar = builder.isForStar;
    this.isForMultipleProcedures = builder.isForMultipleProcedures;
    this.isFullDegreeLatFix = builder.isFullDegreeLatFix;
    this.isHalfDegreeLatFix = builder.isHalfDegreeLatFix;
    this.isHelicopterOnlyAirwayFix = builder.isHelicopterOnlyAirwayFix;
    this.isInitialApproachFix = builder.isInitialApproachFix;
    this.isInitialDepartureFix = builder.isInitialDepartureFix;
    this.isInnerMarker = builder.isInnerMarker;
    this.isIntermediateApproachFix = builder.isIntermediateApproachFix;
    this.isIntersectionDmeFix = builder.isIntersectionDmeFix;
    this.isMiddleMarker = builder.isMiddleMarker;
    this.isMissedApproachPoint = builder.isMissedApproachPoint;
    this.isNdb = builder.isNdb;
    this.isOceanicGateway = builder.isOceanicGateway;
    this.isOffRoute = builder.isOffRoute;
    this.isOuterMarker = builder.isOuterMarker;
    this.isRequiredOffRoute = builder.isRequiredOffRoute;
    this.isRfLegNotProcedureFix = builder.isRfLegNotProcedureFix;
    this.isSourceProvidedEnroute = builder.isSourceProvidedEnroute;
    this.isStepDownFix = builder.isStepDownFix;
    this.isUncharted = builder.isUncharted;
    this.isUnnamed = builder.isUnnamed;
    this.isVfr = builder.isVfr;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .airwayIntersection(isAirwayIntersection)
        .arcCenter(isArcCenter)
        .backmarker(isBackmarker)
        .controlledAirspaceIntersection(isControlledAirspaceIntersection)
        .enroute(isEnroute)
        .facf(isFacf)
        .faf(isFaf)
        .firOrFraEntryPoint(isFirOrFraEntryPoint)
        .firOrFraExitPoint(isFirOrFraExitPoint)
        .firUirFix(isFirUirFix)
        .forApproach(isForApproach)
        .forSid(isForSid)
        .forStar(isForStar)
        .forMultipleProcedures(isForMultipleProcedures)
        .fullDegreeLatFix(isFullDegreeLatFix)
        .halfDegreeLatFix(isHalfDegreeLatFix)
        .helicopterOnlyAirwayFix(isHelicopterOnlyAirwayFix)
        .initialApproachFix(isInitialApproachFix)
        .initialDepartureFix(isInitialDepartureFix)
        .innerMarker(isInnerMarker)
        .intermediateApproachFix(isIntermediateApproachFix)
        .intersectionDmeFix(isIntersectionDmeFix)
        .middleMarker(isMiddleMarker)
        .missedApproachPoint(isMissedApproachPoint)
        .ndb(isNdb)
        .oceanicGateway(isOceanicGateway)
        .offRoute(isOffRoute)
        .outerMarker(isOuterMarker)
        .requiredOffRoute(isRequiredOffRoute)
        .rfLegNotProcedureFix(isRfLegNotProcedureFix)
        .sourceProvidedEnroute(isSourceProvidedEnroute)
        .stepDownFix(isStepDownFix)
        .uncharted(isUncharted)
        .unnamed(isUnnamed)
        .vfr(isVfr);
  }

  public boolean isAirwayIntersection() {
    return isAirwayIntersection;
  }

  public boolean isArcCenter() {
    return isArcCenter;
  }

  public boolean isBackmarker() {
    return isBackmarker;
  }

  public boolean isControlledAirspaceIntersection() {
    return isControlledAirspaceIntersection;
  }

  public boolean isEnroute() {
    return isEnroute;
  }

  public boolean isFacf() {
    return isFacf;
  }

  public boolean isFaf() {
    return isFaf;
  }

  public boolean isFirOrFraEntryPoint() {
    return isFirOrFraEntryPoint;
  }

  public boolean isFirOrFraExitPoint() {
    return isFirOrFraExitPoint;
  }

  public boolean isFirUirFix() {
    return isFirUirFix;
  }

  public boolean isForApproach() {
    return isForApproach;
  }

  public boolean isForSid() {
    return isForSid;
  }

  public boolean isForStar() {
    return isForStar;
  }

  public boolean isForMultipleProcedures() {
    return isForMultipleProcedures;
  }

  public boolean isFullDegreeLatFix() {
    return isFullDegreeLatFix;
  }

  public boolean isHalfDegreeLatFix() {
    return isHalfDegreeLatFix;
  }

  public boolean isHelicopterOnlyAirwayFix() {
    return isHelicopterOnlyAirwayFix;
  }

  public boolean isInitialApproachFix() {
    return isInitialApproachFix;
  }

  public boolean isInitialDepartureFix() {
    return isInitialDepartureFix;
  }

  public boolean isInnerMarker() {
    return isInnerMarker;
  }

  public boolean isIntermediateApproachFix() {
    return isIntermediateApproachFix;
  }

  public boolean isIntersectionDmeFix() {
    return isIntersectionDmeFix;
  }

  public boolean isMiddleMarker() {
    return isMiddleMarker;
  }

  public boolean isMissedApproachPoint() {
    return isMissedApproachPoint;
  }

  public boolean isNdb() {
    return isNdb;
  }

  public boolean isOceanicGateway() {
    return isOceanicGateway;
  }

  public boolean isOffRoute() {
    return isOffRoute;
  }

  public boolean isOuterMarker() {
    return isOuterMarker;
  }

  public boolean isRequiredOffRoute() {
    return isRequiredOffRoute;
  }

  public boolean isRfLegNotProcedureFix() {
    return isRfLegNotProcedureFix;
  }

  public boolean isSourceProvidedEnroute() {
    return isSourceProvidedEnroute;
  }

  public boolean isStepDownFix() {
    return isStepDownFix;
  }

  public boolean isUncharted() {
    return isUncharted;
  }

  public boolean isUnnamed() {
    return isUnnamed;
  }

  public boolean isVfr() {
    return isVfr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArincWaypointType that = (ArincWaypointType) o;
    return isAirwayIntersection == that.isAirwayIntersection && isArcCenter == that.isArcCenter && isBackmarker == that.isBackmarker && isControlledAirspaceIntersection == that.isControlledAirspaceIntersection && isEnroute == that.isEnroute && isFacf == that.isFacf && isFaf == that.isFaf && isFirOrFraEntryPoint == that.isFirOrFraEntryPoint && isFirOrFraExitPoint == that.isFirOrFraExitPoint && isFirUirFix == that.isFirUirFix && isForApproach == that.isForApproach && isForSid == that.isForSid && isForStar == that.isForStar && isForMultipleProcedures == that.isForMultipleProcedures && isFullDegreeLatFix == that.isFullDegreeLatFix && isHalfDegreeLatFix == that.isHalfDegreeLatFix && isHelicopterOnlyAirwayFix == that.isHelicopterOnlyAirwayFix && isInitialApproachFix == that.isInitialApproachFix && isInitialDepartureFix == that.isInitialDepartureFix && isInnerMarker == that.isInnerMarker && isIntermediateApproachFix == that.isIntermediateApproachFix && isIntersectionDmeFix == that.isIntersectionDmeFix && isMiddleMarker == that.isMiddleMarker && isMissedApproachPoint == that.isMissedApproachPoint && isNdb == that.isNdb && isOceanicGateway == that.isOceanicGateway && isOffRoute == that.isOffRoute && isOuterMarker == that.isOuterMarker && isRequiredOffRoute == that.isRequiredOffRoute && isRfLegNotProcedureFix == that.isRfLegNotProcedureFix && isSourceProvidedEnroute == that.isSourceProvidedEnroute && isStepDownFix == that.isStepDownFix && isUncharted == that.isUncharted && isUnnamed == that.isUnnamed && isVfr == that.isVfr;
  }

  @Override
  public int hashCode() {
    return Objects.hash(isAirwayIntersection, isArcCenter, isBackmarker, isControlledAirspaceIntersection, isEnroute, isFacf, isFaf, isFirOrFraEntryPoint, isFirOrFraExitPoint, isFirUirFix, isForApproach, isForSid, isForStar, isForMultipleProcedures, isFullDegreeLatFix, isHalfDegreeLatFix, isHelicopterOnlyAirwayFix, isInitialApproachFix, isInitialDepartureFix, isInnerMarker, isIntermediateApproachFix, isIntersectionDmeFix, isMiddleMarker, isMissedApproachPoint, isNdb, isOceanicGateway, isOffRoute, isOuterMarker, isRequiredOffRoute, isRfLegNotProcedureFix, isSourceProvidedEnroute, isStepDownFix, isUncharted, isUnnamed, isVfr);
  }

  @Override
  public String toString() {
    return "ArincWaypointType{" +
        "isAirwayIntersection=" + isAirwayIntersection +
        ", isArcCenter=" + isArcCenter +
        ", isBackmarker=" + isBackmarker +
        ", isControlledAirspaceIntersection=" + isControlledAirspaceIntersection +
        ", isEnroute=" + isEnroute +
        ", isFacf=" + isFacf +
        ", isFaf=" + isFaf +
        ", isFirOrFraEntryPoint=" + isFirOrFraEntryPoint +
        ", isFirOrFraExitPoint=" + isFirOrFraExitPoint +
        ", isFirUirFix=" + isFirUirFix +
        ", isForApproach=" + isForApproach +
        ", isForSid=" + isForSid +
        ", isForStar=" + isForStar +
        ", isForMultipleProcedures=" + isForMultipleProcedures +
        ", isFullDegreeLatFix=" + isFullDegreeLatFix +
        ", isHalfDegreeLatFix=" + isHalfDegreeLatFix +
        ", isHelicopterOnlyAirwayFix=" + isHelicopterOnlyAirwayFix +
        ", isInitialApproachFix=" + isInitialApproachFix +
        ", isInitialDepartureFix=" + isInitialDepartureFix +
        ", isInnerMarker=" + isInnerMarker +
        ", isIntermediateApproachFix=" + isIntermediateApproachFix +
        ", isIntersectionDmeFix=" + isIntersectionDmeFix +
        ", isMiddleMarker=" + isMiddleMarker +
        ", isMissedApproachPoint=" + isMissedApproachPoint +
        ", isNdb=" + isNdb +
        ", isOceanicGateway=" + isOceanicGateway +
        ", isOffRoute=" + isOffRoute +
        ", isOuterMarker=" + isOuterMarker +
        ", isRequiredOffRoute=" + isRequiredOffRoute +
        ", isRfLegNotProcedureFix=" + isRfLegNotProcedureFix +
        ", isSourceProvidedEnroute=" + isSourceProvidedEnroute +
        ", isStepDownFix=" + isStepDownFix +
        ", isUncharted=" + isUncharted +
        ", isUnnamed=" + isUnnamed +
        ", isVfr=" + isVfr +
        '}';
  }

  public static class Builder {
    private boolean isAirwayIntersection = false;
    private boolean isArcCenter = false;
    private boolean isBackmarker = false;
    private boolean isControlledAirspaceIntersection = false;
    private boolean isEnroute = false;
    private boolean isFacf = false;
    private boolean isFaf = false;
    private boolean isFirOrFraEntryPoint = false;
    private boolean isFirOrFraExitPoint = false;
    private boolean isFirUirFix = false;
    private boolean isForApproach = false;
    private boolean isForSid = false;
    private boolean isForStar = false;
    private boolean isForMultipleProcedures = false;
    private boolean isFullDegreeLatFix = false;
    private boolean isHalfDegreeLatFix = false;
    private boolean isHelicopterOnlyAirwayFix = false;
    private boolean isInitialApproachFix = false;
    private boolean isInitialDepartureFix = false;
    private boolean isInnerMarker = false;
    private boolean isIntermediateApproachFix = false;
    private boolean isIntersectionDmeFix = false;
    private boolean isMiddleMarker = false;
    private boolean isMissedApproachPoint = false;
    private boolean isNdb = false;
    private boolean isOceanicGateway = false;
    private boolean isOffRoute = false;
    private boolean isOuterMarker = false;
    private boolean isRequiredOffRoute = false;
    private boolean isRfLegNotProcedureFix = false;
    private boolean isSourceProvidedEnroute = false;
    private boolean isStepDownFix = false;
    private boolean isUncharted = false;
    private boolean isUnnamed = false;
    private boolean isVfr = false;

    private Builder() {}

    public Builder airwayIntersection(boolean airwayIntersection) {
      isAirwayIntersection = airwayIntersection;
      return this;
    }

    public Builder arcCenter(boolean arcCenter) {
      isArcCenter = arcCenter;
      return this;
    }

    public Builder backmarker(boolean backmarker) {
      isBackmarker = backmarker;
      return this;
    }

    public Builder controlledAirspaceIntersection(boolean controlledAirspaceIntersection) {
      isControlledAirspaceIntersection = controlledAirspaceIntersection;
      return this;
    }

    public Builder enroute(boolean enroute) {
      isEnroute = enroute;
      return this;
    }

    public Builder facf(boolean facf) {
      isFacf = facf;
      return this;
    }

    public Builder faf(boolean faf) {
      isFaf = faf;
      return this;
    }

    public Builder firOrFraEntryPoint(boolean firOrFraEntryPoint) {
      isFirOrFraEntryPoint = firOrFraEntryPoint;
      return this;
    }

    public Builder firOrFraExitPoint(boolean firOrFraExitPoint) {
      isFirOrFraExitPoint = firOrFraExitPoint;
      return this;
    }

    public Builder firUirFix(boolean firUirFix) {
      isFirUirFix = firUirFix;
      return this;
    }

    public Builder forApproach(boolean forApproach) {
      isForApproach = forApproach;
      return this;
    }

    public Builder forSid(boolean forSid) {
      isForSid = forSid;
      return this;
    }

    public Builder forStar(boolean forStar) {
      isForStar = forStar;
      return this;
    }

    public Builder forMultipleProcedures(boolean forMultipleProcedures) {
      isForMultipleProcedures = forMultipleProcedures;
      return this;
    }

    public Builder fullDegreeLatFix(boolean fullDegreeLatFix) {
      isFullDegreeLatFix = fullDegreeLatFix;
      return this;
    }

    public Builder halfDegreeLatFix(boolean halfDegreeLatFix) {
      isHalfDegreeLatFix = halfDegreeLatFix;
      return this;
    }

    public Builder helicopterOnlyAirwayFix(boolean helicopterOnlyAirwayFix) {
      isHelicopterOnlyAirwayFix = helicopterOnlyAirwayFix;
      return this;
    }

    public Builder initialApproachFix(boolean initialApproachFix) {
      isInitialApproachFix = initialApproachFix;
      return this;
    }

    public Builder initialDepartureFix(boolean initialDepartureFix) {
      isInitialDepartureFix = initialDepartureFix;
      return this;
    }

    public Builder innerMarker(boolean innerMarker) {
      isInnerMarker = innerMarker;
      return this;
    }

    public Builder intermediateApproachFix(boolean intermediateApproachFix) {
      isIntermediateApproachFix = intermediateApproachFix;
      return this;
    }

    public Builder intersectionDmeFix(boolean intersectionDmeFix) {
      isIntersectionDmeFix = intersectionDmeFix;
      return this;
    }

    public Builder middleMarker(boolean middleMarker) {
      isMiddleMarker = middleMarker;
      return this;
    }

    public Builder missedApproachPoint(boolean missedApproachPoint) {
      isMissedApproachPoint = missedApproachPoint;
      return this;
    }

    public Builder ndb(boolean ndb) {
      isNdb = ndb;
      return this;
    }

    public Builder oceanicGateway(boolean oceanicGateway) {
      isOceanicGateway = oceanicGateway;
      return this;
    }

    public Builder offRoute(boolean offRoute) {
      isOffRoute = offRoute;
      return this;
    }

    public Builder outerMarker(boolean outerMarker) {
      isOuterMarker = outerMarker;
      return this;
    }

    public Builder requiredOffRoute(boolean requiredOffRoute) {
      isRequiredOffRoute = requiredOffRoute;
      return this;
    }

    public Builder rfLegNotProcedureFix(boolean rfLegNotProcedureFix) {
      isRfLegNotProcedureFix = rfLegNotProcedureFix;
      return this;
    }

    public Builder sourceProvidedEnroute(boolean sourceProvidedEnroute) {
      isSourceProvidedEnroute = sourceProvidedEnroute;
      return this;
    }

    public Builder stepDownFix(boolean stepDownFix) {
      isStepDownFix = stepDownFix;
      return this;
    }

    public Builder uncharted(boolean uncharted) {
      isUncharted = uncharted;
      return this;
    }

    public Builder unnamed(boolean unnamed) {
      isUnnamed = unnamed;
      return this;
    }

    public Builder vfr(boolean vfr) {
      isVfr = vfr;
      return this;
    }

    public ArincWaypointType build() {
      return new ArincWaypointType(this);
    }
  }

}

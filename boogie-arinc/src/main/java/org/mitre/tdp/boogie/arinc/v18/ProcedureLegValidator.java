package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.ImmutableMap;

public final class ProcedureLegValidator implements Predicate<ArincRecord> {

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && arincRecord.containsParsedField("recordType")
        && arincRecord.containsParsedField("airportIdentifier")
        && arincRecord.containsParsedField("airportIcaoRegion")
        && arincRecord.containsParsedField("sidStarIdentifier")
        && arincRecord.containsParsedField("routeType")
        && arincRecord.containsParsedField("sequenceNumber")
        && arincRecord.containsParsedField("fileRecordNumber")
        && arincRecord.containsParsedField("lastUpdateCycle")
        && arincRecord.containsParsedField("pathTerm")
        && legTypeValidator(arincRecord).test(arincRecord);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter(s -> Pattern.matches("D|E|F", s)).isPresent();
  }

  Predicate<ArincRecord> legTypeValidator(ArincRecord arincRecord) {
    PathTerm pathTerm = arincRecord.requiredField("pathTerm");
    return legValidators.get(pathTerm);
  }

  /**
   * The mapping of all leg-specific validators based on the derived {@link PathTerm} of the leg. Most ARINC 424 legs have
   * different explicitly required field-level content in order for them to be flown properly by the aircraft FMS.
   * <br>
   * It doesn't take too long to toss these together on the raw record classes but it probably pays to have ones which operate
   * on the more structured form of these objects as well (e.g. {@link ArincProcedureLeg}).
   */
  private static final ImmutableMap<PathTerm, Predicate<ArincRecord>> legValidators = ImmutableMap.<PathTerm, Predicate<ArincRecord>>builder()
      .put(PathTerm.IF, ProcedureLegValidator::containsTerminalFixFields)
      .put(PathTerm.TF, ProcedureLegValidator::containsTerminalFixFields)
      .put(PathTerm.CF, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerm.DF, ProcedureLegValidator::containsTerminalFixFields)
      .put(PathTerm.FA, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.FC, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.FD, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.FM, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.CA, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.CD, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerm.CI, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.CR, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.RF, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsCenterFixFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerm.AF, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho"))
      .put(PathTerm.VA, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("altitudeDescription")
          && arincRecord.containsParsedField("minAltitude1"))
      .put(PathTerm.VD, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerm.VI, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.VM, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerm.VR, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("theta"))
      .put(PathTerm.PI, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerm.HA, arincRecord -> containsTerminalFixFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerm.HF, arincRecord -> containsTerminalFixFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerm.HM, arincRecord -> containsTerminalFixFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .build();

  static boolean containsTerminalFixFields(ArincRecord arincRecord) {
    return arincRecord.containsParsedField("fixIdentifier")
        && arincRecord.containsParsedField("fixIcaoRegion")
        && arincRecord.containsParsedField("fixSectionCode")
        && arincRecord.containsParsedField("fixSubSectionCode");
  }

  static boolean containsRecommendedNavaidFields(ArincRecord arincRecord) {
    return arincRecord.containsParsedField("recommendedNavaidIdentifier")
        && arincRecord.containsParsedField("recommendedNavaidIcaoRegion")
        && arincRecord.containsParsedField("recommendedNavaidSectionCode")
        && arincRecord.containsParsedField("recommendedNavaidSubSectionCode");
  }

  static boolean containsCenterFixFields(ArincRecord arincRecord) {
    return arincRecord.containsParsedField("fixIdentifier")
        && arincRecord.containsParsedField("fixIcaoRegion")
        && arincRecord.containsParsedField("fixSectionCode")
        && arincRecord.containsParsedField("fixSubSectionCode");
  }
}

package org.mitre.tdp.boogie.arinc.v18;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.ImmutableMap;

/**
 * The {@link ProcedureLegValidator} represents a set of expectations on an {@link ArincRecord} for it to be considered eligible
 * to be converted into an {@link ArincProcedureLeg}.
 * <br>
 * Generally speaking this class is checking that the fields outlined as required in the ARINC spec for each {@link PathTerminator}
 * type exist in the leg definition. The goal is to not perform the conversion on any legs which by definition aren't flyable
 * as per the 424 spec.
 * <br>
 * These expectations are (to a degree) mirrored in the implementation and method calls of the {@link ProcedureLegConverter}.
 */
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

  private static final Set<String> allowedSubsections = newHashSet("D", "E", "F");

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter(allowedSubsections::contains).isPresent();
  }

  Predicate<ArincRecord> legTypeValidator(ArincRecord arincRecord) {
    PathTerminator pathTerminator = arincRecord.requiredField("pathTerm");
    return legValidators.get(pathTerminator);
  }

  /**
   * The mapping of all leg-specific validators based on the derived {@link PathTerminator} of the leg. Most ARINC 424 legs have
   * different explicitly required field-level content in order for them to be flown properly by the aircraft FMS.
   * <br>
   * It doesn't take too long to toss these together on the raw record classes but it probably pays to have ones which operate
   * on the more structured form of these objects as well (e.g. {@link ArincProcedureLeg}).
   */
  private static final ImmutableMap<PathTerminator, Predicate<ArincRecord>> legValidators = ImmutableMap.<PathTerminator, Predicate<ArincRecord>>builder()
      .put(PathTerminator.IF, ProcedureLegValidator::containsTerminalFixFields)
      .put(PathTerminator.TF, ProcedureLegValidator::containsTerminalFixFields)
      .put(PathTerminator.CF, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerminator.DF, ProcedureLegValidator::containsTerminalFixFields)
      .put(PathTerminator.FA, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.FC, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.FD, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.FM, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.CA, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.CD, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerminator.CI, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.CR, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.RF, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsCenterFixFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerminator.AF, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho"))
      .put(PathTerminator.VA, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("altitudeDescription")
          && arincRecord.containsParsedField("minAltitude1"))
      .put(PathTerminator.VD, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerminator.VI, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.VM, arincRecord -> arincRecord.containsParsedField("outboundMagneticCourse"))
      .put(PathTerminator.VR, arincRecord -> containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("theta"))
      .put(PathTerminator.PI, arincRecord -> containsTerminalFixFields(arincRecord)
          && containsRecommendedNavaidFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("theta")
          && arincRecord.containsParsedField("rho")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerminator.HA, arincRecord -> containsTerminalFixFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerminator.HF, arincRecord -> containsTerminalFixFields(arincRecord)
          && arincRecord.containsParsedField("turnDirection")
          && arincRecord.containsParsedField("outboundMagneticCourse")
          && arincRecord.containsParsedField("routeHoldDistanceTime"))
      .put(PathTerminator.HM, arincRecord -> containsTerminalFixFields(arincRecord)
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

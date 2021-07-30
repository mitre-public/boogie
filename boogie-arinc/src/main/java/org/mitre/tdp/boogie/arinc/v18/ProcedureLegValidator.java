package org.mitre.tdp.boogie.arinc.v18;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.v18.ValidationHelper.containsParsedField;

import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private static final Logger LOG = LoggerFactory.getLogger(ProcedureLegValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public ProcedureLegValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public ProcedureLegValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
    this.legValidators = legValidators();
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "sidStarIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "routeType", missingFieldConsumer)
        && containsParsedField(arincRecord, "sequenceNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer)
        && containsParsedField(arincRecord, "pathTerm", missingFieldConsumer)
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
  private final ImmutableMap<PathTerminator, Predicate<ArincRecord>> legValidators;

  private ImmutableMap<PathTerminator, Predicate<ArincRecord>> legValidators() {
    return ImmutableMap.<PathTerminator, Predicate<ArincRecord>>builder()
        .put(PathTerminator.IF, this::containsAssociatedFixFields)
        .put(PathTerminator.TF, this::containsAssociatedFixFields)
        .put(PathTerminator.CF, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "rho", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .put(PathTerminator.DF, this::containsAssociatedFixFields)
        .put(PathTerminator.FA, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "rho", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.FC, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "rho", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.FD, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "rho", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.FM, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "rho", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.CA, arincRecord -> containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.CD, arincRecord -> containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .put(PathTerminator.CI, arincRecord -> containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.CR, arincRecord -> containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.RF, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsCenterFixFields(arincRecord)
            && containsParsedField(arincRecord, "turnDirection", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .put(PathTerminator.AF, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "turnDirection", missingFieldConsumer)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "rho", missingFieldConsumer))
        .put(PathTerminator.VA, arincRecord ->
            containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
                && containsParsedField(arincRecord, "altitudeDescription", missingFieldConsumer)
                && containsParsedField(arincRecord, "minAltitude1", missingFieldConsumer))
        .put(PathTerminator.VD, arincRecord -> containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .put(PathTerminator.VI, arincRecord -> containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.VM, arincRecord -> containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer))
        .put(PathTerminator.VR, arincRecord -> containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer))
        .put(PathTerminator.PI, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsRecommendedNavaidFields(arincRecord)
            && containsParsedField(arincRecord, "turnDirection", missingFieldConsumer)
            && containsParsedField(arincRecord, "theta", missingFieldConsumer)
            && containsParsedField(arincRecord, "rho", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .put(PathTerminator.HA, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsParsedField(arincRecord, "turnDirection", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .put(PathTerminator.HF, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsParsedField(arincRecord, "turnDirection", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .put(PathTerminator.HM, arincRecord -> containsAssociatedFixFields(arincRecord)
            && containsParsedField(arincRecord, "turnDirection", missingFieldConsumer)
            && containsParsedField(arincRecord, "outboundMagneticCourse", missingFieldConsumer)
            && containsParsedField(arincRecord, "routeHoldDistanceTime", missingFieldConsumer))
        .build();
  }

  boolean containsAssociatedFixFields(ArincRecord arincRecord) {
    return containsParsedField(arincRecord, "fixIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "fixIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "fixSectionCode", missingFieldConsumer)
        && (arincRecord.containsParsedField("fixSubSectionCode") || arincRecord.optionalField("fixSectionCode").filter(SectionCode.D::equals).isPresent());
  }

  boolean containsRecommendedNavaidFields(ArincRecord arincRecord) {
    return containsParsedField(arincRecord, "recommendedNavaidIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "recommendedNavaidIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "recommendedNavaidSectionCode", missingFieldConsumer)
        && (arincRecord.containsParsedField("recommendedNavaidSubSectionCode") || arincRecord.optionalField("recommendedNavaidSectionCode").filter(SectionCode.D::equals).isPresent());
  }

  boolean containsCenterFixFields(ArincRecord arincRecord) {
    return containsParsedField(arincRecord, "centerFixIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "centerFixIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "centerFixSectionCode", missingFieldConsumer)
        && (arincRecord.containsParsedField("centerFixSubSectionCode") || arincRecord.optionalField("centerFixSectionCode").filter(SectionCode.D::equals).isPresent());
  }
}

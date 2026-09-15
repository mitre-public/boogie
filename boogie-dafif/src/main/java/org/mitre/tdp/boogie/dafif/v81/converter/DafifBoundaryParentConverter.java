package org.mitre.tdp.boogie.dafif.v81.converter;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;

public final class DafifBoundaryParentConverter implements Function<DafifRecord, Optional<DafifBoundaryParent>> {

  @Override
  public Optional<DafifBoundaryParent> apply(DafifRecord record) {
    requireNonNull(record, "Cannot convert null DafifRecord.");

    return Optional.of(DafifBoundaryParent.builder()
        .boundaryIdentification(record.requiredField("boundaryIdentification"))
        .boundaryType(BoundaryTypeConverter.INSTANCE.apply(record.<Integer>requiredField("boundaryType")))
        .name(record.<String>optionalField("name").orElse(null))
        .icaoCode(record.requiredField("icaoCode"))
        .controllingAuthority(record.<String>optionalField("controllingAuthority").orElse(null))
        .localHorizontalDatum(record.<String>optionalField("localHorizontalDatum").orElse(null))
        .geodeticDatum(record.requiredField("geodeticDatum"))
        .communicationName(record.<String>optionalField("communicationName").orElse(null))
        .communicationsFrequency1(record.<String>optionalField("communicationsFrequency1").orElse(null))
        .communicationsFrequency2(record.<String>optionalField("communicationsFrequency2").orElse(null))
        .airspaceClass(record.<String>optionalField("airspaceClass").orElse(null))
        .classExceptionFlag(record.<String>optionalField("classExceptionFlag").orElse(null))
        .classExceptionRemarks(record.<String>optionalField("classExceptionRemarks").orElse(null))
        .level(record.requiredField("level"))
        .upperAltitude(record.requiredField("upperAltitude"))
        .lowerAltitude(record.requiredField("lowerAltitude"))
        .requiredNavPerformance(record.<Integer>optionalField("requiredNavPerformance").orElse(null))
        .cycleDate(record.requiredField("cycleDate"))
        .upperRvsm(record.requiredField("upperRvsm"))
        .lowerRvsm(record.requiredField("lowerRvsm"))
        .build());
  }
}

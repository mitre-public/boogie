package org.mitre.tdp.boogie.dafif.v81.converter;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;

public final class DafifBoundaryConverter implements Function<DafifRecord, Optional<DafifBoundarySegment>> {

  @Override
  public Optional<DafifBoundarySegment> apply(DafifRecord record) {
    requireNonNull(record, "Cannot convert null DafifRecord.");

    return Optional.of(DafifBoundarySegment.builder()
        .boundaryIdentification(record.requiredField("boundaryIdentification"))
        .segmentNumber(record.requiredField("segmentNumber"))
        .name(record.<String>optionalField("name").orElse(null))
        .boundaryType(BoundaryTypeConverter.INSTANCE.apply(record.<Integer>requiredField("boundaryType")))
        .icaoCode(record.requiredField("icaoCode"))
        .shape(ShapeConverter.INSTANCE.apply(record.requiredField("shape")))
        .derivation(record.<String>optionalField("derivation").map(DerivationConverter.INSTANCE).orElse(null))
        .geodeticLatitude1(record.<String>optionalField("geodeticLatitude1").orElse(null))
        .latitude1(record.<Double>optionalField("latitude1").orElse(null))
        .geodeticLongitude1(record.<String>optionalField("geodeticLongitude1").orElse(null))
        .longitude1(record.<Double>optionalField("longitude1").orElse(null))
        .geodeticLatitude2(record.<String>optionalField("geodeticLatitude2").orElse(null))
        .latitude2(record.<Double>optionalField("latitude2").orElse(null))
        .geodeticLongitude2(record.<String>optionalField("geodeticLongitude2").orElse(null))
        .longitude2(record.<Double>optionalField("longitude2").orElse(null))
        .geodeticLatitude0(record.<String>optionalField("geodeticLatitude0").orElse(null))
        .latitude0(record.<Double>optionalField("latitude0").orElse(null))
        .geodeticLongitude0(record.<String>optionalField("geodeticLongitude0").orElse(null))
        .longitude0(record.<Double>optionalField("longitude0").orElse(null))
        .radius1(record.<Double>optionalField("radius1").orElse(null))
        .radius2(record.<Double>optionalField("radius2").orElse(null))
        .bearing1(record.<Double>optionalField("bearing1").orElse(null))
        .bearing2(record.<Double>optionalField("bearing2").orElse(null))
        .navaidIdentifier(record.<String>optionalField("navaidIdentifier").orElse(null))
        .navaidType(record.<Integer>optionalField("navaidType").orElse(null))
        .navaidCountryCode(record.<String>optionalField("navaidCountryCode").orElse(null))
        .navaidKeyCode(record.<Integer>optionalField("navaidKeyCode").orElse(null))
        .cycleDate(record.requiredField("cycleDate"))
        .build());
  }
}

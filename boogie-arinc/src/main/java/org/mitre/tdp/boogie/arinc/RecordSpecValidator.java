package org.mitre.tdp.boogie.arinc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 * The record spec validator performs the set of common checks to ensure that the provided {@link RecordSpec} meets the basic
 * expectations of the {@link ArincRecordParser} and other classes.
 * <br>
 * At a high level this class is checking four primary things:
 * <br>
 * 1. That the supplied {@code RecordSpec#recordLength() > 0}
 * 2. That the sum of the field lengths in the spec is equal to the provided expected record length
 * 3. That there are no non-unique {@link RecordField#fieldName()}s that were provided within the spec
 * 4. That the spec declares at least one non-null {@link RecordDiscriminator}
 */
public final class RecordSpecValidator implements Consumer<RecordSpec> {

  public static final RecordSpecValidator INSTANCE = new RecordSpecValidator();

  private static final Logger LOG = LoggerFactory.getLogger(RecordSpecValidator.class);

  private RecordSpecValidator() {
  }

  @Override
  public void accept(RecordSpec recordSpec) {
    LOG.debug("Beginning validation of record spec: {}", recordSpec.getClass().getSimpleName());

    checkArgument(recordSpec.recordLength() > 0, "Inferred record length based on the field spec is <= 0: ".concat(Integer.toString(recordSpec.recordLength())));

    List<RecordDiscriminator> recordDiscriminators = recordSpec.recordDiscriminators();
    checkArgument(nonNull(recordDiscriminators) && !recordDiscriminators.isEmpty(), "Record spec must declare at least one discriminator.");
    checkArgument(recordDiscriminators.stream().allMatch(Objects::nonNull), "Record spec discriminators cannot contain null.");
    checkArgument(recordDiscriminators.stream().allMatch(discriminator -> fitsWithinRecord(discriminator, recordSpec.recordLength())), "Record spec discriminators must address columns within the configured record length.");

    int fieldSpecSumLengths = recordSpec.recordFields().stream().map(RecordField::fieldSpec).mapToInt(FieldSpec::fieldLength).sum();
    checkArgument(recordSpec.recordLength() == fieldSpecSumLengths, "Mismatched specified record length and sum of field lengths: ".concat(Integer.toString(recordSpec.recordLength()).concat(" vs ").concat(Integer.toString(fieldSpecSumLengths))));

    Map<String, List<RecordField<?>>> fieldsByName = recordSpec.recordFields().stream().collect(Collectors.groupingBy(RecordField::fieldName));
    LOG.debug("Record spec contained {} uniquely named fields.", fieldsByName.size());

    List<List<RecordField<?>>> duplicateFields = fieldsByName.values().stream().filter(list -> list.size() > 1).collect(Collectors.toList());
    LOG.debug("Identified {} total duplicate fields (by name) within the provided record spec.", duplicateFields.size());

    duplicateFields.forEach(list -> LOG.warn("Duplicate record fields encountered: {}", duplicateFields));
    checkArgument(duplicateFields.isEmpty(), "Duplicate record fields encountered in spec - see log warnings for listing.");
  }

  private static boolean fitsWithinRecord(RecordDiscriminator discriminator, int recordLength) {
    if (discriminator instanceof RecordDiscriminator.Prefix prefix) {
      return prefix.value().length() <= recordLength;
    }
    RecordDiscriminator.SectionSubsection sectionSubsection =
        (RecordDiscriminator.SectionSubsection) discriminator;
    return switch (sectionSubsection.subsectionColumn()) {
      case COLUMN_6 -> recordLength >= 6;
      case COLUMN_13 -> recordLength >= 13;
    };
  }
}

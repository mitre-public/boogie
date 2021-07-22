package org.mitre.tdp.boogie.arinc;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The record spec validator performs the set of common checks to ensure that the provided {@link RecordSpec} meets the basic
 * expectations of the {@link ArincRecordParser} and other classes.
 * <br>
 * At a high level this class is checking three primary things:
 * <br>
 * 1. That the supplied {@link RecordSpec#recordLength()} > 0
 * 2. That the sum of the field lengths in the spec is equal to the provided expected record length
 * 3. That there are no non-unique {@link RecordField#fieldName()}s that were provided within the spec
 */
public final class RecordSpecValidator implements Consumer<RecordSpec> {

  public static final RecordSpecValidator INSTANCE = new RecordSpecValidator();

  private static final Logger LOG = LoggerFactory.getLogger(RecordSpecValidator.class);

  private RecordSpecValidator() {
  }

  @Override
  public void accept(RecordSpec recordSpec) {
    LOG.info("Beginning validation of record spec: {}", recordSpec.getClass().getSimpleName());

    checkArgument(recordSpec.recordLength() > 0, "Inferred record length based on the field spec is <= 0: ".concat(Integer.toString(recordSpec.recordLength())));

    int fieldSpecSumLengths = recordSpec.recordFields().stream().map(RecordField::fieldSpec).mapToInt(FieldSpec::fieldLength).sum();
    checkArgument(recordSpec.recordLength() == fieldSpecSumLengths, "Mismatched specified record length and sum of field lengths: ".concat(Integer.toString(recordSpec.recordLength()).concat(" vs ").concat(Integer.toString(fieldSpecSumLengths))));

    Map<String, List<RecordField<?>>> fieldsByName = recordSpec.recordFields().stream().collect(Collectors.groupingBy(RecordField::fieldName));
    LOG.info("Record spec contained {} uniquely named fields.", fieldsByName.size());

    List<List<RecordField<?>>> duplicateFields = fieldsByName.values().stream().filter(list -> list.size() > 1).collect(Collectors.toList());
    LOG.info("Identified {} total duplicate fields (by name) within the provided record spec.", duplicateFields.size());

    duplicateFields.forEach(list -> LOG.warn("Duplicate record fields encountered: {}", duplicateFields));
    checkArgument(duplicateFields.isEmpty(), "Duplicate record fields encountered in spec - see log warnings for listing.");
  }
}

package org.mitre.tdp.boogie.dafif;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Checks to ensrue Dafif Record Spec meets expected criteria
 *
 * 1. That the number of {@link DafifRecordSpec} meets the expected number of fields
 * 2. That there are no non-unique {@link DafifRecordField#fieldName()}s that were provided within the spec
 */
public class RecordSpecValidator implements Consumer<DafifRecordSpec> {

  public static final RecordSpecValidator INSTANCE = new RecordSpecValidator();
  @Override
  public void accept(DafifRecordSpec recordSpec) {

    checkArgument(recordSpec.recordFields().size() == recordSpec.expectedNumFields(), "Unexpected number of fields in spec.");

    Map<String, List<DafifRecordField<?>>> fieldsByName = recordSpec.recordFields().stream().collect(Collectors.groupingBy(DafifRecordField::fieldName));
    List<List<DafifRecordField<?>>> duplicateFields = fieldsByName.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
    checkArgument(duplicateFields.isEmpty(), "Duplicate record fields encountered in " + recordSpec.getClass().getSimpleName());
  }
}

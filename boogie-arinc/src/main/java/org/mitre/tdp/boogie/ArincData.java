package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Optional;


public class ArincData implements ArincRecord {

  private final String rawRecord;
  private final HashMap<String, ArincField<?>> fieldMap = new HashMap<>();
  private final HashMap<String, String> dataMap = new HashMap<>();

  public ArincData(String rawRecord) {
    this.rawRecord = rawRecord;
  }

  @Override
  public String rawRecord() {
    return rawRecord;
  }

  @Override
  public void put(ArincField<?> field, String rawValue) {
    ArincField<?> fieldRes = fieldMap.put(field.fieldName(), field);
    String valRes = dataMap.put(field.fieldName(), rawValue);

    if (fieldRes != null || valRes != null) {
      throw new DuplicateFieldException(field, rawRecord());
    }
  }

  @Override
  public <T, A extends ArincField<T>> A fieldForName(String fieldName) {
    return (A) fieldMap.get(fieldName);
  }

  @Override
  public String getRawField(String fieldName) {
    return dataMap.get(fieldName);
  }

  @Override
  public <T> Optional<T> getOptionalField(ArincField<T> spec) {
    String specField = getRawField(spec.fieldName());
    return spec.fieldSpec().parse(specField);
  }

  @Override
  public ArincRecord parseWithSpec(RecordSpec spec) {
    checkArgument(spec.matchesRecord(rawRecord()));

    int i = 0;
    int offset = 0;
    while (i < spec.recordFields().size()) {
      ArincField<?> field = spec.recordFields().get(i);
      fieldMap.put(field.fieldName(), field);
      String value = rawRecord.substring(offset, offset + field.fieldSpec().fieldLength());
      dataMap.put(field.fieldName(), value);
      i++;
      offset += field.fieldSpec().fieldLength();
    }

    checkArgument(offset == spec.recordLength(), "Final offset not equals to length of record. " + offset + " vs " + spec.recordLength());
    return this;
  }
}

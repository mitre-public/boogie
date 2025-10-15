package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.boogie.xml.v23_4.generated.A424Record;

final class ArincRecordConverter implements Function<A424Record, ArincRecordInfo> {
  static final ArincRecordConverter INSTANCE = new ArincRecordConverter();
  private ArincRecordConverter() {}
  @Override
  public ArincRecordInfo apply(A424Record a424Record) {
    return ArincRecordInfo.builder()
        .recordType(Optional.of(a424Record.getRecordType()).map(Enum::name).map(ArincRecordType::valueOf).orElseThrow(() -> new IllegalStateException("Record Type was required: " + a424Record)))
        .areaCode(Optional.ofNullable(a424Record.getAreaCode()).map(Enum::name).orElse(null))
        .customerCode(a424Record.getCustomerCode())
        .notes(a424Record.getNotes())
        .build();
  }
}

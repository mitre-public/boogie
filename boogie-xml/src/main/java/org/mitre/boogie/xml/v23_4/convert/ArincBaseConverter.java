package org.mitre.boogie.xml.v23_4.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.model.fields.SupplementalData;
import org.mitre.boogie.xml.v23_4.generated.A424Base;

final class ArincBaseConverter implements Function<A424Base, ArincBaseInfo> {
  static final ArincBaseConverter INSTANCE = new ArincBaseConverter();
  private ArincBaseConverter() {}
  @Override
  public ArincBaseInfo apply(A424Base a424Base) {
    return Optional.ofNullable(a424Base.getSupplementalData())
        .map(SupplementalData::record)
        .map(ArincBaseInfo::from)
        .orElse(null);
  }
}

package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Map;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableMap;

/**
 * The “FIR/UIR Entry Report” field is used to indicate whether an entry report on ICAO flight plan is required for that specific FIR/UIR.
 * “Y” in this field indicates Entry Report is required, “N” in this field indicates no Entry Report is required.
 * The field will be entered on the first record only for each FIR/UIR identifier.
 */
public final class FirUirEntryReport implements FieldSpec<Boolean> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.124";
  }

  private static final Map<String, Boolean> VALUE_MAP = ImmutableMap.of(
      "Y", Boolean.TRUE,
      "N", Boolean.FALSE);

  @Override
  public Optional<Boolean> apply(String string) {
    return Optional.ofNullable(string).filter(VALUE_MAP::containsKey).map(VALUE_MAP::get);
  }
}

package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import java.util.Optional;

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

  private static Boolean fromCode(String code) {
    return switch (code) {
      case "Y" -> Boolean.TRUE;
      case "N" -> Boolean.FALSE;
      default ->  null;
    };
  }

  @Override
  public Optional<Boolean> parse(String source, int startOffset, int endOffset) {
    return Optional.ofNullable(source)
        .map(s -> s.substring(startOffset, endOffset))
        .map(FirUirEntryReport::fromCode);
  }
}

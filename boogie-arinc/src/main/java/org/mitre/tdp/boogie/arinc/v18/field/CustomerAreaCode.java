package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableBiMap;

/**
 * Definition/Description: The “Customer Area Code” field permits the categorization of standard records by geographical area
 * and of tailored records by the airlines for whom they are provided in the master file. Several record types do not adhere
 * to the established geographical boundaries. There is no “AREA” in such records.
 */
public enum CustomerAreaCode implements FieldSpec<CustomerAreaCode> {
  /**
   * Intended to use to parse other boundary codes.
   * e.g. BoundaryCode.USA == BoundaryCode.SPEC.parse("U").
   */
  SPEC,
  /**
   * The United States - CONUS
   */
  USA,
  /**
   * Canada and Alaska
   */
  CAN,
  /**
   * Pacific
   */
  PAC,
  /**
   * Latin America
   */
  LAM,
  /**
   * South America
   */
  SAM,
  /**
   * South Pacific
   */
  SPA,
  /**
   * Europe
   */
  EUR,
  /**
   * Eastern Europe
   */
  EEU,
  /**
   * Middle East-South Asia
   */
  MES,
  /**
   * Africa
   */
  AFR;

  static final ImmutableBiMap<String, String> lookup = ImmutableBiMap.<String, String>builder()
      .put("U", CustomerAreaCode.USA.name())
      .put("C", CustomerAreaCode.CAN.name())
      .put("P", CustomerAreaCode.PAC.name())
      .put("L", CustomerAreaCode.LAM.name())
      .put("S", CustomerAreaCode.SAM.name())
      .put("1", CustomerAreaCode.SPA.name())
      .put("E", CustomerAreaCode.EUR.name())
      .put("2", CustomerAreaCode.EEU.name())
      .put("M", CustomerAreaCode.MES.name())
      .put("A", CustomerAreaCode.AFR.name())
      .build();

  private static final List<Optional<CustomerAreaCode>> PARSED_VALUES = lookup.values().stream()
      .map(CustomerAreaCode::valueOf)
      .map(Optional::of)
      .toList();

  public String boundaryCode() {
    return lookup.inverse().get(this.name());
  }

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.3";
  }

  @Override
  public Optional<CustomerAreaCode> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    while (startOffset < endOffset && source.charAt(startOffset) <= ' ') {
      startOffset++;
    }
    while (startOffset < endOffset && source.charAt(endOffset - 1) <= ' ') {
      endOffset--;
    }
    if (endOffset - startOffset != fieldLength()) {
      return Optional.empty();
    }

    for (int index = 0; index < PARSED_VALUES.size(); index++) {
      Optional<CustomerAreaCode> parsed = PARSED_VALUES.get(index);
      if (source.regionMatches(startOffset, parsed.orElseThrow().name(), 0, fieldLength())) {
        return parsed;
      }
    }
    return Optional.empty();
  }
}

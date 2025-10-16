package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ArincNameFormatIndicator implements Serializable {
  ABEAM,
  BEARING_DISTANCE,
  AIRPORT_NAME,
  FIR,
  PHONETIC_LETTER_NAME,
  AIRPORT_IDENT,
  LAT_LONG,
  MULTIPLE_WORD,
  NAVAID,
  PUBLISHED_FIVE_LETTER_NAME,
  PUBLISHED_LESS_THAN_FIVE_LETTER_NAME,
  PUBLISHED_MORE_THAN_FIVE_NAME,
  APT_RWY_RELATED,
  UIR,
  OFFICIAL_FIVE_LETTER;

  public static final Set<String> VALID = Arrays.stream(ArincNameFormatIndicator.values()).map(ArincNameFormatIndicator::name).collect(Collectors.toSet());
}

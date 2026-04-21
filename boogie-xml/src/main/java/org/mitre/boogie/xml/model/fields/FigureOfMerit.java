package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum FigureOfMerit {
  TERMINAL_USE,
  LOW_ALT,
  HIGH_ALT,
  EXTENDED_HIGH_ALT,
  NOT_NOTA_MD,
  OUT_OF_SERVICE;
  public static final Set<String> VALID = Arrays.stream(FigureOfMerit.values()).map(FigureOfMerit::name).collect(Collectors.toUnmodifiableSet());
}

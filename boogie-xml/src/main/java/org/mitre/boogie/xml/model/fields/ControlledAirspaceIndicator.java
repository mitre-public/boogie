package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ControlledAirspaceIndicator {
  WithinOrBelowClassC,
  WithinOrBelowCta,
  WithinOrBelowTmsTca,
  WithinOrBelowRadarZone,
  WithinOrBelowClassB;
  public static Set<String> VALID = Arrays.stream(ControlledAirspaceIndicator.values()).map(Enum::name).collect(Collectors.toSet());
}

package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NavaidNdbEmissionType {
  UNMODULATED_CARRIER,
  CARRIER_KEYED,
  TONE_KEY_MODULATION;
  public static final Set<String> VALID = Arrays.stream(NavaidNdbEmissionType.values()).map(NavaidNdbEmissionType::name).collect(Collectors.toUnmodifiableSet());
}

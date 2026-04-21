package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum SignalEmission {
  LSB_CARRIER_UNK,
  USB_CARRIER_UNK;
  public static final Set<String> VALID = Arrays.stream(SignalEmission.values()).map(SignalEmission::name).collect(Collectors.toUnmodifiableSet());
}

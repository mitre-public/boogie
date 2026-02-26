package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum NavaidVoice {
  VOICE_IDENT,
  NO_VOICE_IDENT,
  UNDEFINED;
  public static final Set<String> VALID = Arrays.stream(NavaidVoice.values()).map(NavaidVoice::name).collect(Collectors.toUnmodifiableSet());
}

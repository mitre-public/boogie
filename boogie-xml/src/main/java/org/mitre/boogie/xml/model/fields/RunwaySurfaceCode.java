package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RunwaySurfaceCode {
  Hard,
  Soft,
  Water,
  Undefined;
  public static Set<String> VALID = Arrays.stream(RunwaySurfaceCode.values()).map(RunwaySurfaceCode::name).collect(Collectors.toSet());
}

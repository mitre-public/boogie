package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

@FunctionalInterface
public interface RouteIdentifier extends FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  default String fieldCode() {
    return "5.8";
  }

  static RouteIdentifier enroute() {
    return () -> 5;
  }

  static RouteIdentifier preferred() {
    return () -> 10;
  }
}

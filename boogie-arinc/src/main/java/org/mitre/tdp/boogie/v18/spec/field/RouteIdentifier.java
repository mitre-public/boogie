package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.FieldSpec;

@FunctionalInterface
public interface RouteIdentifier extends FieldSpec<String> {

  @Override
  default String fieldCode() {
    return "5.8";
  }

  @Override
  default String parse(String fieldString) {
    return fieldString;
  }

  static RouteIdentifier enroute() {
    return () -> 5;
  }

  static RouteIdentifier preferred() {
    return () -> 10;
  }
}

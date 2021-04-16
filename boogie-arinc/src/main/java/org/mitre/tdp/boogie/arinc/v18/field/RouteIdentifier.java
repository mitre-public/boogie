package org.mitre.tdp.boogie.arinc.v18.field;

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

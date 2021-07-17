package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * For Enroute Airways, Route Identifier codes should be derived from official government publications.
 * <br>
 * For Preferred Routes, Route Identifiers may or may not be provided in government publications. Where they are available,
 * they will be used.
 * <br>
 * For North American Routes for North Atlantic Traffic, “Common Portion” and other similar route system, route identifier
 * code shall be those published in government sources.
 */
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

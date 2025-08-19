package org.mitre.tdp.boogie.alg.split;

import java.util.List;

import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;

/**
 * Responsible for splitting an input route string into a sequence of elements matchable to infrastructure elements based on ID
 * e.g. {@code KATL.CHPPR1.DRSDN.J121.JMACK..HOBTT.GNDLF1.KORD} to {@code [KATL, CHPPR1, J121, DRSDN, etc.]}.
 */
@FunctionalInterface
public interface RouteTokenizer {

  /**
   * This deals with FAA or ICAO formats. Each string must be consistent, but each string can switch.
   * @return The combined tokenizer
   */
  static RouteTokenizer combinedFormat() {
    return new CombinedRouteTokenizer();
  }

  /**
   * Route tokenizer for FAA IFR format route strings.
   *
   * <p>e.g. {@code KATL.BOOVE4.DRSDN..AMF.J121.RPA..WYNDE.WYNDE8.KORD/0211}
   *
   * @return The faa tokenizer
   */
  static RouteTokenizer faaIfrFormat() {
    return new FaaIfrFormatTokenizer();
  }

  /**
   * Route tokenizer for International ICAO format route strings.
   *
   * <p>e.g. {@code DCT GREKI IFR DCT BGO/N0485F410 M626 VKB M751 VPK B469 BIKTA PIBAP PAS3 MABA2A}
   *
   * @return The icao tokenizer
   */
  static RouteTokenizer icaoFormat() {
    return new IcaoFormatTokenizer();
  }

  /**
   * Tokenize an input route of some (potentially undetermined format) and convert it to token identifiers which can be matched
   * against infrastructure using various {@link RouteTokenResolver} implementations.
   *
   * @param route the stringified route-of-flight for an aircraft
   *
   * @return the list of route tokens split by the concrete class
   */
  List<RouteToken> tokenize(String route);
}

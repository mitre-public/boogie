package org.mitre.tdp.boogie.alg.split;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public final class CombinedRouteTokenizer implements RouteTokenizer {

  private static final RouteTokenizer IFR = RouteTokenizer.faaIfrFormat();

  private static final RouteTokenizer INT = RouteTokenizer.icaoFormat();

  private static final Predicate<String> IS_INT = i -> i.contains(" ") && !i.contains(".") && !i.contains("..");

  private static final Predicate<String> FUBAR = i -> i.contains(" ") && (i.contains(".") || i.contains(".."));

  CombinedRouteTokenizer() {
  }

  @Override
  public List<RouteToken> tokenize(String route) {
    if (FUBAR.or(Objects::isNull).test(route)) {
      throw new IllegalArgumentException("The flight plan is either null or mixed between FAA/ICAO formatting which is bad :" + route);
    }
    return Optional.of(route).filter(IS_INT).map(INT::tokenize).orElseGet(() -> IFR.tokenize(route));
  }
}

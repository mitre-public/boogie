package org.mitre.tdp.boogie.alg.split;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This is meant to tokenize TFM restrictions into a list of {@link RouteToken}s.
 * TFM restrictions come with airports and then the restriction elements that are meant to be resolved here.
 */
public final class TfmRestrictionTokenizer implements RouteTokenizer {
  TfmRestrictionTokenizer() {}
  @Override
  public List<RouteToken> tokenize(String route) {
    String[] splits = route.split("/");
    return IntStream.range(0, splits.length)
        .mapToObj(i -> {
          String tkn = splits[i].trim();
          if (tkn.isBlank()) {
            return null;
          }
          return RouteToken.standard(tkn, i);
        })
        .filter(Objects::nonNull)
        .toList();
  }
}

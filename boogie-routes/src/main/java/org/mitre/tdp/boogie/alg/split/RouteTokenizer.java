package org.mitre.tdp.boogie.alg.split;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface RouteTokenizer  {

  static RouteTokenizer faaIfrFormat() {
    return new FaaIfrFormatSplitter();
  }

  List<RouteToken> tokenize(String route);
}

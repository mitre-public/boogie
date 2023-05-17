package org.mitre.tdp.boogie.alg.split;

import java.util.List;

public interface RouteTokenizer {

  List<RouteToken> tokenize(String route);
}

package org.mitre.tdp.boogie.alg;

@FunctionalInterface
public interface Expander {

  ExpandedRoute expand(String route, RouteContext context);
}

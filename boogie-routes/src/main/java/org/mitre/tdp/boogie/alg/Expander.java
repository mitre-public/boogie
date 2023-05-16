package org.mitre.tdp.boogie.alg;

import java.util.Optional;

@FunctionalInterface
public interface Expander {

  static Standard.Builder standardBuilder() {
    return new Standard.Builder();

  }

  Optional<ExpandedRoute> expand(String route, RouteContext context);

  final class Standard implements Expander {

    @Override
    public Optional<ExpandedRoute> expand(String route, RouteContext context) {
      return Optional.empty();
    }

    public static final class Builder {

    }
  }
}

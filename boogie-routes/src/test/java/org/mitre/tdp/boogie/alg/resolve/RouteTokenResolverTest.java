package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class RouteTokenResolverTest {

  @Test
  void testAnd() {

    RouteTokenResolver resolver = new DumbResolver(new DumbToken("A"))
        .and(new DumbResolver(new DumbToken("B")));

    ResolvedTokens tokens = resolver.resolve(null, RouteToken.standard("AB", 1), null);
    assertEquals(Set.of("A", "B"), tokens.resolvedTokens().stream().map(ResolvedToken::identifier).collect(toSet()));
  }

  @Test
  void testOr1() {

    RouteTokenResolver resolver = new DumbResolver(new DumbToken("A"))
        .or(new DumbResolver(new DumbToken("B")));

    ResolvedTokens tokens = resolver.resolve(null, RouteToken.standard("AB", 1), null);
    assertEquals(Set.of("A"), tokens.resolvedTokens().stream().map(ResolvedToken::identifier).collect(toSet()));
  }

  @Test
  void testOr2() {

    RouteTokenResolver resolver = RouteTokenResolver.noop()
        .or(new DumbResolver(new DumbToken("B")));

    ResolvedTokens tokens = resolver.resolve(null, RouteToken.standard("AB", 1), null);
    assertEquals(Set.of("B"), tokens.resolvedTokens().stream().map(ResolvedToken::identifier).collect(toSet()));
  }

  private static final class DumbResolver implements RouteTokenResolver {

    private final ResolvedToken toReturn;

    private DumbResolver(ResolvedToken toReturn) {
      this.toReturn = requireNonNull(toReturn);
    }

    @Override
    public ResolvedTokens resolve(@Nullable RouteToken previous, RouteToken current, @Nullable RouteToken next) {
      return new ResolvedTokens(current, List.of(toReturn));
    }
  }

  private static final class DumbToken implements ResolvedToken {

    private final String identifier;

    private DumbToken(String identifier) {
      this.identifier = requireNonNull(identifier);
    }

    @Override
    public String identifier() {
      return identifier;
    }

    @Override
    public String infrastructure() {
      return identifier;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
    }
  }
}

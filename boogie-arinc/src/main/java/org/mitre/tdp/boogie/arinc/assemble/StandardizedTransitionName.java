package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Optional;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Transition;

/**
 * Standardization function for {@link Transition} names across sources - ENROUTE/RUNWAY/APPROACH transitions should generally all
 * be consistently named but most COMMON portions of procedures are named any of {@code null, "", or "ALL" -> "ALL"} with this.
 */
public final class StandardizedTransitionName implements UnaryOperator<String> {

  public static final StandardizedTransitionName INSTANCE = new StandardizedTransitionName();

  @Override
  public String apply(@Nullable String transitionName) {
    return Optional.ofNullable(transitionName).map(String::trim).filter(s -> !s.isEmpty()).orElse("ALL");
  }
}

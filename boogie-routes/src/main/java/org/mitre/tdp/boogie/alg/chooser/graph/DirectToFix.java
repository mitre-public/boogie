package org.mitre.tdp.boogie.alg.chooser.graph;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Fix;

final class DirectToFix implements GraphableToken {

  private final Fix fix;

  DirectToFix(Fix fix) {
    this.fix = requireNonNull(fix);
  }
}

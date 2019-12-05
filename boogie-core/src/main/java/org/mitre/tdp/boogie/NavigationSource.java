package org.mitre.tdp.boogie;

public enum NavigationSource {
  FUSED, JEPPESEN, CIFP, NFDC;

  public double transitionPenalty() {
    return (double) this.ordinal() / (double) NavigationSource.values().length;
  }
}

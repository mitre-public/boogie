package org.mitre.tdp.boogie.conformance;

@FunctionalInterface
public interface Scorable<U> {

  Scorer<U> scorer();
}

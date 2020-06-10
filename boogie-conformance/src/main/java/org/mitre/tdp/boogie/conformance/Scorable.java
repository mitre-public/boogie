package org.mitre.tdp.boogie.conformance;

@FunctionalInterface
public interface Scorable<U, S extends Scorable<U, S>> {

  Scorer<U, S> scorer();
}

package org.mitre.tdp.boogie.conformance;

public interface Scorable<U> {

  Scorer<U> scorer();
}

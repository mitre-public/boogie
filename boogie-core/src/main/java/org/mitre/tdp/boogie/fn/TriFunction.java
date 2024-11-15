package org.mitre.tdp.boogie.fn;

@FunctionalInterface
public interface TriFunction<U, V, W, T> {

  T apply(U u, V v, W w);
}

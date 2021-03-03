package org.mitre.tdp.boogie.fn;

@FunctionalInterface
public interface QuadFunction<U, V, W, T, P> {

  P apply(U u, V v, W w, T t);
}

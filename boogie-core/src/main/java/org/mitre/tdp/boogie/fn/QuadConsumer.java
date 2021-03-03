package org.mitre.tdp.boogie.fn;

@FunctionalInterface
public interface QuadConsumer<U, V, W, T> {

  void accept(U u, V v, W w, T t);
}

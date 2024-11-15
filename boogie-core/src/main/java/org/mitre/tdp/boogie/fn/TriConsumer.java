package org.mitre.tdp.boogie.fn;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Same functionality as {@link BiConsumer} but for triplets of input arguments.
 */
@FunctionalInterface
public interface TriConsumer<U, V, T> {

  void accept(U u, V v, T t);

  default TriConsumer<U, V, T> andThen(TriConsumer<? super U, ? super V, ? super T> after) {
    Objects.requireNonNull(after);

    return (l, r, v) -> {
      accept(l, r, v);
      after.accept(l, r, v);
    };
  }
}

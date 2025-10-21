package org.mitre.tdp.boogie.arinc.utils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

public final class DimensionsParser implements BiFunction<String, PadShape, Optional<Dimensions>> {
  public static final DimensionsParser INSTANCE = new DimensionsParser();

  private static final Function<String, Optional<Dimensions>> CIRCULAR = s -> Optional.ofNullable(s)
      .map(i -> i.substring(0, 5))
      .map(Double::parseDouble)
      .map(i -> new Dimensions(null, null, i));

  private static final Function<String, Optional<Dimensions>> RECTANGULAR = s -> Optional.ofNullable(s)
      .map(i -> {
        String sy = i.substring(0, 5);
        String sx = i.substring(5, 8);
        Double y = Double.valueOf(sy);
        Double x = Double.valueOf(sx);
        return new Dimensions(x, y, null);
      });

  private DimensionsParser() {
  }

  @Override
  public Optional<Dimensions> apply(String raw, PadShape shape) {
    return switch (shape) {
      case C -> CIRCULAR.apply(raw);
      case S, R -> RECTANGULAR.apply(raw);
      case U -> Optional.empty();
      case SPEC -> throw new IllegalStateException("Can't have SPEC as value: ".concat(raw));
    };
  }
}

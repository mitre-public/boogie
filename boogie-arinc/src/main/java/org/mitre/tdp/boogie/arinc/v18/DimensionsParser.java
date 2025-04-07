package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.utils.Dimensions;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

public final class DimensionsParser implements BiFunction<String, PadShape, Optional<Dimensions>> {
  public static final DimensionsParser INSTANCE = new DimensionsParser();

  private static final Function<String, Optional<Dimensions>> CIRCULAR = s -> Optional.ofNullable(s)
      .map(i -> i.substring(0, 3))
      .map(Double::parseDouble)
      .map(i -> new Dimensions(null, null, i));

  private static final Function<String, Optional<Dimensions>> RECTANGULAR = s -> Optional.ofNullable(s)
      .map(i -> {
        String sy = i.substring(0, 3);
        String sx = i.substring(3);
        Double y = Double.valueOf(sy);
        Double x = Double.valueOf(sx);
        return new Dimensions(x, y, null);
      });

  private DimensionsParser() {}
  @Override
  public Optional<Dimensions> apply(String s, PadShape shape) {
    return switch (shape) {
      case R -> RECTANGULAR.apply(s);
      case C -> CIRCULAR.apply(s);
      case U -> Optional.empty();
      default -> throw new IllegalArgumentException("Unknown shape for supplement 18" + shape);
    };
  }
}

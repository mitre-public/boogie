package org.mitre.tdp.boogie.dafif.v81.converter;

import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.model.enums.Shape;

/**
 * Converts a parsed DAFIF 8.1 shape code into its model value.
 */
public final class ShapeConverter implements Function<String, Shape> {

  public static final ShapeConverter INSTANCE = new ShapeConverter();

  private ShapeConverter() {
  }

  /**
   * @throws IllegalArgumentException if the code is null or unsupported
   */
  @Override
  public Shape apply(String code) {
    if (code == null) {
      throw new IllegalArgumentException("DAFIF shape code cannot be null.");
    }
    return switch (code) {
      case "A" -> Shape.POINT;
      case "B" -> Shape.GREAT_CIRCLE;
      case "C" -> Shape.CIRCLE;
      case "G" -> Shape.GENERALIZED;
      case "H" -> Shape.RHUMB_LINE;
      case "L" -> Shape.COUNTERCLOCKWISE_ARC;
      case "R" -> Shape.CLOCKWISE_ARC;
      default -> throw new IllegalArgumentException("Unsupported DAFIF shape code: " + code);
    };
  }
}

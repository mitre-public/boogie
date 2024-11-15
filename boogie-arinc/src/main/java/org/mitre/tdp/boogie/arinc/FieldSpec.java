package org.mitre.tdp.boogie.arinc;

import java.util.Optional;
import java.util.function.Function;

/**
 * Interface representing the specification for a logical field type from the ARINC specification.
 * <br>
 * Fields are explicitly tied to their ARINC field code and multiple fields within a record may match the same spec.
 * <br>
 * e.g. latitude is stored the same within a navaid regardless of whether it's the latitude of the navaid itself or it's non
 * collocated DME.
 */
public interface FieldSpec<T> extends Function<String, Optional<T>> {

  /**
   * The length of the field in characters.
   */
  int fieldLength();

  /**
   * The string code name for the field in the ARINC spec.
   */
  String fieldCode();

  /**
   * The default field name for the spec field in a {@link ArincRecord}.
   * <br>
   * The field name to use in accordance with {@link RecordField} when no explicit field name is otherwise provided in the {@link RecordSpec}.
   * <br>
   * Default value for this is camelCase version of the implementing class's simple name. Note that within a {@link RecordSpec}
   * these can be cross-checked to prevent mismatches with the {@link RecordSpecValidator}.
   */
  default String fieldName() {
    String simpleName = this.getClass().getSimpleName();
    return simpleName.substring(0, 1).toLowerCase().concat(simpleName.substring(1));
  }
}
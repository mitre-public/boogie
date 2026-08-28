package org.mitre.tdp.boogie.arinc;

import java.util.Optional;
import java.util.function.Function;

/**
 * Interface representing the specification for a logical field type from the ARINC specification.
 * <p>
 * Fields are explicitly tied to their ARINC field code and multiple fields within a record may match the same spec.
 * <p>
 * e.g. latitude is stored the same within a navaid regardless of whether it's the latitude of the navaid itself or it's non
 * collocated DME.
 * <p>
 * Implementations must be deterministic and side effect free, and returned values must be immutable or otherwise safe to share.
 * {@link ArincRecord} may cache and return the same result for repeated applications to a particular field value.
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

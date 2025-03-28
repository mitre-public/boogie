package org.mitre.tdp.boogie.dafif;

import java.util.Optional;
import java.util.function.Function;

/**
 * Interface representing the specification for a logical field type from the DAFIF specification.
 * <br>
 * Fields are explicitly tied to their DAFIF field code and multiple fields within a record may match the same spec.
 * <br>
 * e.g. latitude is stored the same within a navaid regardless of whether it's the latitude of the navaid itself or it's non
 * collocated DME.
 */
public interface DafifFieldSpec<T> extends Function<String, Optional<T>> {

  /**
   * The maximum length of the field in characters.
   */
  int maxFieldLength();

  /**
   * The string code name for the field in the DAFIF spec.
   */
  int fieldCode();

  String regex();

  /**
   * The default field name for the spec field in a {@link DafifRecord}.
   * <br>
   * The field name to use in accordance with {@link DafifRecordField} when no explicit field name is otherwise provided in the {@link DafifRecordSpec}.
   * <br>
   * Default value for this is camelCase version of the implementing class's simple name. Note that within a {@link DafifRecordSpec}
   * these can be cross-checked to prevent mismatches with the {@link RecordSpecValidator}.
   */
  default String fieldName() {
    String simpleName = this.getClass().getSimpleName();
    return simpleName.substring(0, 1).toLowerCase().concat(simpleName.substring(1));
  }
}

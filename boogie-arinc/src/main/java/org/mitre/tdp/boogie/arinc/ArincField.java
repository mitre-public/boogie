package org.mitre.tdp.boogie.arinc;

import java.io.Serializable;
import java.util.Objects;

/**
 * This class represents a mapping of a field spec to the string value from arinc 424.
 */
public final class ArincField implements Serializable {
  /**
   * The data definition and function to convert the data to something useful like a double/integer
   */
  private final FieldSpec<?> spec;
  /**
   * The raw arinc 424 string of data.
   */
  private final String data;

  public ArincField(FieldSpec<?> spec, String data) {
    this.spec = spec;
    this.data = data;
  }

  public FieldSpec<?> spec() {
    return spec;
  }

  public String data() {
    return data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ArincField that = (ArincField) o;
    return Objects.equals(spec, that.spec) && Objects.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(spec, data);
  }

  @Override
  public String toString() {
    return "ArincField{" +
        "spec=" + spec +
        ", data='" + data + '\'' +
        '}';
  }
}

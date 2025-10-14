package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Objects;

public interface SupplementalData extends Serializable {
  static <T> Record<T> record(T datum) {
    return new Record<>(datum);
  }

  void accept(Visitor visitor);

  interface Visitor {
    void visit(Record<?> record);
  }

  final class Record<T> implements SupplementalData {
    private final T datum;

    private Record(T datum) {
      this.datum = datum;
    }

    public T datum() {
      return datum;
    }

    @Override
    public void accept(Visitor visitor) {
      visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      Record<?> record = (Record<?>) o;
      return Objects.equals(datum, record.datum);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(datum);
    }

    @Override
    public String toString() {
      return "Record{" +
          "datum=" + datum +
          '}';
    }
  }
}

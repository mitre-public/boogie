package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Routes of flight frequently cross geographical boundaries. The “Boundary Code” field identifies the area into, or from which
 * a continuous route passes when such a crossing occurs.
 */
public final class BoundaryCode implements FieldSpec<CustomerAreaCode> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.18";
  }

  @Override
  public Optional<CustomerAreaCode> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(s -> !s.isEmpty()).map(CustomerAreaCode.lookup::get).map(CustomerAreaCode::valueOf);
  }
}

package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

public abstract class ArincInteger implements FieldSpec<Integer> {

  @Override
  public Optional<Integer> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(Integer::parseInt);
  }
}

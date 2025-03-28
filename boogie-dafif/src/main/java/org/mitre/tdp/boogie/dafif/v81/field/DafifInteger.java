package org.mitre.tdp.boogie.dafif.v81.field;

import org.mitre.tdp.boogie.dafif.DafifFieldSpec;
import org.mitre.tdp.boogie.dafif.utils.ValidDafifNumeric;

import java.util.Optional;

abstract class DafifInteger implements DafifFieldSpec<Integer> {

  @Override
  public Optional<Integer> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidDafifNumeric.INSTANCE).map(Integer::parseInt);
  }
}

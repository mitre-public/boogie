package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

public abstract class ArincDouble implements FieldSpec<Double> {

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(Double::parseDouble);
  }
}

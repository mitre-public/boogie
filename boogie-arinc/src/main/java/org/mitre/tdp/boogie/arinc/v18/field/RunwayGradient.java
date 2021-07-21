package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

public final class RunwayGradient implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.212";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(ArincStrings::parseDoubleWithThousandths);
  }
}

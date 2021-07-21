package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * This field is used to specify the DME offset.
 * <br>
 * The field contains a 2-digit bias term in nautical miles and tenths of a nautical mile with the decimal point suppressed.
 * Field is blank for unbiased DME’s.
 */
public final class IlsDmeBias implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.90";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(ArincStrings::parseDoubleWithTenths);
  }
}

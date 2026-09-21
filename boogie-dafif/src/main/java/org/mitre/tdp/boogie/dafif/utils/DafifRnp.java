package org.mitre.tdp.boogie.dafif.utils;

import java.math.BigDecimal;

/** Decodes DAFIF field 266's two-digit mantissa and one-digit negative exponent. */
public final class DafifRnp {

  private DafifRnp() {}

  public static BigDecimal nauticalMiles(int encoded) {
    return BigDecimal.valueOf(encoded / 10).movePointLeft(encoded % 10);
  }
}

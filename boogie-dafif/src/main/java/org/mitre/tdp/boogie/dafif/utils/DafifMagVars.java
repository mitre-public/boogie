package org.mitre.tdp.boogie.dafif.utils;

import org.mitre.tdp.boogie.MagneticVariation;

public final class DafifMagVars {

  public static MagneticVariation fromRecord(String data) {
    try {
      int sign = 'W' == data.charAt(0) ? -1 : 1;
      double degrees = Double.parseDouble(data.substring(1, 4));
      double tenths = Double.parseDouble(data.substring(4, 5));
      return MagneticVariation.ofDegrees(sign * (degrees + (.1 * tenths)));

    } catch (Exception ex) {
      throw new IllegalStateException("We wer missing data from the magvar: ".concat(data));
    }
  }

  public static MagneticVariation fromDynamic(String data) {
    try {
      if (data.indexOf(' ') == 7) {
        int sign = 'W' == data.charAt(0) ? -1 : 1;
        int degrees = Integer.parseInt(data.substring(1, 4));
        int minutes = Integer.parseInt(data.substring(4, 7));
        return MagneticVariation.ofDegrees(sign * (degrees + minutes / 600.0));

      } else {
        int sign = 'W' == data.charAt(0) ? -1 : 1;
        int degrees = Integer.parseInt(data.substring(1, 4));
        return MagneticVariation.ofDegrees(sign * degrees);
      }
    } catch (Exception ex) {
      throw new IllegalArgumentException("the magvar is missing data: ".concat(data));
    }
  }
}

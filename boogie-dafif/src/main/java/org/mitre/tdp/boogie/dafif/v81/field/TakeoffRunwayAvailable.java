package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LENGTH OF RUNWAY DECLARED AVAILABLE AND SUITABLE FOR THE GROUND RUN OF AN AEROPLANE TAKING OFF.
 *
 * EXAMPLE(S):
 * 09001
 * 00820
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 00001 - 40000 (VALUES ARE PADDED WITH LEADING ZEROS)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * THIS VALUE IS ONLY PROVIDED BY HOST. NGA WILL PROVIDE THE HOST VALUE EVEN WHEN
 * IT CONFLICTS WITH VALUES DERIVED BY IMAGERY.
 */
public class TakeoffRunwayAvailable extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 448;
  }

  @Override
  public String regex() {
    return "((0([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})|[1-3][0-9]{4}|40000)?)";
  }
}

package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE DECLARED LENGTH OF THE TAKEOFF RUN AVAILABLE PLUS THE LENGTH OF THE STOPWAY, IF PROVIDED.
 *
 * EXAMPLE(S):
 * 00997
 * 14674
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
 * THIS VALUE IS ONLY PROVIDED BY HOST. NGA WILL PROVIDE THE HOST VALUE EVEN WHEN IT
 * CONFLICTS WITH VALUES DERIVED BY IMAGERY.
 */
public class AccelerateStopDistanceAvailable extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 450;
  }

  @Override
  public String regex() {
    return "((0([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})|[1-3][0-9]{4}|40000)?)";
  }
}

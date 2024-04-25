package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE DISTANCE IN FEET FROM THE DESIGNATED BEGINNING OF THE LOW END OF THE RUNWAY TO THAT
 * PORTION ON THE LOW END OF THE RUNWAY USABLE FOR LANDING.
 *
 * EXAMPLE(S):
 * 6564
 * 0014
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0001-9999 (PADDED WITH LEADING ZEROS)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * THIS VALUE MAY BE DERIVED IN THE ABSENSE OF HOST PROVIDED VALUES USING IMAGERY OR MENSURATION
 * TO PROVIDE PRECISE MEASUREMENT FOR THIS FIELD IN ACCORDANCE WITH DO-200B AND DO-201.
 */
public class LowEndDisplacedThreshold extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 97;
  }

  @Override
  public String regex() {
    return "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})?)";
  }
}

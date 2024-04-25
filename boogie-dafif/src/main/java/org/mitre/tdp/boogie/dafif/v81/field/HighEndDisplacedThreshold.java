package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE DISTANCE IN FEET FROM THE DESIGNATED BEGINNING OF THE LOW END OF THE RUNWAY TO THAT 	PORTION OTHE
 * LOW END OF THE RUNWAY USABLE FOR LANDING.  EXAMPLE(S): 6564 0014 NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWABLE VALUES:
 * 0001-9999 (PADDED WITH LEADING ZEROS) 	OR 	NULL
 *
 * INTENDED USE:
 * THIS VALUE MAY BE DERIVED IN THE ABSENSE OF HOST PROVIDED VALUES USING IMAGERY OR MENSURATION TO PROVIDE
 * PRECISE MEASUREMENT FOR THIS FIELD IN ACCORDANCE WITH DO-200B AND DO-201.
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 */
public class HighEndDisplacedThreshold extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 56;
  }

  @Override
  public String regex() {
    return "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})?)";
  }
}

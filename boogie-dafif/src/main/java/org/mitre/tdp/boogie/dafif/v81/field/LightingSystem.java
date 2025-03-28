package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * VARIOUS LIGHTING AIDS THAT MAY BE INSTALLED ON A AIRPORT/HELIPORT (RUNWAY/PAD LIGHTING, APPROACH  LIGHTING, AND VISUAL GLIDE SLOPE INDICATORS).
 *
 * NOTE:  LIGHTING DEPICTIONS CAN BE FOUND IN THE DOD FLIGHT INFORMATION HANDBOOK (FIH).
 *
 * EXAMPLE(S)
 * NULL
 * 05
 * 55
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 01 - 56 (PADDED WITH LEADING ZEROS)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * Note: When coming from Jeppesen, does not seem to contain the appropriate padded zeros
 */
public final class LightingSystem extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 165;
  }

  @Override
  public String regex() {
    return "(([1-9]|0[1-9]|[1-4][0-9]|5[0-6])?)";
  }
}
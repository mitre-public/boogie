package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE TYPE OF COMPONENT USED TO HELP PROVIDE AN APPROACH PATH FOR EXACT ALIGNMENT AND DESCENT ON FINAL APPROACH TO A RUNWAY.
 * B - BACK COURSE MARKER
 * D - DME
 * G - GLIDE SLOPE
 * I - INNER MARKER
 * L - LOCATOR
 * M - MIDDLE MARKER
 * O - OUTER MARKER
 * P - MLS DME
 * S - MLS LOCALIZER
 * V- MLS GLIDE SLOPE
 * Z - LOCALIZER
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * 1	B, D, G, I, L, M, O, P, S, V, Z
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class ComponentType extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 77;
  }

  @Override
  public String regex() {
    return "(B|D|G|I|L|M|O|P|S|V|Z)";
  }
}

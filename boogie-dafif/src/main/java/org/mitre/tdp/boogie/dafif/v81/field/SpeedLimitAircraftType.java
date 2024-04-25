package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE AIRCRAFT CATEGORY FOR THE SPECIFIED SPEED LIMIT RESTRICTION:
 *
 * EXAMPLE(S):
 * A   (ALL AIRCRAFT)
 * J   (JETS ONLY (TURBOJETS, JETS, ETC))
 * T   (TURBOPROPS ONLY)
 * O  (OTHER)
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * A, J, T, O
 *  OR
 *  NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class SpeedLimitAircraftType extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 296;
  }

  @Override
  public String regex() {
    return "((A|J|T|O)?)";
  }
}
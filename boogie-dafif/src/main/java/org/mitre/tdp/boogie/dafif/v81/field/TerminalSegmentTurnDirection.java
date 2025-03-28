package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES THE SPECIFIC DIRECTION IN WHICH COURSE TURNS AND HOLDING PATTERNS ARE TO BE MADE.
 *
 * FOR TRACK DESC CODES AF, HA, HM, HF, AND PI, A TURN DIRECTION IS REQUIRED.  FOR ALL OTHER TRACK CODES, REQUIRED FOR A TURN DIRECTION OVER 90 DEGREES.
 *
 *  L - LEFT TURN
 *  R - RIGHT TURN
 *
 * EXAMPLE(S):
 * NULL
 * L
 * R
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * L, R
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * REQUIRED FOR A TURN  DIRECTION OVER 90 DEGREES. IN PERFORMANCE BASED SITUATIONS, TURN DIRECTION WILL BE POPULATED ONLY WHEN A TURN CAN BE VERIFIED GREATER THAN 90 DEGREES.
 */
public final class TerminalSegmentTurnDirection extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 325;
  }

  @Override
  public String regex() {
    return "((L|R)?)";
  }
}
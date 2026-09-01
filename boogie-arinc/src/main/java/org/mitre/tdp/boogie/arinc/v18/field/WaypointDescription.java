package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.checkFromToIndex;

/**
 * 4-Character string designating the type, function, and attributes of a specific fix in enroute airway or terminal procedure
 * encoding.
 * <br>
 * This was optionally populated at the level of the fix records but is almost always encoded in the procedure when referencing
 * a fix.
 * <br>
 * The full set of valid values and meanings per column is:
 * <br>
 * <b>Column1:</b>
 * A - Airport as waypoint (STAR/APCH)
 * E - Essential waypoint (Enroute/SID/STAR/APCH)
 * F - Off airway floating waypoint (Enroute)
 * G - Runway as waypoint (SID/STAR/APCH)
 * H - Heliport as waypoint (STAR/APCH)
 * N - Ndb navaid as waypoint (Enroute/SID/STAR/APCH)
 * P - Phantom waypoint (SID/STAR/APCH)
 * R - Non-essential waypoint (Enroute)
 * T - Transition essential waypoint (Enroute)
 * V - Vhf navaid as waypoint (Enroute/SID/STAR/APCH)
 * <br>
 * <b>Column2:</b>
 * B - Flyover waypoint, ending leg (SID/STAR/APCH)
 * E - End of continuous segment (Enroute/SID/STAR/APCH)
 * U - Uncharted airway intersection (Enroute)
 * Y - Flyover waypoint (SID/STAR/APCH)
 * <br>
 * <b>Column3:</b>
 * A - Unnamed stepdown fix final approach segment (APCH)
 * B - Unnamed stepdown fix intermediate approach segment (APCH)
 * C - ATC compulsory reporting point (Enroute/SID/STAR/APCH)
 * G - Oceanic gateway point (Enroute)
 * M - First leg of missed approach procedure (APCH)
 * P - Path point fix
 * R - Fix used for turn to final (APCH)
 * S - Named stepdown fix (APCH)
 * <br>
 * <b>Column4:</b>
 * A - Initial approach fix (APCH)
 * B - Intermediate approach fix (APCH)
 * C - Holding at initial approach fix (APCH)
 * D - Initial approach fix at FACF (APCH)
 * E - Final end point (APCH)
 * F - Final approach fix (APCH)
 * G - Source provided enroute waypoint without holding (Enroute)
 * H - Source provided enroute waypoint with holding (Enroute)
 * I - Final approach course fix (APCH)
 * M - Missed approach point (APCH)
 * N - Engine out SID missed approach disarm point (SID - engine out, APCH)
 */
public final class WaypointDescription implements FieldSpec<String> {

  private static final String COLUMN_1_VALUES = " AEFGHNPRTV";
  private static final String COLUMN_2_VALUES = " BEUY";
  private static final String COLUMN_3_VALUES = " ABCGMPRS";
  private static final String COLUMN_4_VALUES = " ABCDEFGHIMN";
  private static final int DESCRIPTION_COUNT = COLUMN_1_VALUES.length() * COLUMN_2_VALUES.length() * COLUMN_3_VALUES.length() * COLUMN_4_VALUES.length();
  private static final List<Optional<String>> DESCRIPTIONS = descriptions();

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.17";
  }

  @Override
  public Optional<String> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset < fieldLength()) {
      // All four fixed-width positions are required.
      throw new StringIndexOutOfBoundsException(endOffset - startOffset);
    }
    return filteredDescription(source, startOffset);
  }

  private static Optional<String> filteredDescription(String source, int startOffset) {
    int column1 = normalizedIndex(COLUMN_1_VALUES, source.charAt(startOffset));
    int column2 = normalizedIndex(COLUMN_2_VALUES, source.charAt(startOffset + 1));
    int column3 = normalizedIndex(COLUMN_3_VALUES, source.charAt(startOffset + 2));
    int column4 = normalizedIndex(COLUMN_4_VALUES, source.charAt(startOffset + 3));
    int descriptionIndex = ((column1 * COLUMN_2_VALUES.length() + column2) * COLUMN_3_VALUES.length() + column3)
        * COLUMN_4_VALUES.length() + column4;
    return DESCRIPTIONS.get(descriptionIndex);
  }

  private static List<Optional<String>> descriptions() {
    List<Optional<String>> descriptions = new ArrayList<>(DESCRIPTION_COUNT);
    for (int column1 = 0; column1 < COLUMN_1_VALUES.length(); column1++) {
      for (int column2 = 0; column2 < COLUMN_2_VALUES.length(); column2++) {
        for (int column3 = 0; column3 < COLUMN_3_VALUES.length(); column3++) {
          for (int column4 = 0; column4 < COLUMN_4_VALUES.length(); column4++) {
            descriptions.add(Optional.of(new String(new char[] {
                COLUMN_1_VALUES.charAt(column1),
                COLUMN_2_VALUES.charAt(column2),
                COLUMN_3_VALUES.charAt(column3),
                COLUMN_4_VALUES.charAt(column4)
            })));
          }
        }
      }
    }
    return List.copyOf(descriptions);
  }

  private static int normalizedIndex(String allowedValues, char value) {
    return Math.max(allowedValues.indexOf(value), 0);
  }
}

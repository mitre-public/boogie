package org.mitre.tdp.boogie.arinc.v18.field;

import static java.util.Objects.checkFromToIndex;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Waypoint Type” field defines both the “type” and function of IFR waypoints and also define a waypoint as being VFR.
 */
public final class WaypointType implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.42";
  }

  @Override
  public Optional<String> parse(String source, int startOffset, int endOffset) {
    checkFromToIndex(startOffset, endOffset, source.length());
    if (endOffset - startOffset != fieldLength()) {
      return Optional.empty();
    }
    return Optional.of(new String(new char[] {
        inSetOrBlank(source.charAt(startOffset), allowedColumn1),
        inSetOrBlank(source.charAt(startOffset + 1), allowedColumn2),
        inSetOrBlank(source.charAt(startOffset + 2), allowedColumn3)
    }));
  }

  private static char inSetOrBlank(char value, String allowed) {
    return allowed.indexOf(value) >= 0 ? value : ' ';
  }

  private static final String allowedColumn1 = "CINRUVWAMO";

  private static final String allowedColumn2 = "ABCDEFIKLMNOPSUVW";

  private static final String allowedColumn3 = "DEFZ";
}

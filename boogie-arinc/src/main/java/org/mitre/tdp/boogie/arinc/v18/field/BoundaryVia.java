package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableSet;

/**
 * The Boundary VIA defines the path of the boundary from the position identified in the record to the next defined position.
 * <p>
 * The path of the boundary will be determined from official government sources or the rule listed below and the Boundary
 * VIA will be selected from the table below.
 * <p>
 * Definition: Pos1, Pos2
 * C, null - Circle
 * G, null - Great Circle
 * H, null - Rhumb Line
 * L, null - Counter Clockwise ARC
 * R, null - Clockwise ARC
 * null, E - End of description, return to origin point
 * <p>
 * Application Rules:
 * 1. Special Use Airspace designated as following rivers, country, state or other political boundaries will be averaged
 * in coding by using a series of straight lines so that no path will be greater than two miles from the actual boundary.
 * The Boundary VIA will be G.
 * <p>
 * 2. If there is a named waypoint on an airway which crosses an irregular FIR/UIR boundary, the waypoint coordinates
 * will be used to define a point in the path defining that FIR/UIR boundary. The Boundary VIA will be appropriate to the
 * path definition.
 * <p>
 * 3. Paths that follow lines of latitude will be coded with a Boundary Via of H. Paths that follow lines of longitude
 * may be coded with a Boundary Via of G or H. Consistent use of one or the other with a single airspace is desired.
 * <p>
 * 4. Other than for lines of latitude and longitude, the Boundary VIA of H shall only be used when specifically stated
 * in the official government source. If not stated as Rhumb Line or not along latitude/longitude, all straight lines
 * will be coded as G.
 */

public enum BoundaryVia implements FieldSpec<BoundaryVia> {
  SPEC,
  /**
   * Circle
   */
  C,
  /**
   * Great Circle
   */
  G,
  /**
   * Rhumb Line
   */
  H,
  /**
   * Counterclockwise Arc
   */
  L,
  /**
   * Clockwise Arc
   */
  R,
  /**
   * End of description, return to point of origin
   */
  E,
  /**
   * Circle and End of description, return to point of origin
   */
  CE,
  /**
   * Great Circle and End of description, return to point of origin
   */
  GE,
  /**
   * Rhumb Line and End of description, return to point of origin
   */
  HE,
  /**
   * Counterclockwise Arc and End of description, return to point of origin
   */
  LE,
  /**
   * Clockwise Arc and End of description, return to point of origin
   */
  RE;

  private static final Set<String> VALUES = ImmutableSet.of("C", "G", "H", "L", "R", "E", "CE", "GE", "HE", "LE", "RE");

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.118";
  }

  @Override
  public Optional<BoundaryVia> parse(String source, int startOffset, int endOffset) {
    return Optional.of(source.substring(startOffset, endOffset))
        .map(String::trim)
        .filter(VALUES::contains)
        .map(BoundaryVia::valueOf);
  }
}

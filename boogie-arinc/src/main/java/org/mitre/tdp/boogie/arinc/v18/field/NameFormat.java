package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import java.util.Optional;
import java.util.Set;

/**
 * Joint two character code used to describe the format of the fix name.
 * <br>
 * Note - There are actually three of the these in the raw desc but only 1,2 are currently in used with the third saved for
 * future expansion.
 * <br>
 * Note - For specific explanations of the name formatting that goes with these check chapter 7 of the JEP Arinc424 document
 * which goes into gorey detail about all of this.
 * <br>
 * <b>Column1:</b>
 * A - Abeam fix
 * B - Bearing and distance fix
 * D - Airport name as fix
 * F - FIR fix
 * H - Phonetic letter name fix
 * I - Airport name as fix
 * L - Lat/Lon fix
 * M - Multiple word name fix
 * N - Navaid name as fix
 * P - Published 5 letter fix name
 * Q - Published name fix, less than 5 letters
 * R - Published name fix, greater than 5 letters
 * T - Airport/Runway related fix
 * U - UIR fix
 * <br>
 * <b>Column2:</b>
 * O - Localizer marker with officially published 5 letter name
 * M -  Localizer marker without officially published 5 letter name
 */
public final class NameFormat implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.196";
  }

  @Override
  public Optional<String> parse(String source, int startOffset, int endOffset) {
    return Optional.of(source.substring(startOffset, endOffset))
        .filter(s -> s.length() == 3)
        .map(s -> ""
            .concat(inSetOrBlank(s.substring(0, 1), allowedColumn1))
            .concat(inSetOrBlank(s.substring(1, 2), allowedColumn2))
            .concat(inSetOrBlank(s.substring(2, 3), allowedColumn3))
        );
  }

  private String inSetOrBlank(String s, Set<String> set) {
    return set.contains(s) ? s : " ";
  }

  private static final Set<String> allowedColumn1 = Set.of("A", "B", "D", "F", "H", "I", "L", "M", "N", "P", "Q", "R", "T", "U");

  private static final Set<String> allowedColumn2 = Set.of("O", "M");

  private static final Set<String> allowedColumn3 = Set.of();
}

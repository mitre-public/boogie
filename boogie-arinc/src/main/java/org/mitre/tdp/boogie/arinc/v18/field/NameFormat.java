package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

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
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(s -> s.length() == 3)
        .map(s -> new String()
            .concat(inSetOrBlank(s.substring(0, 1), allowedColumn1))
            .concat(inSetOrBlank(s.substring(1, 2), allowedColumn2))
            .concat(inSetOrBlank(s.substring(2, 3), allowedColumn3))
        );
  }

  private String inSetOrBlank(String s, HashSet<String> set) {
    return set.contains(s) ? s : " ";
  }

  private static final HashSet<String> allowedColumn1 = newHashSet("A", "B", "D", "F", "H", "I", "L", "M", "N", "P", "Q", "R", "T", "U");

  private static final HashSet<String> allowedColumn2 = newHashSet("O", "M");

  private static final HashSet<String> allowedColumn3 = newHashSet();
}

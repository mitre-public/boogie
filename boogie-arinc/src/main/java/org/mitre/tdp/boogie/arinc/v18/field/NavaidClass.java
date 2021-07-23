package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Navaid Class” field provides information in coded format on the type of navaid, the use-able range or assigned
 * output power of the navaid, information carried on the navaid signal and collocation of navaids in both an electronic
 * and aeronautical sense. The field is made up of five columns of codes that define this information.
 */
public final class NavaidClass implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.35";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .filter(s -> s.length() == 5)
        .map(s -> ""
            .concat(inSetOrBlank(s.substring(0, 1), allowedType1))
            .concat(inSetOrBlank(s.substring(1, 2), allowedType2))
            .concat(inSetOrBlank(s.substring(2, 3), allowedRangePower))
            .concat(inSetOrBlank(s.substring(3, 4), allowedAdditionalInfo))
            .concat(inSetOrBlank(s.substring(4, 5), allowedCollocation))
        );
  }

  private String inSetOrBlank(String s, HashSet<String> set) {
    return set.contains(s) ? s : " ";
  }

  private static final HashSet<String> allowedType1 = newHashSet("V", "H", "S", "M");

  private static final HashSet<String> allowedType2 = newHashSet("D", "T", "I", "M", "O", "C", "N", "P");

  private static final HashSet<String> allowedRangePower = newHashSet("T", "U", "H", "M", "L", "C", " ");

  private static final HashSet<String> allowedAdditionalInfo = newHashSet("D", "A", "B", "W", " ");

  private static final HashSet<String> allowedCollocation = newHashSet("B", "A", "N", " ");
}

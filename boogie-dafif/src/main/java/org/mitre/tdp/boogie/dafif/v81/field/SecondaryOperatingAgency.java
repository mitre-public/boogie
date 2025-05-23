package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THERE ARE TWO BASIC CATEGORIES OF PRIMARY OPERATING AGENCIES:  MILITARY/FEDERAL  GOVERNMENT AND CIVIL AIRPORTS OPEN TO THE GENERAL PUBLIC.
 *
 * NOTE:  SECONDARY OPERATING AGENCY APPLIES ONLY WHEN THE SECONDARY AIRPORT INDICATES  'Y' (YES).
 *
 * EXAMPLE(S):
 * CI
 * AF
 * ML
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * COMBINATIONS OF LETTERS AA-ZZ
 * OR
 * NULL
 *
 * INTENDED USE:
 * DEFINITION OF CODES ARE IN APPC/APPC_OPR_AGY
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class SecondaryOperatingAgency extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 249;
  }

  @Override
  public String regex() {
    return "((AA|AE|AF|AN|AR|CG|CI|DF|DN|DO|FA|JU|MC|MI|ML|MU|NA|NB|NC|ND|NE|NF|NM|NR|NS|NT|PA|PS|PV|SM|TC|UF|UN|US|XX)?)";
  }
}

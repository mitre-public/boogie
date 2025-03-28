package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * STATE/PROVINCE IS A TWO CHARACTER PROVINCE IDENTIFICATION.
 * THE CURRENT DAFIF OUTPUT REFLECTS PROVINCE CODE VALUES AS REFLECTED IN FIPS PUB 10-4
 *           STATE
 * 01     Alabama
 * 02     Alaska
 * 04     Arizona
 * 05     Arkansas
 * 06     California
 * 08     Colorado
 * 09     Connecticut
 * 10     Delaware
 * 11     District of Columbia
 * 12     Florida
 * 13     Georgia
 * 15     Hawaii
 * 16     Idaho
 * 17     Illinois
 * 18     Indiana
 * 19     Iowa
 * 20     Kansas
 * 21     Kentucky
 * 22     Louisiana
 * 23     Maine
 * 24     Maryland
 * 25     Massachusetts
 * 26     Michigan
 * 27     Minnesota
 * 28     Mississippi
 * 29     Missouri
 * 30     Montana
 * 31     Nebraska
 * 32     Nevada
 * 33     New Hampshire
 * 34     New Jersey
 * 35     New Mexico
 * 36     New York
 * 37     North Carolina
 * 38     North Dakota
 * 39     Ohio
 * 40     Oklahoma
 * 41     Oregon
 * 42     Pennsylvania
 * 44     Rhode Island
 * 45     South Carolina
 * 46     South Dakota
 * 47     Tennessee
 * 48     Texas
 * 49     Utah
 * 50     Vermont
 * 51     Virginia
 * 53     Washington
 * 54     West Virginia
 * 55     Wisconsin
 * 56     Wyoming
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 *  01 - 56
 * 	OR
 * 	NULL
 *
 * SOURCE: TRANSLATE/FORMAT FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * STATE/PROVINCE CODE IS A KEY FIELD FOR THE ATS_CTRY TABLE ONLY.
 * NGA CAPTURES PROVINCE CODES FOR CANADA AND MEXICO, BUT THEY DO NOT GET POPULATED IN DAFIF.
 */
public final class StateProvinceCode extends DafifInteger {

  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 300;
  }

  @Override
  public String regex() {
    return "((0[1-2]|0[4-6]|0[8-9]|1[0-3]|1[5-9]|[2-3][0-9]|4[0-2]|4[4-9]|5[0-1]|5[3-6])?)";
  }

}

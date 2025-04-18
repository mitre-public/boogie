package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE FIRST TWO LETTERS OF THE INTERNATIONAL CIVIL AVIATION ORGANIZATION (ICAO) LOCATION  IDENTIFIERS.
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * COMBINATIONS OF LETTERS AA-ZZ
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * 1.  THE NAV TABLE USES THE COLUMN HEADER 'ICAO' FOR THE ICAO REGION CODE.
 *
 * 2.  THE TABLES PJA, PJA_PAR, PR_PAR, MTR, AND MTR_PAR ALL USE THE COLUMN HEADER ICAO_REGION FOR THE  ICAO REGION CODE.
 */
public final class IcaoRegion extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 148;
  }

  @Override
  public String regex() {
    return "(A[GNUY]|B[GIK]|C[FUWYZ]|D[ABFGINRTX]|E[BDEFGHIKLNPSTVY]|F[ABCDEGHIJKLMNOPQSTVWXYZ]|G[ABCEFGLMOQSUV]|H[ABCDEFHJKLRSTU]|K[ABCDEFGHIJKLMNOPRSTUVWXYZ]|L[ABCDEFGHIJKLMNOPQRSTUVWXYZ]|M[BDGHKMNPRSTUWYZ]|N[CFGILSTVWZ]|O[ABDEIJKLMOPRSTY]|P[ABCFGHJKLMOPTW]|R[CJKOPU]|S[ABCEFGKLMNOPQSUVY]|T[ABDFGIJKLNQRTUVX]|U[ABCDEGHIKLMNORSTUW]|V[ABCDEGHILMNOQRTVY]|W[ABIMPRS]|Y[ABCDEFGHIJKLMNOPQRSTUVWY]|Z[BGHIJKLMPSUWY])";
  }
}
package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A UNIQUE SET OF NUMBERS FOLLOWING THE APPROPRIATE COUNTRY CODE WHICH ENABLES AN INDIVIDUAL AIRPORT TO BE UNIQUELY IDENTIFIED WITHIN THE DATABASE. (DAFIF)
 * NOTE: THE FIRST TWO CHARACTERS OF THE IDENTIFICATION ARE THE COUNTRY CODE.
 *
 * EXAMPLE(S):
 * BF00001
 * GM00322
 * JA99924
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * FIRST TWO POSITIONS: COMBINATIONS OF LETTERS A-Z
 * SUBSEQUENT POSITIONS: COMBINATIONS OF NUMBERS 0-9
 *
 * SOURCE: SYSTEM GENERATED
 *
 * INTENDED USE:
 * WHEN DETERMINING THE PEDIGREE OF A TERMINAL PROCEDURE IN DAFIF, USE THE ARPT_IDENT TO IDENTIFY THE COUNTRY THE PROCEDURE CAME FROM, THE OPR FIELD TO IDENTIFY WHO IS RESPONSIBLE FOR CODING THE PROCEDURE, AND THE HOST_CTRY_AUTH TO DETERMINE IF THE AIP IS CIVIL OR MILITARY.
 *
 * EXAMPLE 1:
 * ARPT_IDENT = NO91674: THE PROCEDURE WAS DEVELOPED IN NORWAY
 * OPR = AIDU: THE PROCEDURE WAS CODED BY THE UNITED KINGDOM (AIDU)
 * HOST_CTRY_AUTH = CIV: THE PROCEDURE CAME OUT OF THE CIVIL AIP
 *
 * EXAMPLE 2:
 * ARPT_IDENT = CA25699: THE PROCEDURE WAS DEVELOPED IN CANADA
 * OPR = DNGA: THE PROCEDURE WAS CODED BY NGA
 * HOST_CTRY_AUTH = MIL: THE PROCEDURE CAME OUT OF THE MILITARY AIP
 *
 * EXAMPLE 3:
 * ARPT_IDENT = FR00699: THE PROCEDURE WAS DEVELOPED IN FRANCE
 * OPR = FR: THE PROCEDURE WAS CODED BY THE FRENCH AVIATION AUTHORITY
 * HOST_CTRY_AUTH = CIV: THE PROCEDURE CAME OUT OF THE CIVIL AIP
 */
public final class AirportIdentification extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 12;
  }

  @Override
  public String regex() {
    return "(((A[ACEFGJLMNOQRSTUVY]|B[ABCDEFGHKLMNOPQRSTUVXY]|C[ABDEFGHIJKMNOQRSTUVWY]|D[AJOQR]|E[CGIKNRSTUZ]|F[GIJKMOPQRS]|G[ABGHIJKLMOPQRTVYZ]|H[AKMOQRU]|I[CDMNOPRSTVZ]|J[AEMNOQU]|K[EGNQRSTUVZ]|L[AEGHIOQSTUY]|M[ABCDFGHIJKLNOPQRTUVXYZ]|N[CEFGHILNOPRSUZ]|OD|P[ACEFGKLMOPSU]|QA|R[EIMNOPQSW]|S[ABCEFGHILMNOPTUVWYZ]|T[BDEHIKLNOPSTUVWXZ]|U[CGKPSVYZ]|V[CEIMQT]|W[AEFIQSZ]|YM|ZA|ZI)([0-9]{5}))?)";
  }
}
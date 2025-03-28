package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DESIGNATION OF AN AIR TRAFFIC SERVICE (ATS) ROUTE BETWEEN TWO WAYPOINTS. CONTAINS THE NAME OR SERIES OF CHARACTERS WITH WHICH THE ATS ROUTE IS IDENTIFIED.
 *
 * NOTES:
 * A. USE THE FULL IDENT IF SIX OR LESS CHARACTERS ARE INVOLVED.
 *
 * B. IF THE IDENT IS MORE THAN SIX CHARACTERS, IDENTS SHOULD BE DEVELOPED USING THE FOLLOWING RULES SEQUENTIALLY UNTIL SIX CHARACTER IDENTS EMERGE.
 * 1. ONE-WORD IDENTS
 * a. ELIMINATE DOUBLE LETTERS STARTING FROM RIGHT TO LEFT.
 * EXAMPLES:  RABBITT BECOMES RABBIT
 *                          BURWELL BECOMES BURWEL
 * b. KEEP THE FIRST LETTER FIRST VOWEL AND LAST LETTER. DROP OTHER VOWELS STARTING FROM RIGHT TO LEFT.
 * EXAMPLES:  BOHNERT BECOMES BOHNRT
 *                          THACKER BECOMES THACKR
 *                          ACKERMANN BECOMES ACKRMN
 * c. DROP CONSONANTS, STARTING FROM RIGHT TO LEFT.
 * EXAMPLES:  ANDREWS BECOMES ANDRWS
 *                          BRIDGEPORT BECOMES BRIDGT
 *                          BLACKWELL BECOMES BLACWELL
 *
 * 2. MULTIPLE WORD IDENTS
 * USE THE FIRST LETTER OF THE FIRST IDENT AND ABBREVIATE THE LAST WORD USING THE ABOVE RULES.  SEQUENCE ONE-WORD IDENTS TO FIVE CHARACTERS.
 * EXAMPLES:  CLEAR LAKE BECOMES CLAKE
 *                          ROUGH AND READY BECOMES READY
 * 3. ELIMINATE WORDS OR ABBREVIATIONS SUCH AS "CORR", "ROUTE", AND "TRACK" IF THEY ARE A PART OF THE IDENT AND USE THE ABOVE RULES.
 * EXAMPLES:  MAYA CORR BECOMES MAYA
 *                          CORRIDOR PURTO CABEZAS BECOMES PCABZS
 *                          SOUTH CORRIDOR BECOMES SOUTH
 *                          HAMBURG-BERLIN CORRIDOR BECOMES HBERLN
 *                          NORTH ROUTE 1 BECOMES NORTH1
 *                          CENTER ROUTE 2 BECOMES CENTR2
 *                          BUCKEBURG-BERLIN CORRIDOR BECOMES BBERLN
 *                          CENTER ROUTE 1 BECOMES CENTR1
 *                          CENTER ROUTE 2 BECOMES CENTR2
 *                          FRANKFURT-BERLIN CORRIDOR BECOMES FBERLN
 *                          SOUTH ROUTE 1 BECOMES SOUTH1
 *                          SOUTH ROUTE 2 BECOMES SOUTH2
 *                          TRACK HOLLY BECOMES HOLLY
 * C. UNIDENTIFIED ATS ROUTES:
 * FOR THE FOLLOWING TYPES, IDENTIFIERS MUST HAVE A SUBSTRING:
 * NAME - TYPE -I D ENTIFIER SUBSTRING
 * ADVISORY - D - D
 * BAHAMAN - B - BR
 * CORRIDOR - C - COR
 * DIRECT - E - E
 * ATLANTIC - A - AR
 * MILITARY - M - MIL
 * OCEANIC - O - OC
 * AIRWAY - W - AW
 * RNAV - R - RNV
 * SUBSTITUTE - S - S
 * TACAN - T - T
 *
 * THE FOLLOWING PROCEDURES SHALL APPLY:
 * 1. USE SUBSTRING AND NUMBER TO GENERATE AN IDENT WITH A NUMERIC AREA BREAKDOWN. THE FIRST DIGIT IN THE IDENT IDENTIFIES EACH AREA:
 * AFRICA - 1
 * C&amp;SA - 2
 * PAA - 3
 * ENAME - 4
 * U.S. &amp; CANADA (INCLUDES ALASKA) - 5
 * EE&amp;A - 6
 *
 * 2. THE SUBSTRING SHALL BE USED TO SEPARATE THE AREA IDENTIFICATION FROM THE ROUTE NUMBER (ROUTE NUMBER IS TO BE SELECTED FROM THE APPROPRIATE LOG BOOK).
 * 1D199 - 199TH UNIDENTIFIED ADVISORY ROUTE IN AFRICA
 * 2BR1 - 1ST UNIDENTIFIED BAHAMA ROUTE IN C&amp;SA
 * 4COR1 - 1ST UNIDENTIFIED CORRIDOR IN ENAME
 * 3E9999 - 9,999TH UNIDENTIFIED DIRECT ROUTE IN PAA
 * 5AR1 - 1ST UNIDENTIFIED ATLANTIC ROUTE IN U.S. &amp; CANADA
 * 3MIL13 - 13TH UNIDENTIFIED MILITARY ROUTE IN PAA
 * 20C203 - 203RD UNIDENTIFIED OCEANIC ROUTE IN C&amp;SA
 * 4AW123 - 123RD UNIDENTIFIED AIRWAY IN ENAME
 * 5RNV99 - 99TH UNIDENTIFIED RNAV ROUTE IN U.S. &amp; CANADA
 * 5S11 - 11TH UNIDENTIFIED SUBSTITUTE ROUTE IN U.S. &amp; CANADA
 * 5T154 - 154TH UNIDENTIFIED TACAN R0UTE IN U.S. &amp; CANADA
 *
 * 3. CONTROL AREA ROUTES IDENTIFIERS WILL BE THE LETTER "C" AND THE CONTROL AREA NUMBER, EXAMPLE:
 * C1418. MULTIPLES ROUTES IN AN AREA WILL BE IDENTIFIED WITH ALPHA SUFFIX, EXAMPLE: C1186A, C1186B, ETC.
 *
 * 4. CANADIAN CONTROL AREA TRACKS MAY BE IDENTIFIED USING THE FOLLOWING EXAMPLES:
 * NORTHERN CONTROL AREA TRACKS (NCA):
 * NCA 11= NCA11
 * NCA ALPHA= NCAA
 * NORTHERN CONTROL LATERAL TRACKS (NCA):
 * NCA LATERAL 1 = NCAL1
 * NCA LATERAL 5 = NCAL5
 * ARCTIC CONTROL AREA TRACKS (ACA):
 * ACA OSCAR = ACAO
 * ACA PAPA= ACAP
 * SOUTHERN CONTROL AREA TRACKS (SCA):
 * SCA GULF= SCAG
 * SCA HOTAL = SCAH
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * COMBINATIONS OF NUMBERS 0-9 AND LETTERS A-Z UP TO SIX CHARACTERS IN LENGTH
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class AtsIdentifier extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 6;
  }

  @Override
  public int fieldCode() {
    return 36;
  }

  @Override
  public String regex() {
    return "(([0-9A-Z]){1,6})";
  }
}
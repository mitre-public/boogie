package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A ONE CHARACTER ALPHA CODE INDICATING AN AIRPORT'S OPERATIONS/FACILITIES IN ABBREVIATED  FORM.
 *
 * A - ACTIVE CIVIL AIRPORTS CONTROLLED AND OPERATED BY CIVIL AUTHORITIES PRIMARILY FOR USE BY  CIVIL AIRCRAFT,
 * ALTHOUGH THE MILITARY MAY HAVE LANDING PRIVILEGES AND/OR CONTRACT RIGHTS.
 * MINIMUM FACILITIES ARE AVAILABLE TO INCLUDE:
 *
 *    1) CONTROL TOWER OR SOME SIMILAR CONTROL  SYSTEM SUCH AS A FLIGHT SERVICE STATION (FSS) WHICH ISSUES CLEARANCES AND
 *    ADVISORIES WHEN  THERE IS NO TOWER OR THE TOWER IS NOT IN OPERATION. THE FSS CAN ALSO HAVE A REMOTE  COMMUNICATION
 *    OUTLET (RCO) OR CAN BE COLLOCATED WITH A UNICOM (AERONAUTICAL ADVISORY  STATION).  THE COMMON TRAFFIC ADVISORY FREQUENCY
 *    (CTAF) ADVISES ON KNOWN TRAFFIC AROUND  AIRPORTS WITH NO TOWER;
 *
 *    2) PERMANENT OR TEMPORARY LIGHTING (MAY BE FLARE POTS, ETC.);
 *
 *    3)  POL; AND
 *
 *    4) FACILITIES FOR ORGANIZATIONAL MAINTENANCE OR BETTER.  (AAFIF)
 *
 * B - ACTIVE JOINT (CIVIL AND MILITARY).  AIRPORTS JOINTLY CONTROLLED, USED AND/OR OPERATED BY  BOTH CIVIL AND MILITARY AGENCIES.
 * THE MILITARY AGENCIES MUST BE PERMANENT OPERATIONAL   FLIGHT LINE TYPE TENANTS, WITH OR WITHOUT AIRCRAFT STATIONED AT THE AIRPORT.
 * FACILITIES ARE  THE SAME AS FOR CIVIL (A) AIRPORTS.  (AAFIF)
 *
 * C - ACTIVE MILITARY AIRPORTS CONTROLLED AND OPERATED BY MILITARY AUTHORITIES PRIMARILY FOR  USE BY MILITARY AIRCRAFT,
 * ALTHOUGH CIVIL AIRCRAFT MAY HAVE LANDING PRIVILEGES AND/OR   CONTRACTS RIGHTS.  FACILITIES ARE THE SAME AS FOR CIVIL (A) AIRPORTS.  (AAFIF)
 *
 * D - ACTIVE AIRPORTS HAVING PERMANENT TYPE SURFACE RUNWAYS WITH LESS THAN THE MINIMUM  FACILITIES REQUIRED FOR A, B, OR C AIRPORTS
 * ABOVE.  AIRPORTS UNDER CONSTRUCTION WITH NO   RUNWAY YET USABLE ARE INCLUDED IN THIS CATEGORY.  FOR THE UNITED STATES, THE FAA TERM
 * "INACTIVE" WILL BE INCORPORATED INTO THIS CATEGORY.  (AAFIF)
 *
 * FIELD TYPE: A
 *
 * ALLOWABLE VALUES:
 * A, B, C, D
 */
public final class AirportType extends TrimmableString {

  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 13;
  }

  @Override
  public String regex() {
    return "(A|B|C|D)";
  }

}

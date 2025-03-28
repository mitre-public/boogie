package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * The “Sequence Number” field defines the location of the record in the sequence defining the route of flight
 * identified in the route identifier field.
 * <br>
 * For Boundary Type Records - A boundary is defined by a series of records taken in order. The “Sequence Number” field defines
 * the location of the record in the sequence defining a boundary.
 * <br>
 * For Record Types requiring more than one primary record to define the complete content – In a series of records used to define
 * a complete condition, the “Sequence Number” is used to define each primary record in the sequence.
 * <br>
 * Sequence numbers are assigned during the route, boundary or sequence definition phase of the data file assembly.
 * <br>
 * Sequence numbers are assigned so as not be duplicated within the route, boundary or sequence assigned a unique identification
 * or designation.
 * <br>
 * For three or four digit Sequence Numbers, initially, an increment of ten should be maintained between the sequence numbers
 * assigned to consecutive records.
 * <br>
 * For one or two digit Sequence Numbers, the initial increment is one.
 * <br>
 * In route or boundary records, should subsequent maintenance of the file necessitate the addition of a record or records, the
 * new record(s) should be located in the correct position in the sequence and assigned a sequence number whose most significant
 * characters are identical to those in the sequence number of the preceding record in sequence.
 * <br>
 * When an enroute airway crosses the boundary separating two geographical areas (Section 5.3), the airway fix lying on or closest
 * to the boundary shall be coded twice, once for each geographical area, and should be assigned the same sequence number in each
 * case.
 * <br>
 * Enroute airway record sequence numbers should be assigned in a manner which permits them to be arranged into continuous airway
 * routes in flight sequence order when sorted according to the Route Identifier and Sequence Number only, without regard to their
 * applicable Geographical Area Code.
 * <br>
 * 4 characters - Enroute Airways, Preferred Routes, FIR/UIR and Restrictive Airspace.
 * <br>
 * 3 characters - SID/STAR/Approach and Company Routes.
 * <br>
 * Examples - 0010, 0135, 2076, 120, 030, 01, 84, 3.
 */
public final class SequenceNumber extends ArincInteger {

  private final int characters;

  public SequenceNumber(int characters) {
    this.characters = characters;
  }

  @Override
  public int fieldLength() {
    return characters;
  }

  @Override
  public String fieldCode() {
    return "5.12";
  }
}

package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The “Sequence Number” field defines the location of the record in the sequence defining the route of flight
 * identified in the route identifier field. For Boundary Type Records - A boundary is defined by a series of
 * records taken in order. The “Sequence Number” field defines the location of the record in the sequence defining
 * a boundary. For Record Types requiring more than one primary record to define the complete content – In a
 * series of records used to define a complete condition, the “Sequence Number” is used to define each primary
 * record in the sequence.
 */
public class SequenceNumber implements NumericInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.12";
  }
}

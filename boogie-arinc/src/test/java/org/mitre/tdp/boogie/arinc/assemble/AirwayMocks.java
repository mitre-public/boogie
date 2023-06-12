package org.mitre.tdp.boogie.arinc.assemble;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public class AirwayMocks {
  public static ArincAirwayLeg leg1() {
    ArincAirwayLeg leg = mock(ArincAirwayLeg.class);
    when(leg.customerAreaCode()).thenReturn(CustomerAreaCode.CAN);
    when(leg.routeIdentifier()).thenReturn("Q802");
    when(leg.sequenceNumber()).thenReturn(1);
    when(leg.continuationRecordNumber()).thenReturn("0");
    when(leg.fixIdentifier()).thenReturn("first");
    return leg;
  }

  public static ArincAirwayLeg leg2() {
    ArincAirwayLeg leg = mock(ArincAirwayLeg.class);
    when(leg.customerAreaCode()).thenReturn(CustomerAreaCode.CAN);
    when(leg.routeIdentifier()).thenReturn("Q802");
    when(leg.sequenceNumber()).thenReturn(2);
    when(leg.continuationRecordNumber()).thenReturn("0");
    when(leg.fixIdentifier()).thenReturn("second");
    return leg;
  }

  public static ArincAirwayLeg leg3() {
    ArincAirwayLeg leg = mock(ArincAirwayLeg.class);
    when(leg.customerAreaCode()).thenReturn(CustomerAreaCode.USA);
    when(leg.routeIdentifier()).thenReturn("Q802");
    when(leg.sequenceNumber()).thenReturn(1);
    when(leg.continuationRecordNumber()).thenReturn("0");
    when(leg.fixIdentifier()).thenReturn("fifth");
    return leg;
  }

  public static ArincAirwayLeg leg4() {
    ArincAirwayLeg leg = mock(ArincAirwayLeg.class);
    when(leg.customerAreaCode()).thenReturn(CustomerAreaCode.USA);
    when(leg.routeIdentifier()).thenReturn("Q802");
    when(leg.sequenceNumber()).thenReturn(2);
    when(leg.continuationRecordNumber()).thenReturn("0");
    when(leg.fixIdentifier()).thenReturn("sixth");
    return leg;
  }

  public static ArincAirwayLeg leg5() {
    ArincAirwayLeg leg = mock(ArincAirwayLeg.class);
    when(leg.customerAreaCode()).thenReturn(CustomerAreaCode.USA);
    when(leg.routeIdentifier()).thenReturn("Q802");
    when(leg.sequenceNumber()).thenReturn(10);
    when(leg.continuationRecordNumber()).thenReturn("0");
    when(leg.fixIdentifier()).thenReturn("seventh");
    return leg;
  }

  public static ArincAirwayLeg leg6() {
    ArincAirwayLeg leg = mock(ArincAirwayLeg.class);
    when(leg.customerAreaCode()).thenReturn(CustomerAreaCode.USA);
    when(leg.routeIdentifier()).thenReturn("J2");
    when(leg.sequenceNumber()).thenReturn(2);
    when(leg.continuationRecordNumber()).thenReturn("0");
    when(leg.fixIdentifier()).thenReturn("third");
    return leg;
  }

  public static ArincAirwayLeg leg7() {
    ArincAirwayLeg leg = mock(ArincAirwayLeg.class);
    when(leg.customerAreaCode()).thenReturn(CustomerAreaCode.USA);
    when(leg.routeIdentifier()).thenReturn("J2");
    when(leg.sequenceNumber()).thenReturn(10);
    when(leg.continuationRecordNumber()).thenReturn(null);
    when(leg.fixIdentifier()).thenReturn("fourth");
    return leg;
  }


  public static Collection<ArincAirwayLeg> legs() {
    ArincAirwayLeg one = leg1();
    ArincAirwayLeg two = leg2();
    ArincAirwayLeg three = leg3();
    ArincAirwayLeg four = leg4();
    ArincAirwayLeg five = leg5();
    ArincAirwayLeg six = leg6();
    ArincAirwayLeg seven = leg7();
    return Set.of(five, two, three, six, seven,four, one);
  }
}

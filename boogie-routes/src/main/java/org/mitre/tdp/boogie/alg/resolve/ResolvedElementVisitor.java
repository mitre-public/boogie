package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;

import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;

/**
 * Visitor interface such that different {@link ResolvedElement}s can visit each-other to determine how they should be linked.
 * <br>
 * The call hierarchy in visitors can get complex and the {@link LinkedLegs} need to be directional - so to call it out explicitly,
 * MY implementation of the visit method will only be called as the result of a call to a different ResolvedElement's:
 * <br>
 * {@link ResolvedElement#linksTo(ResolvedElementVisitor)}
 * <br>
 * These methods are used in the {@link GraphBasedRouteChooser} - and the call there is:
 * <br>
 * previousElements.linksTo(nextElements)
 * <br>
 * Taking the above into account my visit(ResolvedElementSubClass...) will only be called when I am the <i>target</i> of a linking
 * attempt - and therefore the returned LinkedLegs should have my legs as the target and the incoming elements legs as the source.
 */
public interface ResolvedElementVisitor {

  List<LinkedLegs> visit(AirportElement airportElement);

  List<LinkedLegs> visit(AirwayElement airwayElement);

  List<LinkedLegs> visit(FixElement fixElement);

  List<LinkedLegs> visit(SidElement sidElement);

  List<LinkedLegs> visit(StarElement starElement);

  List<LinkedLegs> visit(ApproachElement approachElement);

  List<LinkedLegs> visit(TailoredElement tailoredElement);

  List<LinkedLegs> visit(LatLonElement latLonElement);

  default List<LinkedLegs> visit(DirectToFixElement fixElement){
    return List.of();
  }

  default List<LinkedLegs> visit(InitialFixElement fixElement){
    return List.of();
  }
}

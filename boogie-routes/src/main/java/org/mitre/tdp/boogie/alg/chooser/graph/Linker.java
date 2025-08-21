package org.mitre.tdp.boogie.alg.chooser.graph;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.alg.chooser.graph.LinkingSupport.distanceBetween;
import static org.mitre.tdp.boogie.alg.chooser.graph.LinkingSupport.firstLegWithLocation;
import static org.mitre.tdp.boogie.alg.chooser.graph.LinkingSupport.highlander;
import static org.mitre.tdp.boogie.alg.chooser.graph.LinkingSupport.lastLegWithLocation;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

/**
 * A linker is able to (on demand) return a collection of links (presumed to be between pairs of {@link ResolvedToken}s).
 */
@FunctionalInterface
public interface Linker {

  static Linker noop() {
    return Collections::emptyList;
  }

  /**
   * A linker which wraps an existing {@link Linker} and applies the provided tweaking function to the scores of all its generated
   * links.
   *
   * @param linker  the linker to decorate
   * @param tweaker the function to apply to the original link score to generate the new one
   */
  static Linker tweak(Linker linker, DoubleUnaryOperator tweaker) {
    return new Tweaker(linker, tweaker);
  }

  /**
   * A linker which returns {@link LinkedLegs} between all leg pairs of the left and right {@link LinkableToken}s where the fixes
   * associated with the legs are within the given range.
   *
   * @param range the max {@link Distance} between legs to link from the two tokens
   * @param left  the token to link from
   * @param right the token to link to
   */
  static Linker pointsWithinRange(Distance range, LinkableToken left, LinkableToken right) {
    checkArgument(range.isPositive(), "Range should be positive.");
    return new PointsWithinRange(range, left, right);
  }

  /**
   * A linker which returns a single {@link LinkedLegs} taking the closes point between the legs of the {@link LinkableToken}s.
   *
   * @param left  the token to link from
   * @param right the token to link to
   */
  static Linker closestPointBetween(LinkableToken left, LinkableToken right) {
    return () -> pointsWithinRange(Distance.ofNauticalMiles(Double.MAX_VALUE), left, right)
        .links().stream().findFirst().map(List::of).orElseGet(Collections::emptyList);
  }

  /**
   * A linker which builds connections between any airport and any SID. This linker is a special case prioritizing connecting the
   * airport to the ends of the {@code runway > common > enroute} transition portions of the SID.
   *
   * <p>Without this linker down-selecting to the ends of these transitions, depending on the geometry of the SID, a linker like
   * the {@link #closestPointBetween(LinkableToken, LinkableToken)} may link the airport to the middle of a runway transition.
   *
   * <p>This is less common for SIDs than it is for STARs where runway transitions often have long downwinds that run parallel to
   * the runways and pass by the airport somewhere in the middle.
   */
  static Linker airportToSid(AnyAirport airport, AnySid sid) {
    return new AirportToSid(airport, sid);
  }

  /**
   * A linker which builds connections between any STAR and any airport. This linker is a special case prioritizing connecting the
   * airport to the ends of the {@code runway > common > enroute} transition portions of the STAR.
   *
   * <p>Without this linker down-selecting to the ends of these transitions, depending on the geometry of the STAR, specifically
   * the runway transitions with long downwinds, using the {@link #closestPointBetween(LinkableToken, LinkableToken)} linker may
   * connect the airport to the middle of one of the runway transitions.
   */
  static Linker starToAirport(AnyStar star, AnyAirport airport) {
    return new StarToAirport(star, airport);
  }

  /**
   * A linker which builds connections between any generic piece of infrastructure and an approach procedure. This linker is a
   * special case prioritizing linking to the start of the "initial" transitions in the approach procedure.
   *
   * <p>Similar to the {@link #airportToSid(AnyAirport, AnySid)} and {@link #starToAirport(AnyStar, AnyAirport)} linkers may be
   * vulnerable to certain geometries of approaches and linking infrastructure without this specialization.
   */
  static Linker anyToApproach(LinkableToken any, AnyApproach approach) {
    return new AnyToApproach(any, approach);
  }

  /**
   * A linker which builds connections between the terminal legs of a SID and the initial legs of an approach procedure.
   */
  static Linker sidToApproach(AnySid sid, AnyApproach approach) {
    return new SidToApproach(sid, approach);
  }

  /**
   * Might need to just link the star to the approach.
   * @param star our star
   * @param approach our approach
   * @return the links
   */
  static Linker starToApproach(AnyStar star, AnyApproach approach) {
    return new StarToApproach(star, approach);
  }

  /**
   * A linker that will link a sid token with fm/vm to anything with a point.
   * @param left the sid
   * @param right anything with apoint
   * @return link the sid
   */
  static Linker sidXm(AnySid left, LinkableToken right) {
    return new SidXm(left, right);
  }

  /**
   * Returns a collection of links between a pair of configured {@link LinkableToken}s.
   */
  Collection<LinkedLegs> links();

  /**
   * Returns a new {@link Linker} implementation returning the legs generated by itself (or if none were present) the collection
   * of legs generated by the provided other linker.
   */
  default Linker orElseTry(Linker other) {
    Linker me = this;
    return () -> {
      Collection<LinkedLegs> legs = me.links();
      return legs.isEmpty() ? other.links() : legs;
    };
  }

  final class Tweaker implements Linker {

    private final Linker linker;

    private final DoubleUnaryOperator tweaker;

    private Tweaker(Linker linker, DoubleUnaryOperator tweaker) {
      this.linker = requireNonNull(linker);
      this.tweaker = requireNonNull(tweaker);
    }

    @Override
    public Collection<LinkedLegs> links() {
      return linker.links().stream()
          .map(linked -> new LinkedLegs(linked.source(), linked.target(), tweaker.applyAsDouble(linked.linkWeight())))
          .toList();
    }
  }

  final class PointsWithinRange implements Linker {

    private final Distance range;

    private final LinkableToken left;

    private final LinkableToken right;

    private PointsWithinRange(Distance range, LinkableToken left, LinkableToken right) {
      this.range = requireNonNull(range);
      this.left = requireNonNull(left);
      this.right = requireNonNull(right);
    }

    @Override
    public List<LinkedLegs> links() {

      List<Leg> element1Legs = withLocation(left.graphRepresentation());
      List<Leg> element2Legs = withLocation(right.graphRepresentation());

      return cartesianProduct(element1Legs, element2Legs).stream()
          .sorted(comparing(this::distanceBetween))
          .map(this::createPair)
          .filter(pair -> pair.linkWeight() < range.inNauticalMiles())
          .toList();
    }

    private double distanceBetween(Pair<Leg, Leg> pair) {

      Fix left = pair.first().associatedFix().orElseThrow(IllegalStateException::new);
      Fix right = pair.second().associatedFix().orElseThrow(IllegalStateException::new);

      return left.distanceInNmTo(right);
    }

    private LinkedLegs createPair(Pair<Leg, Leg> pair) {
      return new LinkedLegs(
          pair.first(),
          pair.second(),
          Math.max(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT, distanceBetween(pair))
      );
    }

    private List<Leg> withLocation(Collection<LinkedLegs> linkedLegs) {
      return linkedLegs.stream()
          .flatMap(linked -> Stream.of(linked.source(), linked.target()))
          .distinct()
          .filter(leg -> leg.associatedFix().isPresent())
          .toList();
    }
  }

  final class AirportToSid implements Linker {

    private final AnyAirport airport;

    private final AnySid sid;

    private AirportToSid(AnyAirport airport, AnySid sid) {
      this.airport = requireNonNull(airport);
      this.sid = requireNonNull(sid);
    }

    @Override
    public Collection<LinkedLegs> links() {

      LinkedLegs airportLegs = highlander(airport.graphRepresentation()).orElseThrow();

      return sid.sid().initialTransitions().stream()
          .map(transition -> new LinkedLegs(airportLegs.target(), transition.legs().get(0), matchWeightFor(airportLegs.target(), transition)))
          .toList();
    }

    private double matchWeightFor(Leg airportLeg, Transition transition) {

      Optional<Leg> locationLeg = firstLegWithLocation(transition);

      double distanceScore = locationLeg.flatMap(Leg::associatedFix)
          .map(fix -> airportLeg.associatedFix().orElseThrow(IllegalStateException::new).distanceInNmTo(fix))
          .orElse(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT);

      return (distanceScore * transitionTypeModifier(transition.transitionType()))
          / locationLeg.map(transition.legs()::indexOf).map(i -> i + 1.).orElse(1.);
    }

    private double transitionTypeModifier(TransitionType transitionType) {
      return TransitionType.RUNWAY.equals(transitionType) ? .01 : 1.;
    }
  }

  final class StarToAirport implements Linker {

    private final AnyStar star;

    private final AnyAirport airport;

    private StarToAirport(AnyStar star, AnyAirport airport) {
      this.star = requireNonNull(star);
      this.airport = requireNonNull(airport);
    }

    @Override
    public Collection<LinkedLegs> links() {

      LinkedLegs airportLegs = highlander(airport.graphRepresentation()).orElseThrow();

      return star.star().finalTransitions().stream()
          .map(transition -> new LinkedLegs(transition.legs().get(transition.legs().size() - 1), airportLegs.source(), matchWeightFor(airportLegs.source(), transition)))
          .toList();
    }

    private double matchWeightFor(Leg airportLeg, Transition transition) {
      Optional<Leg> locationLeg = lastLegWithLocation(transition);

      double distanceScore = locationLeg.flatMap(Leg::associatedFix)
          .map(fix -> airportLeg.associatedFix().orElseThrow(IllegalStateException::new).distanceInNmTo(fix))
          .orElse(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT);

      // longer transitions are better - this should have minimal effect
      return (distanceScore * transitionTypeModifier(transition.transitionType()))
          / locationLeg.map(transition.legs()::indexOf).map(i -> i + 1.).orElse(1.);
    }

    private double transitionTypeModifier(TransitionType transitionType) {
      return TransitionType.RUNWAY.equals(transitionType) ? .01 : 1.;
    }
  }

  final class AnyToApproach implements Linker {

    private final LinkableToken any;

    private final AnyApproach approach;

    private AnyToApproach(LinkableToken any, AnyApproach approach) {
      this.any = requireNonNull(any);
      this.approach = requireNonNull(approach);
    }

    @Override
    public Collection<LinkedLegs> links() {

      List<Leg> anyLegs = withLocation(any.graphRepresentation());

      List<Leg> approachEntryLegs = approach.approach().entryLegs(leg -> leg.associatedFix().isPresent());

      return cartesianProduct(anyLegs, approachEntryLegs).stream()
          .min(Comparator.comparing(LinkingSupport::distanceBetween))
          .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
          .filter(pair -> pair.linkWeight() < Double.MAX_VALUE)
          .map(List::of)
          .orElse(Collections.emptyList());
    }

    private static List<Leg> withLocation(Collection<LinkedLegs> linkedLegs) {
      return linkedLegs.stream()
          .flatMap(linked -> Stream.of(linked.source(), linked.target()))
          .distinct()
          .filter(leg -> leg.associatedFix().isPresent())
          .toList();
    }
  }

  final class SidToApproach implements Linker {

    private final AnySid sid;

    private final AnyApproach approach;

    private SidToApproach(AnySid sid, AnyApproach approach) {
      this.sid = requireNonNull(sid);
      this.approach = requireNonNull(approach);
    }

    @Override
    public Collection<LinkedLegs> links() {

      Predicate<Leg> hasPosition = leg -> leg.associatedFix().isPresent();

      return cartesianProduct(sid.sid().exitLegs(hasPosition), approach.approach().entryLegs(hasPosition)).stream()
          .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
          .toList();
    }
  }

  final class StarToApproach implements Linker {

    private final AnyStar star;

    private final AnyApproach approach;

    private StarToApproach(AnyStar star, AnyApproach approach) {
      this.star = requireNonNull(star);
      this.approach = requireNonNull(approach);
    }

    @Override
    public Collection<LinkedLegs> links() {

      Predicate<Leg> hasPosition = leg -> leg.associatedFix().isPresent();

      return cartesianProduct(star.star().exitLegs(hasPosition), approach.approach().entryLegs(hasPosition)).stream()
          .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceBetween(pair)))
          .toList();
    }
  }

  final class SidXm implements Linker {
    private final AnySid left;
    private final LinkableToken right;

    private SidXm(AnySid left, LinkableToken right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public Collection<LinkedLegs> links() {
      Predicate<Leg> xm = leg -> PathTerminator.VM.equals(leg.pathTerminator()) || PathTerminator.FM.equals(leg.pathTerminator());
      Collection<Leg> fmVm = left.sid().exitLegs(xm);
      Collection<Leg> rightLegs = withLocation(right.graphRepresentation());
      return cartesianProduct(fmVm, rightLegs).stream()
          .map(pair -> new LinkedLegs(pair.first(), pair.second(), distanceOrMin(pair)))
          .toList();
    }
    private List<Leg> withLocation(Collection<LinkedLegs> linkedLegs) {
      return linkedLegs.stream()
          .flatMap(linked -> Stream.of(linked.source(), linked.target()))
          .distinct()
          .filter(leg -> leg.associatedFix().isPresent())
          .toList();
    }
    private static double distanceOrMin(Pair<Leg, Leg> pair) {
      return Optional.of(pair)
          .filter(l -> PathTerminator.FM.equals(l.first().pathTerminator()))
          .filter(l -> !l.first().associatedFix().orElseThrow().equals(l.second().associatedFix().orElseThrow())) //we know they have fixes from the leg type
          .map(i -> distanceBetween(pair))
          .orElse(LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT);
    }
  }
}

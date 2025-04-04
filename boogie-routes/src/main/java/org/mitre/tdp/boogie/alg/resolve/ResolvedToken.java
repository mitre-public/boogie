package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.resolve.infer.SectionInferrer;
import org.mitre.tdp.boogie.alg.split.RouteToken;

/**
 * A resolved token represents a piece of infrastructure which has been resolved from a {@link RouteToken}.
 *
 * <p>Tokens exist to provide an additional layer of specificity at the object level around infrastructure data such that context
 * from the route string can be converted to new objects in the object tree.
 *
 * <p>This allows us to create new objects wrapping the same infrastructure to provide additional context around how a resolved
 * piece of infrastructure should be interpreted.
 *
 * <p>Additionally these tokens provide "just enough" additional structured information beyond the scope of the original string
 * {@link RouteToken}s to allow for more advanced {@link SectionInferrer}s to be built on top of them.
 *
 * <p>Tokens are visit-able such that downstream classes can walk between the object graph of token types and make decisions about
 * how to interpret them where multiple are present for a given {@link RouteToken}.
 */
public interface ResolvedToken {

  /**
   * Wraps a resolved {@link Airport} indicting it was filed as part of the route string in the standard fashion.
   *
   * <p>The standard fashion here implies non-direct <i>and</i> typically as part of a {@code APT.SID.FIX} or {@code FIX.STAR.APT}
   * combination and indicates what airport the flight is arriving at or departing from (as SIDs and STARs may serve multiple).
   */
  static StandardAirport standardAirport(Airport airport) {
    return new StandardAirport(airport);
  }

  /**
   * Wraps a resolved {@link Airport} as one indicating that it was filed as "fly direct to" in the underlying route string.
   *
   * <p>This is common for aircraft headed to smaller airports where they don't need to be assigned a start and where (when close
   * enough in) the tower controller will assign them an approach procedure.
   *
   * <p>The route strings themselves always omit approach information because it's hard to predict what approach the tower will
   * give you in advance due to local weather/ground conditions + it is completely at the controller's discretion.
   */
  static DirectToAirport directToAirport(Airport airport) {
    return new DirectToAirport(airport);
  }

  /**
   * Wraps a resolved {@link Airway} as one indicating it was filed in the standard fashion.
   *
   * <p>The standard fashion here generally implies it was filed as part of a {@code FIX.AWY.FIX} combination but resolvers may
   * choose to use this even if a more unorthodox combination is filed as long as the downstream {@link RouteChooser} is robust.
   */
  static StandardAirway standardAirway(Airway airway) {
    return new StandardAirway(airway);
  }

  /**
   * Wraps a resolved (typically inferred) {@link Procedure} as a standard approach procedure.
   *
   * <p>Approaches aren't filed as part of route strings, see {@link SectionInferrer}, but
   * they may be inferred. This serves as a default wrapper for them when the above occurs.
   */
  static StandardApproach standardApproach(Procedure procedure) {
    return new StandardApproach(procedure);
  }

  /**
   * A token representing a {@link Fix} which was filed in the "standard" fashion in the original route string.
   *
   * <p>In this case the standard fashion generally implies it was filed as the "entry or exit fix" of some procedure or airway
   * combination, e.g. {@code AWY.FIX.STAR} or {@code SID.FIX.STAR}.
   *
   * <p>As part of these combinations the fix is flown to as it is referenced in the joining leg of the subsequent procedure
   * (typically as a TF leg).
   *
   * <p>Alternatively this can be used as a "default" cases for fixes which weren't clearly filed to direct, e.g. those which were
   * filed post a tailoring indicator in the route {@code APT./.FIX}. Normally post-tailoring elements are fix-radial-distances
   * (FRDs) denoting the current position of the aircraft but a fix may be used if the aircraft recently traversed it.
   */
  static StandardFix standardFix(Fix fix) {
    return new StandardFix(fix);
  }

  /**
   * A token representing a {@link Fix} which was filed to "direct" in the original route string.
   *
   * <p>This is often done when shortcutting portions of a previously filed route, e.g {@code FIX..FIX.STAR.APT}, where the second
   * fix is filed to "direct" meaning don't follow any defined infrastructure just go there.
   */
  static DirectToFix directToFix(Fix fix) {
    return new DirectToFix(fix);
  }

  /**
   * A token representing a literal {@link LatLong} filed in the "standard" fashion in the original route string.
   *
   * <p>In this case the standard fashion generally implies it wasn't filed to "direct" and there is no clear special handling
   * that should be applied (essentially a default case).
   *
   * <p>It's typically to see these filed as "initial fixes" after a tailoring indicator in a flightplan, {@code APT./.LL}. Normally
   * post-tailoring elements are fix-radial-distances (FRDs) denoting the current position of the aircraft but may be a LatLon
   * if there are no appropriate fixes nearby (e.g. tailoring over the ocean).
   *
   * @param original the original text-encoding of the LatLong, requested as the without this Boogie can't faithfully reproduce
   *                 the original element name as the {@link Fix#fixIdentifier()} in the final route
   */
  static StandardLatLong standardLatLong(String original, LatLong latLong) {
    return new StandardLatLong(original, latLong);
  }

  /**
   * A token representing a literal {@link LatLong} value which was filed to "direct" in the original route string.
   *
   * <p>Typically these are filed during oceanic crossings and map to NATs (North Atlantic Tracks) or PACOTs (Pacific Organized
   * Track System) - which the ERAM system substitutes out of routes for international flights and replaces with Lat/Lon values.
   *
   * <p>^ is done so the FMS (and us) don't have to go look up what Atlantic/Pacific crossing tracks the FAA happens to be using
   * on the given day and can instead get back specifically flyable and easily interpretable LL values to fly.
   *
   * <p>Generally these show up as a series of direct-tos in the FP {@code 5300N/14000W..5400N/14200W}.
   *
   * @param original the original text-encoding of the LatLong, requested as the without this Boogie can't faithfully reproduce
   *                 the original element name as the {@link Fix#fixIdentifier()} in the final route
   */
  static DirectToLatLong directToLatLong(String original, LatLong latLong) {
    return new DirectToLatLong(original, latLong);
  }

  /**
   * A token representing a resolved or inferred common/enroute portion of the given SID {@link Procedure}.
   *
   * <p>Generally (because they lack runway information) flight plans only specify the (optional) Enroute and Common portions of
   * the SID (in filings like {@code APT.SID.FIX}).
   *
   * <p>In a route like that the runway is left unspecified and is up to the departure controller to specify close to the time of
   * departure of the flight, while the FIX implies the exit fix from the procedure and is usually the end of one of the enroute
   * transitions (though in reality it may be any fix on the procedure).
   *
   * <p>The above isn't enough context on its own to resolve a runway transition (if the SID has them) so this element exists to
   * represent that.
   *
   * <p>If additional context is provided a runway transition may be inferred, see {@link #sidRunway(Procedure)}.
   *
   * @param sid the unmodified procedure definition, consumers may decorate this as a
   *            {@link Procedure#maskTransitions(Procedure, Predicate)}.
   */
  static SidEnrouteCommon sidEnrouteCommon(Procedure sid) {
    return new SidEnrouteCommon(Procedure.onlyEnrouteCommon(sid));
  }

  /**
   * A token representing a resolved or inferred runway-transition portion of the given SID {@link Procedure}.
   *
   * <p>A typical route-string will not allow the inference of this from the content of the route itself, however with additional
   * context (i.e. departure runway) a runway transition can be inferred.
   *
   * <p>See {@link SectionInferrer}.
   *
   * @param sid the unmodified procedure definition, consumers may decorate this as a
   *            {@link Procedure#maskTransitions(Procedure, Predicate)}.
   */
  static SidRunway sidRunway(Procedure sid) {
    return new SidRunway(Procedure.onlyRunway(sid));
  }

  /**
   * A token representing a resolved or inferred common/enroute portion of the given STAR {@link Procedure}.
   *
   * <p>Generally (because they lack runway information) route strings only specify the (optional) Enroute and Common portions of
   * the STAR (in filings like {@code FIX.STAR.APT}).
   *
   * <p>In a route like that the runway (and approach procedure) are left unspecified and are up to the arrival controller to
   * specify closer to the arrival time of the flight. The FIX in the above specifies the entry point to the STAR and is typically
   * along one of the enroute transitions or on the common portion (though it may be any fix on the procedure).
   *
   * <p>The above isn't enough context on its own to resolve a runway transition (if the STAR has them) so this element exists to
   * represent that.
   *
   * <p>If additional context is provided a ruwany transition may be inferred, see {@link #starRunway(Procedure)}.
   *
   * @param star the unmodified procedure definition, consumers may decorate this as a
   *             {@link Procedure#maskTransitions(Procedure, Predicate)}.
   */
  static StarEnrouteCommon starEnrouteCommon(Procedure star) {
    return new StarEnrouteCommon(Procedure.onlyEnrouteCommon(star));
  }

  /**
   * A token representing a resolved or inferred runway-transition portion of the given STAR {@link Procedure}.
   *
   * <p>A typical route-string will not allow the inference of this from the content of the route itself, however with additional
   * context (i.e. arrival runway) a runway transition can be inferred.
   *
   * <p>See {@link SectionInferrer}.
   *
   * @param star the unmodified procedure definition, consumers may decorate this as a
   *             {@link Procedure#maskTransitions(Procedure, Predicate)}.
   */
  static StarRunway starRunway(Procedure star) {
    return new StarRunway(Procedure.onlyRunway(star));
  }

  /**
   * A token representing a "standard" filing of a {@link Fix#fixRadialDistance(Fix, Course, Distance)} in a route string.
   *
   * <p>Typically FRDs are "filed" (generated by automation) post tailoring of a route-string {@code APT./.FRD} and indicate the
   * new "initial" point of the route starting from the aircraft's current position.
   */
  static StandardFrd standardFrd(Fix.FixRadialDistance frd) {
    return new StandardFrd(frd);
  }

  /**
   * A token representing a FRD which was filed to "direct" in a route string.
   *
   * <p>Special case where a FRD is filed to direct as a normal fix in a route might be, see {@link #directToFix(Fix)}. This isn't
   * particularly common but can occur in some cases for domestic routes. It's unclear why operationally.
   */
  static DirectToFrd directToFrd(Fix.FixRadialDistance frd) {
    return new DirectToFrd(frd);
  }

  /**
   * The identifier of the {@link ResolvedToken} as it was looked-up from configured infrastructure data. If the token resolved
   * to a clear piece of nav infrastructure like a SID/STAR this should be the identifier of that resource.
   *
   * <p>However, some types of tokens resolve to infrastructure as a floating {@link LatLong} value or a fix-radial-distance and
   * may not be well-defined navigation element that can be looked up in a nav database or {@link LookupService}.
   */
  String identifier();

  /**
   * Returns the infrastructure element associated with this resolved token. This could be an airway, an airport, a fix, FRD or
   * any other type of infrastructure that can be referred to in a route string.
   */
  Object infrastructure();

  /**
   * Accepts a visitor for single-dispatch purposes where-in the visitor can handle elements of this token's concrete type and
   * do something interesting with it.
   */
  void accept(ResolvedTokenVisitor visitor);

  final class StandardAirport implements ResolvedToken {

    private final Airport airport;

    private StandardAirport(Airport airport) {
      this.airport = requireNonNull(airport);
    }

    @Override
    public String identifier() {
      return airport.airportIdentifier();
    }

    @Override
    public Airport infrastructure() {
      return airport;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class DirectToAirport implements ResolvedToken {

    private final Airport airport;

    private DirectToAirport(Airport airport) {
      this.airport = requireNonNull(airport);
    }

    @Override
    public String identifier() {
      return airport.airportIdentifier();
    }

    @Override
    public Airport infrastructure() {
      return airport;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class StandardAirway implements ResolvedToken {

    private final Airway airway;

    private StandardAirway(Airway airway) {
      this.airway = requireNonNull(airway);
    }

    @Override
    public String identifier() {
      return airway.airwayIdentifier();
    }

    @Override
    public Airway infrastructure() {
      return airway;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class StandardApproach implements ResolvedToken {

    private final Procedure procedure;

    private StandardApproach(Procedure procedure) {
      this.procedure = requireNonNull(procedure);
    }

    @Override
    public String identifier() {
      return procedure.procedureIdentifier();
    }

    @Override
    public Procedure infrastructure() {
      return procedure;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class StandardFix implements ResolvedToken {

    private final Fix fix;

    private StandardFix(Fix fix) {
      this.fix = requireNonNull(fix);
    }

    @Override
    public String identifier() {
      return fix.fixIdentifier();
    }

    @Override
    public Fix infrastructure() {
      return fix;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class DirectToFix implements ResolvedToken {

    private final Fix fix;

    private DirectToFix(Fix fix) {
      this.fix = requireNonNull(fix);
    }

    @Override
    public String identifier() {
      return fix.fixIdentifier();
    }

    @Override
    public Fix infrastructure() {
      return fix;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class StandardLatLong implements ResolvedToken {

    private final String original;

    private final LatLong latLong;

    private StandardLatLong(String original, LatLong latLong) {
      this.original = requireNonNull(original);
      this.latLong = requireNonNull(latLong);
    }

    @Override
    public String identifier() {
      return original;
    }

    @Override
    public LatLong infrastructure() {
      return latLong;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class DirectToLatLong implements ResolvedToken {

    private final String original;

    private final LatLong latLong;

    private DirectToLatLong(String original, LatLong latLong) {
      this.original = requireNonNull(original);
      this.latLong = requireNonNull(latLong);
    }

    @Override
    public String identifier() {
      return original;
    }

    @Override
    public LatLong infrastructure() {
      return latLong;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class SidEnrouteCommon implements ResolvedToken {

    private final Procedure sid;

    private SidEnrouteCommon(Procedure sid) {
      this.sid = requireNonNull(sid);
    }

    @Override
    public String identifier() {
      return sid.procedureIdentifier();
    }

    @Override
    public Procedure infrastructure() {
      return sid;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class SidRunway implements ResolvedToken {

    private final Procedure sid;

    private SidRunway(Procedure sid) {
      this.sid = requireNonNull(sid);
    }

    @Override
    public String identifier() {
      return sid.procedureIdentifier();
    }

    @Override
    public Procedure infrastructure() {
      return sid;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class StarEnrouteCommon implements ResolvedToken {

    private final Procedure star;

    private StarEnrouteCommon(Procedure star) {
      this.star = requireNonNull(star);
    }

    @Override
    public String identifier() {
      return star.procedureIdentifier();
    }

    @Override
    public Procedure infrastructure() {
      return star;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class StarRunway implements ResolvedToken {

    private final Procedure star;

    private StarRunway(Procedure star) {
      this.star = requireNonNull(star);
    }

    @Override
    public String identifier() {
      return star.procedureIdentifier();
    }

    @Override
    public Procedure infrastructure() {
      return star;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class StandardFrd implements ResolvedToken {

    private final Fix.FixRadialDistance frd;

    private StandardFrd(Fix.FixRadialDistance frd) {
      this.frd = requireNonNull(frd);
    }

    @Override
    public String identifier() {
      return String.format("[%f,%f]", frd.latitude(), frd.longitude());
    }

    @Override
    public Fix.FixRadialDistance infrastructure() {
      return frd;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }

  final class DirectToFrd implements ResolvedToken {

    private final Fix.FixRadialDistance frd;

    private DirectToFrd(Fix.FixRadialDistance frd) {
      this.frd = requireNonNull(frd);
    }

    @Override
    public String identifier() {
      return String.format("[%f,%f]", frd.latitude(), frd.longitude());
    }

    @Override
    public Fix.FixRadialDistance infrastructure() {
      return frd;
    }

    @Override
    public void accept(ResolvedTokenVisitor visitor) {
      visitor.visit(this);
    }
  }
}

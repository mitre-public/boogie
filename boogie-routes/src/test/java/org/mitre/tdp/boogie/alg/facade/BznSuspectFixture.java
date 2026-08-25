package org.mitre.tdp.boogie.alg.facade;

import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.VA;
import static org.mitre.tdp.boogie.MockObjects.VI;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.airway;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mitre.tdp.boogie.MockObjects.leg;
import static org.mitre.tdp.boogie.MockObjects.newProcedure;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.List;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

/**
 * Hand-assembled infrastructure for the {@code KBZN.BZN6.BZN.V365.CUSRI..KHLN} expansion so the test doesn't need to parse a
 * full nav-data file.
 * <p>
 * Coordinates were lifted verbatim from a debug run against the LIDO data.
 * <p>
 * The point of this fixture is the pair of <em>identically named</em> fixes near Oxford, England - {@link #BZN6_UK} and
 * {@link #BZN_UK}. They collide by name with the {@code BZN6} SID and the {@code BZN} VOR in Montana, and they are what drag the
 * chosen path across the Atlantic and back.
 */
final class BznSuspectFixture {

  private BznSuspectFixture() {
  }

  public static final Airport KBZN = airport("KBZN", 45.7772, -111.1503);

  public static final Airport KHLN = airport("KHLN", 46.6067, -111.9833);

  /**
   * The Montana BZN VOR - the actual exit fix of the {@code BZN6} departure.
   * <p>
   * Corroborated by the {@code CF} leg below, which records BZN at theta {@code 2230} / rho {@code 0150} from BRIGR - this
   * position computes to 15.0nm at 223 degrees magnetic given the E013.0 variation at the field.
   */
  public static final Fix BZN_MT = fix("BZN", 45.7839, -111.1555);

  public static final Fix BRIGR = fix("BRIGR", 45.6623, -111.4672);

  public static final Fix CUSRI = fix("CUSRI", 46.4852, -111.6370);

  /**
   * Oxford-area fix that shares its name with the Montana {@code BZN6} SID.
   */
  public static final Fix BZN6_UK = fix("BZN6", 51.7377, -1.4435);

  /**
   * Oxford-area fix that shares its name with the Montana {@code BZN} VOR.
   */
  public static final Fix BZN_UK = fix("BZN", 51.7482, -1.6035);

  /**
   * The {@code BZN6} departure out of KBZN, transcribed from the ARINC 424 records.
   *
   * <p><b>This procedure is made up entirely of RUNWAY transitions - there is no COMMON and no ENROUTE transition.</b> It is a
   * plain VOR departure: each runway flies its own route straight to the BZN VOR, so there is no common segment to factor out.
   * Note the route-type digit {@code 1} on both transitions, versus the {@code 3} carried by the enroute transitions on the other
   * KBZN departures ({@code BGSKY2}, {@code BOBKT5}, {@code MEADO2}).
   * <p>
   * That shape is the crux of the bug:
   * {@link org.mitre.tdp.boogie.alg.resolve.ResolvedToken#sidEnrouteCommon(Procedure)} masks RUNWAY transitions away, which
   * leaves a procedure with zero transitions, which the token grapher renders as zero legs. The {@code BZN6} position in the
   * route graph is then occupied solely by the same-named {@link #BZN6_UK} fix.
   * <p>
   * {@code HIA5} at the same airport has the identical runway-only shape and will fail the same way, so this is a data pattern
   * rather than a one-off.
   */
  public static Procedure BZN6() {
    Transition rw12 = transition("RW12", "BZN6", "KBZN", TransitionType.RUNWAY, ProcedureType.SID, List.of(
        VA(),
        VI(),
        leg("BRIGR", 45.6623, -111.4672, PathTerminator.CF, 30, 223.0),
        leg("BZN", 45.7839, -111.1555, PathTerminator.DF, 40, null)
    ));

    Transition rw30 = transition("RW30", "BZN6", "KBZN", TransitionType.RUNWAY, ProcedureType.SID, List.of(
        VA(),
        VI(),
        leg("BRIGR", 45.6623, -111.4672, PathTerminator.CF, 30, 223.0),
        leg("BZN", 45.7839, -111.1555, PathTerminator.DF, 40, null)
    ));

    return newProcedure(List.of(rw12, rw30));
  }

  /**
   * V365 from BYI through CTB, transcribed from the enroute airway records.
   */
  public static Airway V365() {
    return airway("V365", List.of(
        TF("BYI", 42.5802, -113.8659, 70),
        TF("ACFIJ", 43.2131, -113.0835, 80),
        TF("JATTS", 43.2668, -112.9076, 90),
        TF("ZELOR", 43.3363, -112.6780, 95),
        TF("ROCCA", 43.4012, -112.4616, 100),
        TF("IDA", 43.5190, -112.0639, 110),
        TF("RIGBY", 43.6549, -111.9558, 120),
        TF("SABAT", 44.0166, -111.6653, 130),
        TF("LVM", 45.7025, -110.4425, 140),
        TF("ZUBLI", 45.6495, -110.9107, 150),
        TF("BZN", 45.7839, -111.1555, 160),
        TF("MENAR", 45.9908, -111.2754, 170),
        TF("GODFE", 46.2625, -111.4344, 175),
        TF("SWEDD", 46.4496, -111.5449, 180),
        TF("CUSRI", 46.4852, -111.6370, 185),
        TF("HLN", 46.6068, -111.9535, 190),
        TF("USOBE", 46.8215, -111.9948, 192),
        TF("KOTLE", 46.8546, -112.0012, 194),
        TF("CUSDA", 46.9371, -112.0172, 197),
        TF("WOKEN", 46.9819, -112.0259, 200),
        TF("ROSOE", 47.3190, -112.0918, 210),
        TF("SHIMY", 47.4620, -112.1201, 220),
        TF("CHOTE", 47.6657, -112.1606, 230),
        TF("PENRY", 48.0820, -112.2443, 240),
        TF("WELUR", 48.3999, -112.3093, 245),
        TF("CTB", 48.5649, -112.3433, 250)
    ));
  }

  public static List<Airport> airports() {
    return List.of(KBZN, KHLN);
  }

  public static List<Procedure> procedures() {
    return List.of(BZN6());
  }

  public static List<Airway> airways() {
    return List.of(V365());
  }

  /**
   * Note the two Oxford-area imposters sitting alongside the Montana fixes - both are indexed by the same identifiers the route
   * string references.
   */
  public static List<Fix> fixes() {
    return List.of(BZN_MT, BRIGR, CUSRI, BZN6_UK, BZN_UK);
  }
}

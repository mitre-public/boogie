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
 *
 * <p>Coordinates were lifted verbatim from a debug run against the LIDO data.
 *
 * <p>The point of this fixture is the pair of <em>identically named</em> fixes near Oxford, England - {@link #BZN6_UK} and
 * {@link #BZN_UK}. They collide by name with the {@code BZN6} SID and the {@code BZN} VOR in Montana, and they are what drag the
 * chosen path across the Atlantic and back.
 */
final class BznSuspectFixture {

  private BznSuspectFixture() {
  }

  /**
   * {@code SUSAP KBZNK1ABZN     110000093YHN45463805W111090094E013004473250...}
   */
  public static final Airport KBZN = airport("KBZN", 45.7772361, -111.1502611);

  public static final Airport KHLN = airport("KHLN", 46.6067, -111.9833);

  /**
   * The Montana BZN VOR - the actual exit fix of the {@code BZN6} departure.
   *
   * <p>Corroborated by the {@code CF} leg below, which records BZN at theta {@code 2230} / rho {@code 0150} from BRIGR - this
   * position computes to 15.0nm at 223 degrees magnetic given the E013.0 variation at the field.
   */
  public static final Fix BZN_MT = fix("BZN", 45.7839, -111.1555);

  /**
   * {@code SUSAP KBZNK1CBRIGR K10    R Z   N45394428W111280200 ... E0113}
   */
  public static final Fix BRIGR = fix("BRIGR", 45.6623000, -111.4672222);

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
   *
   * <p>That shape is the crux of the bug:
   * {@link org.mitre.tdp.boogie.alg.resolve.ResolvedToken#sidEnrouteCommon(Procedure)} masks RUNWAY transitions away, which
   * leaves a procedure with zero transitions, which the token grapher renders as zero legs. The {@code BZN6} position in the
   * route graph is then occupied solely by the same-named {@link #BZN6_UK} fix.
   *
   * <p>{@code HIA5} at the same airport has the identical runway-only shape and will fail the same way, so this is a data pattern
   * rather than a one-off.
   *
   * <pre>
   * SUSAP KBZNK1DBZN6  1RW12 P010         0        VA                     1230        + 04873     18000
   * SUSAP KBZNK1DBZN6  1RW12 P020         0    R   VIY                    2550
   * SUSAP KBZNK1DBZN6  1RW12 P030BRIGRK1PC0EY      CF BZN K1      2230015022300110D
   * SUSAP KBZNK1DBZN6  1RW12 P040BZN  K1D 0VE  R   DF
   * SUSAP KBZNK1DBZN6  1RW30 P010         0        VA                     3030        + 04873     18000
   * SUSAP KBZNK1DBZN6  1RW30 P020         0        VI                     2000
   * SUSAP KBZNK1DBZN6  1RW30 P030BRIGRK1PC0EY      CF BZN K1      2230015022300120D
   * SUSAP KBZNK1DBZN6  1RW30 P040BZN  K1D 0VE  R   DF
   * </pre>
   */
  public static Procedure BZN6() {
    Transition rw12 = transition("RW12", "BZN6", "KBZN", TransitionType.RUNWAY, ProcedureType.SID, List.of(
        VA(),
        VI(),
        leg("BRIGR", 45.6623000, -111.4672222, PathTerminator.CF, 30, 223.0),
        leg("BZN", 45.7839, -111.1555, PathTerminator.DF, 40, null)
    ));

    Transition rw30 = transition("RW30", "BZN6", "KBZN", TransitionType.RUNWAY, ProcedureType.SID, List.of(
        VA(),
        VI(),
        leg("BRIGR", 45.6623000, -111.4672222, PathTerminator.CF, 30, 223.0),
        leg("BZN", 45.7839, -111.1555, PathTerminator.DF, 40, null)
    ));

    return newProcedure(List.of(rw12, rw30));
  }

  /**
   * V365 running roughly north-to-south from CTB down through HLN to CUSRI.
   */
  public static Airway V365() {
    return airway("V365", List.of(
        TF("CTB", 48.5649, -112.3433, 10),
        TF("WELUR", 48.3999, -112.3093, 20),
        TF("PENRY", 48.0820, -112.2443, 30),
        TF("CHOTE", 47.6657, -112.1606, 40),
        TF("SHIMY", 47.4620, -112.1201, 50),
        TF("ROSOE", 47.3190, -112.0918, 60),
        TF("WOKEN", 46.9819, -112.0259, 70),
        TF("CUSDA", 46.9371, -112.0172, 80),
        TF("KOTLE", 46.8546, -112.0012, 90),
        TF("USOBE", 46.8215, -111.9948, 100),
        TF("HLN", 46.6068, -111.9535, 110),
        TF("CUSRI", 46.4852, -111.6370, 120)
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

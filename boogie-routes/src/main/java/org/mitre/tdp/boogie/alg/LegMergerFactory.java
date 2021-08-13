package org.mitre.tdp.boogie.alg;

import java.util.function.BiPredicate;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.model.ProcedureGraph;

public final class LegMergerFactory {

  private LegMergerFactory() {
    throw new IllegalStateException("Unable to instantiate static factory class.");
  }

  /**
   * Returns true when the right leg is a {@link PathTerminator#IF} to the same fix the left leg terminates in.
   * <br>
   * This is provided as a standard method as the {@link GraphBasedRouteChooser} (and the {@link ProcedureGraph}) implementations
   * used in the route resolution naturally include the IF legs marking the start of new transitions within a procedure. Take for
   * example the path from DRSDN->HOBTT within the HOBTT2 procedure at KATL:
   * <br>
   * (DRSDN Transitions){DRSDN(IF)->SMAWG(TF)} ----> (HOBTT Common Portion){SMAWG(IF)->HOBTT(TF)}
   * <br>
   * The graphical route chooser will include the second waypoint SMAWG as an IF and as a TF, this merger will remove the subsequent
   * redundant IF declaration.
   */
  public static <L extends Leg> BiPredicate<L, L> isTrailingInternalIF() {
    return (left, right) -> right.pathTerminator().equals(PathTerminator.IF) && matchingAssociatedFixIdentifiers(left, right);
  }

  /**
   * Returns true when either the right or the left leg is a DF and the {@link Leg#associatedFix()} of the previous/next leg and
   * the DF are identical.
   * <br>
   * This is provided for the cases where the route has a filed DF to what would otherwise be a TF/etc. leg in the entry/exit
   * procedure. Take for example a flight joining the HOBTT procedure in the middle of one of the enroute transitions:
   * <br>
   * Route = WPT1..BLLBO.HOBTT2.KATL, BLLBO is a part of the SHYRE transition which looks like:
   * <br>
   * SHYRE(IF)->FRDDO(TF)->BLLBO(TF)->BGGNS(TF)->SMAWG(TF)
   * <br>
   * The graphical route chooser will return BLLBO(DF)->BLLBO(TF)->BGGNS(TF) with the current linking rules - in this case we want
   * to elide the second BLLBO(TF) as the leg which is actually flown is the direct (DF) to BLLBO and then the TFs onward.
   */
  public static <L extends Leg> BiPredicate<L, L> isLeadingTrailingDF() {
    return (left, right) -> (PathTerminator.DF.equals(left.pathTerminator()) || PathTerminator.DF.equals(right.pathTerminator())) && matchingAssociatedFixIdentifiers(left, right);
  }

  public static <L extends Leg> BiPredicate<L, L> isSubsequentSimilarLeg() {
    return (left, right) -> left.pathTerminator().equals(right.pathTerminator()) && matchingAssociatedFixIdentifiers(left, right);
  }

  static boolean matchingAssociatedFixIdentifiers(Leg left, Leg right) {
    return left.associatedFix().isPresent()
        && right.associatedFix().isPresent()
        && left.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier()
        .equalsIgnoreCase(right.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier());
  }
}

package org.mitre.tdp.boogie;

import java.util.Collection;

import org.mitre.tdp.boogie.model.ProcedureFactory;

/**
 * High level indicator as to the the relative performance capabilities aircraft need to fly the procedure.
 */
public enum RequiredNavigationEquipage {
  /**
   * Conventional procedures are ones where aircraft fly directly between pairs of physical facilities or with direct reference to
   * particular ones.
   */
  CONV,
  /**
   * An RNAV procedure is any procedure which can be flown without direct reference to conventional physical infrastructure such
   * as NDB/VORs/etc. RNAV waypoints don't always directly correspond to traditional infrastructure but can be the product of pairs
   * of VORs, etc. Basically remember you can do RNAV without GPS, same with RNP.
   */
  RNAV,
  /**
   * RNP is really any RNAV procedure which has onboard alerting for the aircraft. This gets a bit weird because its less of a
   * procedure thing and more of an aircraft equipage question. Basically a RNP procedure is a procedure any RNAV equipped aircraft
   * can fly but which has been encoded in such a way that the aircraft (if equipped) will provide alerts when it is too far off
   * route.
   * <br>
   * So whether an aircraft flies the RNAV procedure as RNP depends on whether they have the onboard equipage for the notifications
   * <i>and</i> whether the procedure itself was encoded with RNP tolerances on the legs.
   */
  RNP,
  /**
   * Placeholder - the required high-level equipage can't always be inferred for various procedure sources - this is provided as
   * a placeholder for those situations. e.g. within the {@link ProcedureFactory#newProcedure(Collection)} method - where required
   * equipage can't be inferred based solely on the transition interface.
   */
  UNKNOWN
}

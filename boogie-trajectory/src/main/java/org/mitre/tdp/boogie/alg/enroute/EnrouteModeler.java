package org.mitre.tdp.boogie.alg.enroute;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.models.TraversedLeg;
import org.mitre.tdp.boogie.utils.Iterators;

/**
 * Simple wrapper class for executing the flight modeling and re-wrapping the output.
 */
public class EnrouteModeler {

  /**
   * The model to use to execute the modeling.
   */
  private final EnrouteModel model;

  private EnrouteModeler(EnrouteModel model) {
    this.model = model;
  }

  public List<TraversedLeg> model(List<Leg> legs) {
    List<TraversedLeg> traversed = new ArrayList<>();
    Iterators.fastslow2(legs, leg -> leg.type().concrete(), model::predict);
    return traversed;
  }

  public static EnrouteModeler with(@Nonnull EnrouteModel model) {
    return new EnrouteModeler(model);
  }
}

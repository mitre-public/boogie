package org.mitre.tdp.boogie.alg;

import java.util.List;
import javax.annotation.Nonnull;

import org.mitre.caasd.commons.TimeWindow;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.approach.ApproachModel;
import org.mitre.tdp.boogie.alg.departure.DepartureModel;
import org.mitre.tdp.boogie.alg.enroute.EnrouteModel;
import org.mitre.tdp.boogie.alg.enroute.EnrouteModeler;
import org.mitre.tdp.boogie.models.TraversedLeg;
import org.mitre.tdp.boogie.models.TraversedRoute;

public class FlightModeler {

  private final DepartureModel departureModel;
  private final EnrouteModel enrouteModel;
  private final ApproachModel approachModel;

  private FlightModeler(DepartureModel d, EnrouteModel e, ApproachModel a) {
    this.departureModel = d;
    this.enrouteModel = e;
    this.approachModel = a;
  }

  public DepartureModel departureModel() {
    return departureModel;
  }

  public EnrouteModel enrouteModel() {
    return enrouteModel;
  }

  public ApproachModel approachModel() {
    return approachModel;
  }

  public TraversedRoute model(Airport departure, List<Leg> legs, Airport arrival) {
    TimeWindow departureDuration = departureModel.predict(departure, legs.get(0));

    List<TraversedLeg> traversedLegs = EnrouteModeler.with(enrouteModel).model(legs);

    TimeWindow arrivalDuration = approachModel.predict(legs.get(legs.size() - 1), arrival);

    return new TraversedRoute(departureDuration, traversedLegs, arrivalDuration);
  }

  public static FlightModeler with(@Nonnull DepartureModel d, @Nonnull EnrouteModel e, @Nonnull ApproachModel a) {
    return new FlightModeler(d, e, a);

  }
}

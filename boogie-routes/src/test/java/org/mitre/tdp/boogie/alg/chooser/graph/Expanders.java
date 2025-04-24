package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.alg.facade.FluentRouteExpander;

public final class Expanders {

    public static FluentRouteExpander wsssExpander() {
      Airport wsss = MockObjects.airport("WSSS", 0.0, 6.0);
      Airport other = MockObjects.airport("OTHER", 5.0, 4.0);

      Fix bv413 = Fix.builder().fixIdentifier("BV413").latLong(LatLong.of(5.0, 5.0)).build();
      Fix bv414 = Fix.builder().fixIdentifier("BV414").latLong(LatLong.of(5.0, 6.0)).build();
      Fix lebat = Fix.builder().fixIdentifier("LEBAT").latLong(LatLong.of(5.0, 7.0)).build();

      Leg l10 = Leg.ifBuilder(bv413, 0).build();
      Leg l11 = Leg.dfBuilder(bv414, 1).build();
      Leg l12 = Leg.dfBuilder(lebat, 2).build();

      Fix paspu = Fix.builder().fixIdentifier("PASPU").latLong(LatLong.of(0.0, 2.0)).build();
      Fix pu = Fix.builder().fixIdentifier("PU").latLong(LatLong.of(0.0, 3.0)).build();
      Fix sj = Fix.builder().fixIdentifier("SJ").latLong(LatLong.of(0.0, 4.0)).build();
      Fix palga = Fix.builder().fixIdentifier("PALGA").latLong(LatLong.of(0.0, 5.0)).build();

      Leg l2 = Leg.ifBuilder(paspu, 0).build();
      Leg l3 = Leg.tfBuilder(pu, 1).build();
      Leg l4 = Leg.tfBuilder(sj, 2).build();
      Leg l5 = Leg.tfBuilder(palga, 3).build();

      List<Leg> starLegs = List.of(l2, l3, l4, l5);
      List<Leg> sidLegs = List.of(l10, l11, l12);

      Transition sidTrans = Transition.builder()
          .transitionIdentifier("ALL")
          .transitionType(TransitionType.COMMON)
          .legs(sidLegs)
          .build();
      Transition starTrans = Transition.builder()
          .transitionIdentifier("RW02C")
          .transitionType(TransitionType.RUNWAY)
          .legs(starLegs)
          .build();

      Procedure sid = Procedure.builder()
          .procedureIdentifier("LEBA2A")
          .procedureType(ProcedureType.SID)
          .transitions(List.of(sidTrans))
          .airportIdentifier("OTHER")
          .requiredNavigationEquipage(RequiredNavigationEquipage.RNAV)
          .build();

      Procedure star = Procedure.builder()
          .procedureIdentifier("LEBA2A")
          .procedureType(ProcedureType.STAR)
          .transitions(List.of(starTrans))
          .airportIdentifier("WSSS")
          .requiredNavigationEquipage(RequiredNavigationEquipage.RNAV)
          .build();

      return FluentRouteExpander.inMemoryBuilder(List.of(wsss, other), List.of(sid, star), List.of(), List.of(paspu, lebat)).build();
    }
}

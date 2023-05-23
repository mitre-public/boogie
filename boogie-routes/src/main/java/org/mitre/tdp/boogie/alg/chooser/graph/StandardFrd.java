package org.mitre.tdp.boogie.alg.chooser.graph;

import org.mitre.tdp.boogie.alg.FixRadialDistance;

import static java.util.Objects.requireNonNull;

final class StandardFrd implements GraphableToken {

   private final FixRadialDistance frd;

   StandardFrd(FixRadialDistance frd){
     this.frd = requireNonNull(frd);
   }
}

package org.mitre.tdp.boogie.alg.chooser.graph;

import org.mitre.tdp.boogie.Procedure;

import static java.util.Objects.requireNonNull;

final class StarEnrouteCommon implements GraphableToken {

   private final Procedure star;

   StarEnrouteCommon(Procedure star){
     this.star = requireNonNull(star);
   }
}

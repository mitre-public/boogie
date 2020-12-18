package org.mitre.tdp.boogie;

import java.util.List;

/**  */
@FunctionalInterface
public interface Route {

  List<? extends Leg> legs();
}

package org.mitre.tdp.boogie.models;

import org.mitre.tdp.boogie.Leg;

public class LinkedLeg {

  private final Leg source;
  private final Leg target;

  public LinkedLeg(Leg s, Leg t) {
    this.source = s;
    this.target = t;
  }

  public Leg source() {
    return source;
  }

  public Leg target() {
    return target;
  }
}

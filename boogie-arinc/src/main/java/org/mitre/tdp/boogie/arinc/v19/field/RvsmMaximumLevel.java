package org.mitre.tdp.boogie.arinc.v19.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * Definition/Description: RVSM Maximum Level is the highest defined cruising level for an airway or holding pattern.
 */
public final class RvsmMaximumLevel extends ArincInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.295";
  }
}

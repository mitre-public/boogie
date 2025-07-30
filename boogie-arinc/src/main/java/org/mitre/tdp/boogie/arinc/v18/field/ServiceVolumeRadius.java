package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * The service volume radius identifies the radius of the service
 * volume around the transmitter in Nautical miles.
 */
public final class ServiceVolumeRadius extends ArincInteger {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.245";
  }
}

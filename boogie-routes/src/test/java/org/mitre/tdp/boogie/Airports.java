package org.mitre.tdp.boogie;

import static org.mitre.tdp.boogie.MockObjects.airport;

/**
 * Collection of mock airport objects and their actual locations for use in testing.
 */
public final class Airports {

  private Airports() {
    throw new IllegalStateException("Unable to instantiate static factory class.");
  }

  public static Airport KDEN() {
    return airport("KDEN", 39.861666666666665, -104.67316666666667);
  }

  public static Airport KATL() {
    return airport("KATL", 33.6367, -84.4278638888889);
  }

  public static Airport KMCO() {
    return airport("KMCO", 28.4293889, -81.3090000);
  }

  public static Airport KPHL() {
    return airport("KPHL", 39.872084, -75.240663);
  }

  public static Airport KSEA(){
    return airport("KSEA", 47.449888888888886, -122.31177777777778);
  }

  public static Airport GQNO() {
    return airport("GQNO", 18.31, -15.969722);
  }
}

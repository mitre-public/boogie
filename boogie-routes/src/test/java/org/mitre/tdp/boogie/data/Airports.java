package org.mitre.tdp.boogie.data;

import org.mitre.tdp.boogie.Airport;

import static org.mitre.tdp.boogie.ObjectMocks.airport;

/**
 * Collection of mock airport objects and their actual locations for use in testing.
 */
public class Airports {

  public static Airport KDEN() {
    return airport("KDEN", 39.861666666666665, -104.67316666666667);
  }

  public static Airport KATL() {
    return airport("KATL", 33.6367, -84.4278638888889);
  }
}

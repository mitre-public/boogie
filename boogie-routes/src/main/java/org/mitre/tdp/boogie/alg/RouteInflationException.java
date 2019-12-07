package org.mitre.tdp.boogie.alg;

/**
 * Specific wrapper exception class for when a error occurs while perfroming the route
 * expansion.
 */
public class RouteInflationException extends Exception {

  public RouteInflationException(String msg) {
    super(msg);
  }

  public RouteInflationException(String msg, Throwable throwable) {
    super(msg, throwable);
  }
}

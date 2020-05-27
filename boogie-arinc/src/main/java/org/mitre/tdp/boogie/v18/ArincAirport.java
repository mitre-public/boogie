package org.mitre.tdp.boogie.v18;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.ArincRecord;

/**
 * Interface for an {@link Airport} which decorates a parsed {@link ArincRecord}.
 */
public interface ArincAirport extends Airport {

  ArincRecord arincRecord();
}

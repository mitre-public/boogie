package org.mitre.tdp.boogie;

import java.util.List;

/**
 * Interface representing an instance of a full ARINC specification - which in this case is a collection of record
 * specifications with their own internal field specifications.
 */
public interface ArincSpec {

  /**
   * The collection of {@link RecordSpec}s associate with this overall instantiation of the ARINC specification.
   */
  List<RecordSpec> recordSpecs();
}

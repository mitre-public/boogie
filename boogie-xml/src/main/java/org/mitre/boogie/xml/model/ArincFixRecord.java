package org.mitre.boogie.xml.model;

import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;

/**
 * Common interface for XML model types that can be assembled as fixes. All fix-like record types (waypoints, navaids,
 * runways, gates, helipads, localizers, markers, GLS, and airport reference points) implement this interface.
 *
 * <p>This enables a single-record {@code FixAssembler.assemble(ArincFixRecord)} method mirroring the ARINC pattern.
 */
public interface ArincFixRecord {

  ArincPointInfo pointInfo();

  ArincRecordInfo recordInfo();
}

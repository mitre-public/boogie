package org.mitre.tdp.boogie.arinc.model;

import java.io.Serializable;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Common interface for all ARINC-424 related record model classes (e.g. {@link ArincAirport}, {@link ArincRunway}) providing
 * programmatic access to the section and subsection of the ARINC-424 database the records came from.
 */
public interface ArincModel extends Serializable {

  SectionCode sectionCode();

  /**
   * The vast majority of record types where we want to build a concrete model class will have an explicitly populated sub-section
   * code (as not everything within the broader section fits into the same model) - but there are a couple record types which are
   * unique in this sense (e.g. {@link ArincVhfNavaid}s) so the interface returns optional.
   * <br>
   * In cases where this field is required on the record type the various 'Validator' classes will enforce its presence pre-convert
   * and the overrides of this method in the model classes will explicitly use {@link Optional#of(Object)} on return to disallow
   * null values.
   */
  Optional<String> subSectionCode();
}

package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

/**
 * Class representing an in-memory version of a navigation database based around ARINC-424 formatted navigation data.
 * <br>
 * This class represents a set of multi-indexed database
 */
public final class ArincDatabase {

  private ArincDatabase(

  ) {

  }

  private static final class ArincKey {

    private final String identifier;
    private final String icaoRegion;
    private final String section;
    private final String subsection;


    public ArincKey(String identifier, String icaoRegion, String section, String subsection) {
      this.identifier = requireNonNull(identifier);
      this.icaoRegion = requireNonNull(icaoRegion);
      this.section = requireNonNull(section);
      this.subsection = requireNonNull(subsection);
    }

    public String identifier() {
      return identifier;
    }

    public String icaoRegion() {
      return icaoRegion;
    }

    public String section() {
      return section;
    }

    public String subsection() {
      return subsection;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ArincKey arincKey = (ArincKey) o;
      return Objects.equals(identifier, arincKey.identifier) &&
          Objects.equals(icaoRegion, arincKey.icaoRegion) &&
          Objects.equals(section, arincKey.section) &&
          Objects.equals(subsection, arincKey.subsection);
    }

    @Override
    public int hashCode() {
      return Objects.hash(identifier, icaoRegion, section, subsection);
    }

    @Override
    public String toString() {
      return "ArincKey{" +
          "identifier='" + identifier + '\'' +
          ", icaoRegion='" + icaoRegion + '\'' +
          ", section='" + section + '\'' +
          ", subsection='" + subsection + '\'' +
          '}';
    }
  }
}

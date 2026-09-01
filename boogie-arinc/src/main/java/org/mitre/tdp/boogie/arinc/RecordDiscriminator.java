package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

/**
 * A coarse, fixed-width address for a {@link RecordSpec}. The declared alternatives must cover every input for which
 * {@link RecordSpec#matchesRecord(String)} can return {@code true}. False positives are allowed because a discriminator only
 * selects candidate specifications; {@code matchesRecord} still makes the final decision.
 */
public sealed interface RecordDiscriminator {

  /**
   * Selects a section code from column 5 and subsection code from column 6.
   */
  static RecordDiscriminator column6(char sectionCode, char subsectionCode) {
    return new SectionSubsection(sectionCode, subsectionCode, SubsectionColumn.COLUMN_6);
  }

  /**
   * Selects a section code from column 5 and subsection code from column 13.
   */
  static RecordDiscriminator column13(char sectionCode, char subsectionCode) {
    return new SectionSubsection(sectionCode, subsectionCode, SubsectionColumn.COLUMN_13);
  }

  /**
   * Selects records beginning with the supplied prefix.
   */
  static RecordDiscriminator prefix(String prefix) {
    return new Prefix(prefix);
  }

  /**
   * A section/subsection address in the fixed-width record.
   */
  record SectionSubsection(char sectionCode, char subsectionCode, SubsectionColumn subsectionColumn)
      implements RecordDiscriminator {

    public SectionSubsection {
      if (sectionCode > 127 || subsectionCode > 127) {
        throw new IllegalArgumentException("Section and subsection codes must be ASCII characters");
      }
      requireNonNull(subsectionColumn);
    }
  }

  /**
   * A fixed prefix identifying a record independently of section/subsection columns.
   */
  record Prefix(String value) implements RecordDiscriminator {

    public Prefix {
      requireNonNull(value);
      if (value.isEmpty() || !value.chars().allMatch(character -> character < 128)) {
        throw new IllegalArgumentException("Record discriminator prefixes must be non-empty ASCII strings");
      }
    }
  }

  /**
   * Supported subsection columns, named using the one-based column numbering from ARINC 424.
   */
  enum SubsectionColumn {
    COLUMN_6,
    COLUMN_13
  }
}

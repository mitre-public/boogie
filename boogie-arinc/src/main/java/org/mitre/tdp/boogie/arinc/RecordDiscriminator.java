package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.arinc.utils.PrimaryRecord;

/**
 * A complete fixed-width matcher for a {@link RecordSpec}. The parser may use the structured portion as a fast candidate index,
 * but this discriminator remains the source of truth for the final match.
 */
public sealed interface RecordDiscriminator {

  /**
   * Matches a primary record using section/subsection codes from columns 5 and 6.
   *
   * @param continuationRecordIndex the zero-based Java index containing the continuation record number
   */
  static RecordDiscriminator primaryColumn6(
      char sectionCode,
      char subsectionCode,
      int continuationRecordIndex
  ) {
    return new SectionSubsection(
        sectionCode,
        subsectionCode,
        SubsectionColumn.COLUMN_6,
        continuationRecordIndex,
        MatchKind.PRIMARY,
        '\0'
    );
  }

  /**
   * Matches a primary record using the section code from column 5 and subsection code from column 13.
   *
   * @param continuationRecordIndex the zero-based Java index containing the continuation record number
   */
  static RecordDiscriminator primaryColumn13(
      char sectionCode,
      char subsectionCode,
      int continuationRecordIndex
  ) {
    return new SectionSubsection(
        sectionCode,
        subsectionCode,
        SubsectionColumn.COLUMN_13,
        continuationRecordIndex,
        MatchKind.PRIMARY,
        '\0'
    );
  }

  /**
   * Matches an application continuation using section/subsection codes from columns 5 and 6.
   *
   * @param continuationRecordIndex the zero-based Java index containing the continuation record number; the application type
   *                                is read from the following index
   * @param applicationType the application type identifying the continuation layout
   */
  static RecordDiscriminator continuationColumn6(
      char sectionCode,
      char subsectionCode,
      int continuationRecordIndex,
      char applicationType
  ) {
    return new SectionSubsection(
        sectionCode,
        subsectionCode,
        SubsectionColumn.COLUMN_6,
        continuationRecordIndex,
        MatchKind.APPLICATION,
        applicationType
    );
  }

  /**
   * Matches an application continuation using the section code from column 5 and subsection code from column 13.
   *
   * @param continuationRecordIndex the zero-based Java index containing the continuation record number; the application type
   *                                is read from the following index
   * @param applicationType the application type identifying the continuation layout
   */
  static RecordDiscriminator continuationColumn13(
      char sectionCode,
      char subsectionCode,
      int continuationRecordIndex,
      char applicationType
  ) {
    return new SectionSubsection(
        sectionCode,
        subsectionCode,
        SubsectionColumn.COLUMN_13,
        continuationRecordIndex,
        MatchKind.APPLICATION,
        applicationType
    );
  }

  /**
   * Selects records beginning with the supplied prefix.
   */
  static RecordDiscriminator prefix(String prefix) {
    return new Prefix(prefix);
  }

  /** Returns whether this discriminator matches the supplied record. */
  boolean matches(String rawRecord);

  /** Returns the minimum record length needed to evaluate this discriminator. */
  int requiredLength();

  /**
   * A section/subsection address qualified as either a primary or application-continuation record.
   */
  record SectionSubsection(
      char sectionCode,
      char subsectionCode,
      SubsectionColumn subsectionColumn,
      int continuationRecordIndex,
      MatchKind matchKind,
      char applicationType
  ) implements RecordDiscriminator {

    private static final PrimaryRecord PRIMARY = PrimaryRecord.INSTANCE;
    public SectionSubsection {
      if (sectionCode > 127 || subsectionCode > 127) {
        throw new IllegalArgumentException("Section and subsection codes must be ASCII characters");
      }
      requireNonNull(subsectionColumn);
      requireNonNull(matchKind);

      int maximumIndex = matchKind == MatchKind.APPLICATION ? Integer.MAX_VALUE - 2 : Integer.MAX_VALUE - 1;
      if (continuationRecordIndex < 0 || continuationRecordIndex > maximumIndex) {
        throw new IllegalArgumentException("Continuation record index must be non-negative and addressable");
      }
      if (matchKind == MatchKind.PRIMARY && applicationType != '\0') {
        throw new IllegalArgumentException("Primary discriminators cannot declare an application type");
      }
      if (matchKind == MatchKind.APPLICATION && (applicationType == '\0' || applicationType > 127)) {
        throw new IllegalArgumentException("Continuation application type must be a non-null ASCII character");
      }
    }

    @Override
    public boolean matches(String rawRecord) {
      requireNonNull(rawRecord);
      if (rawRecord.length() < requiredLength()
          || rawRecord.charAt(4) != sectionCode
          || rawRecord.charAt(subsectionColumn.index()) != subsectionCode) {
        return false;
      }

      char continuationNumber = rawRecord.charAt(continuationRecordIndex);
      return switch (matchKind) {
        case PRIMARY -> PRIMARY.test(continuationNumber);
        case APPLICATION -> !PRIMARY.test(continuationNumber)
            && rawRecord.charAt(continuationRecordIndex + 1) == applicationType;
      };
    }

    @Override
    public int requiredLength() {
      int continuationLength = continuationRecordIndex + (matchKind == MatchKind.APPLICATION ? 2 : 1);
      return Math.max(subsectionColumn.index() + 1, continuationLength);
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

    @Override
    public boolean matches(String rawRecord) {
      return requireNonNull(rawRecord).startsWith(value);
    }

    @Override
    public int requiredLength() {
      return value.length();
    }
  }

  /** Supported continuation qualifications. */
  enum MatchKind {
    PRIMARY,
    APPLICATION
  }

  /**
   * Supported subsection columns, named using the one-based column numbering from ARINC 424.
   */
  enum SubsectionColumn {
    COLUMN_6(5),
    COLUMN_13(12);

    private final int index;

    SubsectionColumn(int index) {
      this.index = index;
    }

    int index() {
      return index;
    }
  }
}

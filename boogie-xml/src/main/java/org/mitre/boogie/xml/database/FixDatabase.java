package org.mitre.boogie.xml.database;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple index of pre-assembled fix objects keyed by their XML reference ID ({@code referenceId}).
 *
 * <p>Legs in the XML schema reference fixes via IDREF elements which correspond to the {@code referenceId} attribute
 * on each point record. This database indexes every record that carries an
 * {@link org.mitre.boogie.xml.model.fields.ArincPointInfo} &mdash; top-level waypoints, NDB navaids, VHF navaids,
 * and airports together with all of their nested records (runways, gates, terminal waypoints, terminal NDBs, helipads,
 * localizer/glideslopes, markers, and GNSS landing systems).
 *
 * @see FixDatabaseFactory
 */
public final class FixDatabase<F> {

  private final Map<String, F> fixesByReferenceId;

  FixDatabase(Map<String, F> fixesByReferenceId) {
    this.fixesByReferenceId = requireNonNull(fixesByReferenceId);
  }

  /**
   * Create a new builder for incremental construction of a {@link FixDatabase}.
   */
  public static <F> Builder<F> builder() {
    return new Builder<>();
  }

  /**
   * Look up a pre-assembled fix by its XML reference ID.
   */
  public Optional<F> fix(String referenceId) {
    return Optional.ofNullable(fixesByReferenceId.get(referenceId));
  }

  /**
   * Builder for incrementally constructing a {@link FixDatabase} during streaming unmarshalling.
   */
  public static final class Builder<F> {

    private final Map<String, F> index = new HashMap<>();

    private Builder() {
    }

    /**
     * Index a pre-assembled fix by its XML reference ID.
     */
    public void index(String referenceId, F fix) {
      index.put(referenceId, fix);
    }

    public FixDatabase<F> build() {
      return new FixDatabase<>(index);
    }
  }
}

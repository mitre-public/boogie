package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Identifies the model element on which an airspace is based, independently of any resolved point location.
 */
public final class CenterIdentification {

  /**
   * Center identifier
   */
  private final String identifier;
  /**
   * The area that the item is from, often an Area Code from ARINC 424
   */
  private final String area;
  /**
   * The 2 letter icao region
   */
  private final String icaoRegion;
  /**
   * The boogie type that is being referenced here.
   */
  private final BoogieType type;

  private CenterIdentification(Builder builder) {
    this.identifier = requireNonNull(builder.identifier, "identifier");
    this.area = builder.area;
    this.icaoRegion = builder.icaoRegion;
    this.type = builder.type;
  }

  public static Builder builder(String identifier) {
    return new Builder(identifier);
  }

  public Builder toBuilder() {
    return builder(identifier)
        .area(area)
        .icaoRegion(icaoRegion)
        .type(type);
  }

  public String identifier() {
    return identifier;
  }

  public Optional<String> area() {
    return Optional.ofNullable(area);
  }

  public Optional<String> icaoRegion() {
    return Optional.ofNullable(icaoRegion);
  }

  public Optional<BoogieType> type() {
    return Optional.ofNullable(type);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CenterIdentification that)) {
      return false;
    }
    return Objects.equals(identifier, that.identifier)
        && Objects.equals(area, that.area)
        && Objects.equals(icaoRegion, that.icaoRegion)
        && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifier, area, icaoRegion, type);
  }

  @Override
  public String toString() {
    return "CenterIdentification{" +
        "identifier='" + identifier + '\'' +
        ", area='" + area + '\'' +
        ", icaoRegion='" + icaoRegion + '\'' +
        ", type=" + type +
        '}';
  }

  public static final class Builder {
    private final String identifier;
    private String area;
    private String icaoRegion;
    private BoogieType type;

    private Builder(String identifier) {
      this.identifier = requireNonNull(identifier, "identifier");
    }

    public Builder area(String area) {
      this.area = area;
      return this;
    }

    public Builder icaoRegion(String icaoRegion) {
      this.icaoRegion = icaoRegion;
      return this;
    }

    public Builder type(BoogieType type) {
      this.type = type;
      return this;
    }

    public CenterIdentification build() {
      return new CenterIdentification(this);
    }
  }
}

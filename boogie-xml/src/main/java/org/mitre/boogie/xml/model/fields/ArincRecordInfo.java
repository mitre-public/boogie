package org.mitre.boogie.xml.model.fields;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class ArincRecordInfo implements Serializable {
  private final String areaCode;
  private final String customerCode;
  private final String cycleDate;
  private final List<String> notes;
  private final ArincRecordType recordType;

  private ArincRecordInfo(Builder builder) {
    this.areaCode = builder.areaCode;
    this.customerCode = builder.customerCode;
    this.cycleDate = builder.cycleDate;
    this.notes = builder.notes;
    this.recordType = builder.recordType;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .areaCode(areaCode)
        .customerCode(customerCode)
        .cycleDate(cycleDate)
        .notes(notes)
        .recordType(recordType);
  }

  public Optional<ArincAreaCode> areaCode() {
    return Optional.ofNullable(areaCode).filter(ArincAreaCode.VALID::contains).map(ArincAreaCode::valueOf);
  }

  public Optional<String> customerCode() {
    return Optional.ofNullable(customerCode);
  }

  public String cycleDate() {
    return cycleDate;
  }

  public List<String> notes() {
    return Optional.ofNullable(notes).orElseGet(Collections::emptyList);
  }

  public ArincRecordType recordType() {
    return recordType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArincRecordInfo that = (ArincRecordInfo) o;
    return Objects.equals(areaCode, that.areaCode) && Objects.equals(customerCode, that.customerCode) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(notes, that.notes) && recordType == that.recordType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(areaCode, customerCode, cycleDate, notes, recordType);
  }

  @Override
  public String toString() {
    return "ArincRecordInfo{" +
        ", areaCode='" + areaCode + '\'' +
        ", customerCode='" + customerCode + '\'' +
        ", cycleDate='" + cycleDate + '\'' +
        ", notes=" + notes +
        ", recordType=" + recordType +
        '}';
  }

  public static class Builder {
    private String areaCode;
    private String customerCode;
    private String cycleDate;
    private List<String> notes;
    private ArincRecordType recordType;

    private Builder() {}

    public Builder areaCode(String areaCode) {
      this.areaCode = areaCode;
      return this;
    }

    public Builder customerCode(String customerCode) {
      this.customerCode = customerCode;
      return this;
    }

    public Builder cycleDate(String cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder notes(List<String> notes) {
      this.notes = notes;
      return this;
    }

    public Builder recordType(ArincRecordType recordType) {
      this.recordType = recordType;
      return this;
    }

    public ArincRecordInfo build() {
      return new ArincRecordInfo(this);
    }
  }
}

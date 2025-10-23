package org.mitre.tdp.boogie.arinc.model;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.v18.field.ProductionTestFlag;

public class ArincHeaderOne implements ArincHeader {
  private final int headerNumber;
  private final String fileName;
  private final Integer version;
  private final String productionTestFlag;
  private final Integer recordLength;
  private final Integer recordCount;
  private final String cycle;
  private final Instant creationTime;
  private final String dataSupplierIdentifier;
  private final String targetCustomerIdent;
  private final String databasePartNumber;
  private final String fileCrc;

  public ArincHeaderOne(Builder builder) {
    this.headerNumber = builder.headerNumber;
    this.fileName = builder.fileName;
    this.version = builder.version;
    this.productionTestFlag = builder.productionTestFlag;
    this.recordLength = builder.recordLength;
    this.recordCount = builder.recordCount;
    this.cycle = builder.cycle;
    this.creationTime = builder.creationTime;
    this.dataSupplierIdentifier = builder.dataSupplierIdentifier;
    this.targetCustomerIdent = builder.targetCustomerIdent;
    this.databasePartNumber = builder.databasePartNumber;
    this.fileCrc = builder.fileCrc;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .headerNumber(headerNumber)
        .fileName(fileName)
        .version(version)
        .productionTestFlag(productionTestFlag)
        .recordLength(recordLength)
        .recordCount(recordCount)
        .cycle(cycle)
        .creationTime(creationTime)
        .dataSupplierIdentifier(dataSupplierIdentifier)
        .targetCustomerIdent(targetCustomerIdent)
        .databasePartNumber(databasePartNumber)
        .fileCrc(fileCrc);
  }

  @Override
  public int headerNumber() {
    return headerNumber;
  }

  public Optional<String> fileName() {
    return Optional.ofNullable(fileName);
  }

  public Optional<Integer> version() {
    return Optional.ofNullable(version);
  }

  public Optional<ProductionTestFlag> productionTestFlag() {
    return Optional.ofNullable(productionTestFlag)
        .filter(ProductionTestFlag.VALID::contains)
        .map(ProductionTestFlag::valueOf);
  }

  public Optional<Integer> recordLength() {
    return Optional.ofNullable(recordLength);
  }

  public Optional<Integer> recordCount() {
    return Optional.ofNullable(recordCount);
  }

  public Optional<String> cycle() {
    return Optional.ofNullable(cycle);
  }

  public Optional<Instant> creationTime() {
    return Optional.ofNullable(creationTime);
  }

  public Optional<String> dataSupplierIdentifier() {
    return Optional.ofNullable(dataSupplierIdentifier);
  }

  public Optional<String> targetCustomerIdent() {
    return Optional.ofNullable(targetCustomerIdent);
  }

  public Optional<String> databasePartNumber() {
    return Optional.ofNullable(databasePartNumber);
  }

  public Optional<String> fileCrc() {
    return Optional.ofNullable(fileCrc);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    ArincHeaderOne that = (ArincHeaderOne) o;
    return headerNumber == that.headerNumber && Objects.equals(fileName, that.fileName) && Objects.equals(version, that.version) && Objects.equals(productionTestFlag, that.productionTestFlag) && Objects.equals(recordLength, that.recordLength) && Objects.equals(recordCount, that.recordCount) && Objects.equals(cycle, that.cycle) && Objects.equals(creationTime, that.creationTime) && Objects.equals(dataSupplierIdentifier, that.dataSupplierIdentifier) && Objects.equals(targetCustomerIdent, that.targetCustomerIdent) && Objects.equals(databasePartNumber, that.databasePartNumber) && Objects.equals(fileCrc, that.fileCrc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(headerNumber, fileName, version, productionTestFlag, recordLength, recordCount, cycle, creationTime, dataSupplierIdentifier, targetCustomerIdent, databasePartNumber, fileCrc);
  }

  @Override
  public String toString() {
    return "ArincHeaderOne{" +
        "headerNumber=" + headerNumber +
        ", fileName='" + fileName + '\'' +
        ", version=" + version +
        ", productionTestFlag='" + productionTestFlag + '\'' +
        ", recordLength=" + recordLength +
        ", recordCount=" + recordCount +
        ", cycle='" + cycle + '\'' +
        ", creationTime=" + creationTime +
        ", dataSupplierIdentifier='" + dataSupplierIdentifier + '\'' +
        ", targetCustomerIdent='" + targetCustomerIdent + '\'' +
        ", databasePartNumber='" + databasePartNumber + '\'' +
        ", fileCrc='" + fileCrc + '\'' +
        '}';
  }

  public static class Builder {
    private int headerNumber;
    private String fileName;
    private Integer version;
    private String productionTestFlag;
    private Integer recordLength;
    private Integer recordCount;
    private String cycle;
    private Instant creationTime;
    private String dataSupplierIdentifier;
    private String targetCustomerIdent;
    private String databasePartNumber;
    private String fileCrc;

    private  Builder() {
    }

    public Builder headerNumber(int headerNumber) {
      this.headerNumber = headerNumber;
      return this;
    }

    public Builder fileName(String fileName) {
      this.fileName = fileName;
      return this;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder productionTestFlag(String productionTestFlag) {
      this.productionTestFlag = productionTestFlag;
      return this;
    }

    public Builder recordLength(Integer recordLength) {
      this.recordLength = recordLength;
      return this;
    }

    public Builder recordCount(Integer recordCount) {
      this.recordCount = recordCount;
      return this;
    }

    public Builder cycle(String cycle) {
      this.cycle = cycle;
      return this;
    }

    public Builder creationTime(Instant creationTime) {
      this.creationTime = creationTime;
      return this;
    }

    public Builder dataSupplierIdentifier(String dataSupplierIdentifier) {
      this.dataSupplierIdentifier = dataSupplierIdentifier;
      return this;
    }

    public Builder targetCustomerIdent(String targetCustomerIdent) {
      this.targetCustomerIdent = targetCustomerIdent;
      return this;
    }

    public Builder databasePartNumber(String databasePartNumber) {
      this.databasePartNumber = databasePartNumber;
      return this;
    }

    public Builder fileCrc(String fileCrc) {
      this.fileCrc = fileCrc;
      return this;
    }

    public ArincHeaderOne build() {
      return new ArincHeaderOne(this);
    }
  }

}

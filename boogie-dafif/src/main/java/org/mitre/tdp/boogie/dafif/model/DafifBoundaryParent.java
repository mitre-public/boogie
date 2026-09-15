package org.mitre.tdp.boogie.dafif.model;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;

/**
 * Immutable contents of a DAFIF 8.1 BDRY_PAR.TXT record.
 */
public final class DafifBoundaryParent implements DafifModel {

  /**
   * BDRY_IDENT: {@link org.mitre.tdp.boogie.dafif.v81.field.BoundaryIdentification}.
   */
  private final String boundaryIdentification;

  /**
   * TYPE: {@link org.mitre.tdp.boogie.dafif.v81.field.BoundaryType}.
   */
  private final BoundaryType boundaryType;

  /**
   * NAME: {@link org.mitre.tdp.boogie.dafif.v81.field.Name}.
   */
  private final String name;

  /**
   * ICAO: {@link org.mitre.tdp.boogie.dafif.v81.field.IcaoCode}.
   */
  private final String icaoCode;

  /**
   * CON_AUTH: {@link org.mitre.tdp.boogie.dafif.v81.field.ControllingAuthority}.
   */
  private final String controllingAuthority;

  /**
   * LOC_HDATUM: {@link org.mitre.tdp.boogie.dafif.v81.field.LocalHorizontalDatum}.
   */
  private final String localHorizontalDatum;

  /**
   * WGS_DATUM: {@link org.mitre.tdp.boogie.dafif.v81.field.GeodeticDatum}.
   */
  private final String geodeticDatum;

  /**
   * COMM_NAME: {@link org.mitre.tdp.boogie.dafif.v81.field.CommunicationName}.
   */
  private final String communicationName;

  /**
   * COMM_FREQ1: {@link org.mitre.tdp.boogie.dafif.v81.field.CommunicationsFrequency}.
   */
  private final String communicationsFrequency1;

  /**
   * COMM_FREQ2: {@link org.mitre.tdp.boogie.dafif.v81.field.CommunicationsFrequency}.
   */
  private final String communicationsFrequency2;

  /**
   * CLASS: {@link org.mitre.tdp.boogie.dafif.v81.field.AirspaceClass}.
   */
  private final String airspaceClass;

  /**
   * CLASS_EXC: {@link org.mitre.tdp.boogie.dafif.v81.field.ClassExceptionFlag}.
   */
  private final String classExceptionFlag;

  /**
   * CLASS_EX_RMK: {@link org.mitre.tdp.boogie.dafif.v81.field.ClassExceptionRemarks}.
   */
  private final String classExceptionRemarks;

  /**
   * LEVEL: {@link org.mitre.tdp.boogie.dafif.v81.field.Level}.
   */
  private final String level;

  /**
   * UPPER_ALT: {@link org.mitre.tdp.boogie.dafif.v81.field.UpperAltitude}.
   */
  private final String upperAltitude;

  /**
   * LOWER_ALT: {@link org.mitre.tdp.boogie.dafif.v81.field.LowerAltitude}.
   */
  private final String lowerAltitude;

  /**
   * RNP: {@link org.mitre.tdp.boogie.dafif.v81.field.RequiredNavPerformance}.
   */
  private final Integer requiredNavPerformance;

  /**
   * CYCLE_DATE: {@link org.mitre.tdp.boogie.dafif.v81.field.CycleDate}.
   */
  private final Integer cycleDate;

  /**
   * UP_RVSM: {@link org.mitre.tdp.boogie.dafif.v81.field.UpperAltitude}.
   */
  private final String upperRvsm;

  /**
   * LO_RVSM: {@link org.mitre.tdp.boogie.dafif.v81.field.LowerAltitude}.
   */
  private final String lowerRvsm;

  public DafifBoundaryParent(Builder builder) {
    this.boundaryIdentification = builder.boundaryIdentification;
    this.boundaryType = builder.boundaryType;
    this.name = builder.name;
    this.icaoCode = builder.icaoCode;
    this.controllingAuthority = builder.controllingAuthority;
    this.localHorizontalDatum = builder.localHorizontalDatum;
    this.geodeticDatum = builder.geodeticDatum;
    this.communicationName = builder.communicationName;
    this.communicationsFrequency1 = builder.communicationsFrequency1;
    this.communicationsFrequency2 = builder.communicationsFrequency2;
    this.airspaceClass = builder.airspaceClass;
    this.classExceptionFlag = builder.classExceptionFlag;
    this.classExceptionRemarks = builder.classExceptionRemarks;
    this.level = builder.level;
    this.upperAltitude = builder.upperAltitude;
    this.lowerAltitude = builder.lowerAltitude;
    this.requiredNavPerformance = builder.requiredNavPerformance;
    this.cycleDate = builder.cycleDate;
    this.upperRvsm = builder.upperRvsm;
    this.lowerRvsm = builder.lowerRvsm;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder()
        .boundaryIdentification(boundaryIdentification)
        .boundaryType(boundaryType)
        .name(name)
        .icaoCode(icaoCode)
        .controllingAuthority(controllingAuthority)
        .localHorizontalDatum(localHorizontalDatum)
        .geodeticDatum(geodeticDatum)
        .communicationName(communicationName)
        .communicationsFrequency1(communicationsFrequency1)
        .communicationsFrequency2(communicationsFrequency2)
        .airspaceClass(airspaceClass)
        .classExceptionFlag(classExceptionFlag)
        .classExceptionRemarks(classExceptionRemarks)
        .level(level)
        .upperAltitude(upperAltitude)
        .lowerAltitude(lowerAltitude)
        .requiredNavPerformance(requiredNavPerformance)
        .cycleDate(cycleDate)
        .upperRvsm(upperRvsm)
        .lowerRvsm(lowerRvsm);
  }

  public String boundaryIdentification() {
    return boundaryIdentification;
  }

  public BoundaryType boundaryType() {
    return boundaryType;
  }

  public Optional<String> name() {
    return Optional.ofNullable(name);
  }

  public String icaoCode() {
    return icaoCode;
  }

  public Optional<String> controllingAuthority() {
    return Optional.ofNullable(controllingAuthority);
  }

  public Optional<String> localHorizontalDatum() {
    return Optional.ofNullable(localHorizontalDatum);
  }

  public String geodeticDatum() {
    return geodeticDatum;
  }

  public Optional<String> communicationName() {
    return Optional.ofNullable(communicationName);
  }

  public Optional<String> communicationsFrequency1() {
    return Optional.ofNullable(communicationsFrequency1);
  }

  public Optional<String> communicationsFrequency2() {
    return Optional.ofNullable(communicationsFrequency2);
  }

  public Optional<String> airspaceClass() {
    return Optional.ofNullable(airspaceClass);
  }

  public Optional<String> classExceptionFlag() {
    return Optional.ofNullable(classExceptionFlag);
  }

  public Optional<String> classExceptionRemarks() {
    return Optional.ofNullable(classExceptionRemarks);
  }

  public String level() {
    return level;
  }

  public String upperAltitude() {
    return upperAltitude;
  }

  public String lowerAltitude() {
    return lowerAltitude;
  }

  public Optional<Integer> requiredNavPerformance() {
    return Optional.ofNullable(requiredNavPerformance);
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  public String upperRvsm() {
    return upperRvsm;
  }

  public String lowerRvsm() {
    return lowerRvsm;
  }

  @Override
  public DafifFileType getFileType() {
    return DafifFileType.BOUNDARY_PARENT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DafifBoundaryParent that = (DafifBoundaryParent) o;
    return Objects.equals(boundaryIdentification, that.boundaryIdentification)
        && Objects.equals(boundaryType, that.boundaryType)
        && Objects.equals(name, that.name)
        && Objects.equals(icaoCode, that.icaoCode)
        && Objects.equals(controllingAuthority, that.controllingAuthority)
        && Objects.equals(localHorizontalDatum, that.localHorizontalDatum)
        && Objects.equals(geodeticDatum, that.geodeticDatum)
        && Objects.equals(communicationName, that.communicationName)
        && Objects.equals(communicationsFrequency1, that.communicationsFrequency1)
        && Objects.equals(communicationsFrequency2, that.communicationsFrequency2)
        && Objects.equals(airspaceClass, that.airspaceClass)
        && Objects.equals(classExceptionFlag, that.classExceptionFlag)
        && Objects.equals(classExceptionRemarks, that.classExceptionRemarks)
        && Objects.equals(level, that.level)
        && Objects.equals(upperAltitude, that.upperAltitude)
        && Objects.equals(lowerAltitude, that.lowerAltitude)
        && Objects.equals(requiredNavPerformance, that.requiredNavPerformance)
        && Objects.equals(cycleDate, that.cycleDate)
        && Objects.equals(upperRvsm, that.upperRvsm)
        && Objects.equals(lowerRvsm, that.lowerRvsm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        boundaryIdentification,
        boundaryType,
        name,
        icaoCode,
        controllingAuthority,
        localHorizontalDatum,
        geodeticDatum,
        communicationName,
        communicationsFrequency1,
        communicationsFrequency2,
        airspaceClass,
        classExceptionFlag,
        classExceptionRemarks,
        level,
        upperAltitude,
        lowerAltitude,
        requiredNavPerformance,
        cycleDate,
        upperRvsm,
        lowerRvsm);
  }

  @Override
  public String toString() {
    return "DafifBoundaryParent{"
        + "boundaryIdentification=" + boundaryIdentification
        + ", boundaryType=" + boundaryType
        + ", name=" + name
        + ", icaoCode=" + icaoCode
        + ", controllingAuthority=" + controllingAuthority
        + ", localHorizontalDatum=" + localHorizontalDatum
        + ", geodeticDatum=" + geodeticDatum
        + ", communicationName=" + communicationName
        + ", communicationsFrequency1=" + communicationsFrequency1
        + ", communicationsFrequency2=" + communicationsFrequency2
        + ", airspaceClass=" + airspaceClass
        + ", classExceptionFlag=" + classExceptionFlag
        + ", classExceptionRemarks=" + classExceptionRemarks
        + ", level=" + level
        + ", upperAltitude=" + upperAltitude
        + ", lowerAltitude=" + lowerAltitude
        + ", requiredNavPerformance=" + requiredNavPerformance
        + ", cycleDate=" + cycleDate
        + ", upperRvsm=" + upperRvsm
        + ", lowerRvsm=" + lowerRvsm
        + "}";
  }

  public static final class Builder {
    private String boundaryIdentification;
    private BoundaryType boundaryType;
    private String name;
    private String icaoCode;
    private String controllingAuthority;
    private String localHorizontalDatum;
    private String geodeticDatum;
    private String communicationName;
    private String communicationsFrequency1;
    private String communicationsFrequency2;
    private String airspaceClass;
    private String classExceptionFlag;
    private String classExceptionRemarks;
    private String level;
    private String upperAltitude;
    private String lowerAltitude;
    private Integer requiredNavPerformance;
    private Integer cycleDate;
    private String upperRvsm;
    private String lowerRvsm;

    public Builder boundaryIdentification(String boundaryIdentification) {
      this.boundaryIdentification = boundaryIdentification;
      return this;
    }

    public Builder boundaryType(BoundaryType boundaryType) {
      this.boundaryType = boundaryType;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder controllingAuthority(String controllingAuthority) {
      this.controllingAuthority = controllingAuthority;
      return this;
    }

    public Builder localHorizontalDatum(String localHorizontalDatum) {
      this.localHorizontalDatum = localHorizontalDatum;
      return this;
    }

    public Builder geodeticDatum(String geodeticDatum) {
      this.geodeticDatum = geodeticDatum;
      return this;
    }

    public Builder communicationName(String communicationName) {
      this.communicationName = communicationName;
      return this;
    }

    public Builder communicationsFrequency1(String communicationsFrequency1) {
      this.communicationsFrequency1 = communicationsFrequency1;
      return this;
    }

    public Builder communicationsFrequency2(String communicationsFrequency2) {
      this.communicationsFrequency2 = communicationsFrequency2;
      return this;
    }

    public Builder airspaceClass(String airspaceClass) {
      this.airspaceClass = airspaceClass;
      return this;
    }

    public Builder classExceptionFlag(String classExceptionFlag) {
      this.classExceptionFlag = classExceptionFlag;
      return this;
    }

    public Builder classExceptionRemarks(String classExceptionRemarks) {
      this.classExceptionRemarks = classExceptionRemarks;
      return this;
    }

    public Builder level(String level) {
      this.level = level;
      return this;
    }

    public Builder upperAltitude(String upperAltitude) {
      this.upperAltitude = upperAltitude;
      return this;
    }

    public Builder lowerAltitude(String lowerAltitude) {
      this.lowerAltitude = lowerAltitude;
      return this;
    }

    public Builder requiredNavPerformance(Integer requiredNavPerformance) {
      this.requiredNavPerformance = requiredNavPerformance;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder upperRvsm(String upperRvsm) {
      this.upperRvsm = upperRvsm;
      return this;
    }

    public Builder lowerRvsm(String lowerRvsm) {
      this.lowerRvsm = lowerRvsm;
      return this;
    }

    public DafifBoundaryParent build() {
      return new DafifBoundaryParent(this);
    }
  }
}

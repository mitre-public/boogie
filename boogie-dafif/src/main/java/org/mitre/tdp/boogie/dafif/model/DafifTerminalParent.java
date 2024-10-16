package org.mitre.tdp.boogie.dafif.model;

import org.mitre.tdp.boogie.dafif.v81.field.AirportIdentification;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalProcedureType;
import org.mitre.tdp.boogie.dafif.v81.field.TerminalIdentifier;
import org.mitre.tdp.boogie.dafif.v81.field.IcaoCode;
import org.mitre.tdp.boogie.dafif.v81.field.EmergencySafeAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.JulianDate;
import org.mitre.tdp.boogie.dafif.v81.field.AmendmentNumber;
import org.mitre.tdp.boogie.dafif.v81.field.OfficeOfPrimaryResponsibility;
import org.mitre.tdp.boogie.dafif.v81.field.HostCountryAuthority;
import org.mitre.tdp.boogie.dafif.v81.field.CycleDate;
import org.mitre.tdp.boogie.dafif.v81.field.AlternateTakeoffMinimums;
import org.mitre.tdp.boogie.dafif.v81.field.TransitionAltitude;
import org.mitre.tdp.boogie.dafif.v81.field.TransitionLevel;
import org.mitre.tdp.boogie.dafif.v81.field.ProcedureDesignCriteria;
import org.mitre.tdp.boogie.dafif.v81.field.ProcedureAmendmentDate;
import org.mitre.tdp.boogie.dafif.v81.field.ApproachRouteQualifier1;
import org.mitre.tdp.boogie.dafif.v81.field.ApproachRouteQualifier2;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService1;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService2;
import org.mitre.tdp.boogie.dafif.v81.field.LevelOfService3;
import org.mitre.tdp.boogie.dafif.v81.field.ProcedureDesignMagvar;

import java.util.Objects;
import java.util.Optional;

public final class DafifTerminalParent {
  /**
   * {@link AirportIdentification}
   */
  private final String airportIdentification;
  /**
   * {@link TerminalProcedureType}
   */
  private final Integer terminalProcedureType;
  /**
   * {@link TerminalIdentifier}
   */
  private final String terminalIdentifier;
  /**
   * {@link IcaoCode}
   */
  private final String icaoCode;
  /**
   * {@link EmergencySafeAltitude}
   */
  private final Integer emergencySafeAltitude;
  /**
   * {@link JulianDate}
   */
  private final Integer julianDate;
  /**
   * {@link AmendmentNumber}
   */
  private final String amendmentNumber;
  /**
   * {@link OfficeOfPrimaryResponsibility}
   */
  private final String officeOfPrimaryResponsibility;
  /**
   * {@link HostCountryAuthority}
   */
  private final String hostCountryAuthority;
  /**
   * {@link CycleDate}
   */
  private final Integer cycleDate;
  /**
   * {@link AlternateTakeoffMinimums}
   */
  private final String alternateTakeoffMinimums;
  /**
   * {@link TransitionAltitude}
   */
  private final Integer transitionAltitude;
  /**
   * {@link TransitionLevel}
   */
  private final Integer transitionLevel;
  /**
   * {@link ProcedureDesignCriteria}
   */
  private final String procedureDesignCriteria;
  /**
   * {@link ProcedureAmendmentDate}
   */
  private final String procedureAmendmentDate;
  /**
   * {@link ApproachRouteQualifier1}
   */
  private final String approachRouteQualifier1;
  /**
   * {@link ApproachRouteQualifier2}
   */
  private final String approachRouteQualifier2;
  /**
   * {@link LevelOfService}
   */
  private final String levelOfService1;
  /**
   * {@link LevelOfService1}
   */
  private final String levelOfServiceName1;
  /**
   * {@link LevelOfService}
   */
  private final String levelOfService2;
  /**
   * {@link LevelOfService2}
   */

  private final String levelOfServiceName2;
  /**
   * {@link LevelOfService}
   */
  private final String levelOfService3;
  /**
   * {@link LevelOfService3}
   */
  private final String levelOfServiceName3;
  /**
   * {@link ProcedureDesignMagvar}
   */
  private final String procedureDesignMagvar;

  public DafifTerminalParent(Builder builder) {
    this.airportIdentification = builder.airportIdentification;
    this.terminalProcedureType = builder.terminalProcedureType;
    this.terminalIdentifier = builder.terminalIdentifier;
    this.icaoCode = builder.icaoCode;
    this.emergencySafeAltitude = builder.emergencySafeAltitude;
    this.julianDate = builder.julianDate;
    this.amendmentNumber = builder.amendmentNumber;
    this.officeOfPrimaryResponsibility = builder.officeOfPrimaryResponsibility;
    this.hostCountryAuthority = builder.hostCountryAuthority;
    this.cycleDate = builder.cycleDate;
    this.alternateTakeoffMinimums = builder.alternateTakeoffMinimums;
    this.transitionAltitude = builder.transitionAltitude;
    this.transitionLevel = builder.transitionLevel;
    this.procedureDesignCriteria = builder.procedureDesignCriteria;
    this.procedureAmendmentDate = builder.procedureAmendmentDate;
    this.approachRouteQualifier1 = builder.approachRouteQualifier1;
    this.approachRouteQualifier2 = builder.approachRouteQualifier2;
    this.levelOfService1 = builder.levelOfService1;
    this.levelOfServiceName1 = builder.levelOfServiceName1;
    this.levelOfService2 = builder.levelOfService2;
    this.levelOfServiceName2 = builder.levelOfServiceName2;
    this.levelOfService3 = builder.levelOfService3;
    this.levelOfServiceName3 = builder.levelOfServiceName3;
    this.procedureDesignMagvar = builder.procedureDesignMagvar;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    DafifTerminalParent that = (DafifTerminalParent) o;
    return Objects.equals(airportIdentification, that.airportIdentification) && Objects.equals(terminalProcedureType, that.terminalProcedureType) && Objects.equals(terminalIdentifier, that.terminalIdentifier) && Objects.equals(icaoCode, that.icaoCode) && Objects.equals(emergencySafeAltitude, that.emergencySafeAltitude) && Objects.equals(julianDate, that.julianDate) && Objects.equals(amendmentNumber, that.amendmentNumber) && Objects.equals(officeOfPrimaryResponsibility, that.officeOfPrimaryResponsibility) && Objects.equals(hostCountryAuthority, that.hostCountryAuthority) && Objects.equals(cycleDate, that.cycleDate) && Objects.equals(alternateTakeoffMinimums, that.alternateTakeoffMinimums) && Objects.equals(transitionAltitude, that.transitionAltitude) && Objects.equals(transitionLevel, that.transitionLevel) && Objects.equals(procedureDesignCriteria, that.procedureDesignCriteria) && Objects.equals(procedureAmendmentDate, that.procedureAmendmentDate) && Objects.equals(approachRouteQualifier1, that.approachRouteQualifier1) && Objects.equals(approachRouteQualifier2, that.approachRouteQualifier2) && Objects.equals(levelOfService1, that.levelOfService1) && Objects.equals(levelOfServiceName1, that.levelOfServiceName1) && Objects.equals(levelOfService2, that.levelOfService2) && Objects.equals(levelOfServiceName2, that.levelOfServiceName2) && Objects.equals(levelOfService3, that.levelOfService3) && Objects.equals(levelOfServiceName3, that.levelOfServiceName3) && Objects.equals(procedureDesignMagvar, that.procedureDesignMagvar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportIdentification, terminalProcedureType, terminalIdentifier, icaoCode, emergencySafeAltitude, julianDate, amendmentNumber, officeOfPrimaryResponsibility, hostCountryAuthority, cycleDate, alternateTakeoffMinimums, transitionAltitude, transitionLevel, procedureDesignCriteria, procedureAmendmentDate, approachRouteQualifier1, approachRouteQualifier2, levelOfService1, levelOfServiceName1, levelOfService2, levelOfServiceName2, levelOfService3, levelOfServiceName3, procedureDesignMagvar);
  }

  public String airportIdentification() {
    return airportIdentification;
  }

  public Integer terminalProcedureType() {
    return terminalProcedureType;
  }

  public String terminalIdentifier() {
    return terminalIdentifier;
  }

  public String icaoCode() {
    return icaoCode;
  }

  public Integer julianDate() {
    return julianDate;
  }

  public String officeOfPrimaryResponsibility() {
    return officeOfPrimaryResponsibility;
  }

  public Integer cycleDate() {
    return cycleDate;
  }

  public String levelOfService1() {
    return levelOfService1;
  }

  public String levelOfService2() {
    return levelOfService2;
  }

  public String levelOfService3() {
    return levelOfService3;
  }

  public Optional<Integer> emergencySafeAltitude() {
    return Optional.ofNullable(emergencySafeAltitude);
  }

  public Optional<String> amendmentNumber() {
    return Optional.ofNullable(amendmentNumber);
  }

  public Optional<String> hostCountryAuthority() {
    return Optional.ofNullable(hostCountryAuthority);
  }

  public Optional<String> alternateTakeoffMinimums() {
    return Optional.ofNullable(alternateTakeoffMinimums);
  }

  public Optional<Integer> transitionAltitude() {
    return Optional.ofNullable(transitionAltitude);
  }

  public Optional<Integer> transitionLevel() {
    return Optional.ofNullable(transitionLevel);
  }

  public Optional<String> procedureDesignCriteria() {
    return Optional.ofNullable(procedureDesignCriteria);
  }

  public Optional<String> procedureAmendmentDate() {
    return Optional.ofNullable(procedureAmendmentDate);
  }

  public Optional<String> approachRouteQualifier1() {
    return Optional.ofNullable(approachRouteQualifier1);
  }

  public Optional<String> approachRouteQualifier2() {
    return Optional.ofNullable(approachRouteQualifier2);
  }

  public Optional<String> levelOfServiceName1() {
    return Optional.ofNullable(levelOfServiceName1);
  }

  public Optional<String> levelOfServiceName2() {
    return Optional.ofNullable(levelOfServiceName2);
  }

  public Optional<String> levelOfServiceName3() {
    return Optional.ofNullable(levelOfServiceName3);
  }

  public Optional<String> procedureDesignMagvar() {
    return Optional.ofNullable(procedureDesignMagvar);
  }

  public static class Builder {
    private String airportIdentification;
    private Integer terminalProcedureType;
    private String terminalIdentifier;
    private String icaoCode;
    private Integer emergencySafeAltitude;
    private Integer julianDate;
    private String amendmentNumber;
    private String officeOfPrimaryResponsibility;
    private String hostCountryAuthority;
    private Integer cycleDate;
    private String alternateTakeoffMinimums;
    private Integer transitionAltitude;
    private Integer transitionLevel;
    private String procedureDesignCriteria;
    private String procedureAmendmentDate;
    private String approachRouteQualifier1;
    private String approachRouteQualifier2;
    private String levelOfService1;
    private String levelOfServiceName1;
    private String levelOfService2;
    private String levelOfServiceName2;
    private String levelOfService3;
    private String levelOfServiceName3;
    private String procedureDesignMagvar;

    public Builder airportIdentification(String airportIdentification) {
      this.airportIdentification = airportIdentification;
      return this;
    }

    public Builder terminalProcedureType(Integer terminalProcedureType) {
      this.terminalProcedureType = terminalProcedureType;
      return this;
    }

    public Builder terminalIdentifier(String terminalIdentifier) {
      this.terminalIdentifier = terminalIdentifier;
      return this;
    }

    public Builder icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder emergencySafeAltitude(Integer emergencySafeAltitude) {
      this.emergencySafeAltitude = emergencySafeAltitude;
      return this;
    }

    public Builder julianDate(Integer julianDate) {
      this.julianDate = julianDate;
      return this;
    }

    public Builder amendmentNumber(String amendmentNumber) {
      this.amendmentNumber = amendmentNumber;
      return this;
    }

    public Builder officeOfPrimaryResponsibility(String officeOfPrimaryResponsibility) {
      this.officeOfPrimaryResponsibility = officeOfPrimaryResponsibility;
      return this;
    }

    public Builder hostCountryAuthority(String hostCountryAuthority) {
      this.hostCountryAuthority = hostCountryAuthority;
      return this;
    }

    public Builder cycleDate(Integer cycleDate) {
      this.cycleDate = cycleDate;
      return this;
    }

    public Builder alternateTakeoffMinimums(String alternateTakeoffMinimums) {
      this.alternateTakeoffMinimums = alternateTakeoffMinimums;
      return this;
    }

    public Builder transitionAltitude(Integer transitionAltitude) {
      this.transitionAltitude = transitionAltitude;
      return this;
    }

    public Builder transitionLevel(Integer transitionLevel) {
      this.transitionLevel = transitionLevel;
      return this;
    }

    public Builder procedureDesignCriteria(String procedureDesignCriteria) {
      this.procedureDesignCriteria = procedureDesignCriteria;
      return this;
    }

    public Builder procedureAmendmentDate(String procedureAmendmentDate) {
      this.procedureAmendmentDate = procedureAmendmentDate;
      return this;
    }

    public Builder approachRouteQualifier1(String approachRouteQualifier1) {
      this.approachRouteQualifier1 = approachRouteQualifier1;
      return this;
    }

    public Builder approachRouteQualifier2(String approachRouteQualifier2) {
      this.approachRouteQualifier2 = approachRouteQualifier2;
      return this;
    }

    public Builder levelOfService1(String levelOfService1) {
      this.levelOfService1 = levelOfService1;
      return this;
    }

    public Builder levelOfServiceName1(String levelOfServiceName1) {
      this.levelOfServiceName1 = levelOfServiceName1;
      return this;
    }

    public Builder levelOfService2(String levelOfService2) {
      this.levelOfService2 = levelOfService2;
      return this;
    }

    public Builder levelOfServiceName2(String levelOfServiceName2) {
      this.levelOfServiceName2 = levelOfServiceName2;
      return this;
    }

    public Builder levelOfService3(String levelOfService3) {
      this.levelOfService3 = levelOfService3;
      return this;
    }

    public Builder levelOfServiceName3(String levelOfServiceName3) {
      this.levelOfServiceName3 = levelOfServiceName3;
      return this;
    }

    public Builder procedureDesignMagvar(String procedureDesignMagvar) {
      this.procedureDesignMagvar = procedureDesignMagvar;
      return this;
    }

    public DafifTerminalParent build() {
      return new DafifTerminalParent(this);
    }
  }
}

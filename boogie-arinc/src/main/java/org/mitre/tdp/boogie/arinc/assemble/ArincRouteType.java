package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Route type code meanings change based on the {@link SectionCode} + subSection code of the containing object.
 * <br>
 * This class represents route type codes in TDP are represented as the record 'SectionCode_RouteTypeCode' e.g. ER_A for route type
 * A in an enroute airway.
 * <br>
 * Note - All PD, PE, PF route type definitions also apply to HD, HE, and HF route types. (Basically airport route descriptors also
 * apply to heliport records)
 * <br>
 * Note - CIFP distorts the route type field adding additional out of spec codes in accordance with the cycle readme. Thus routeType
 * is generally stored as a plain text string across the data sources with validation done against the spec-ed values done inline.
 */
public enum ArincRouteType {
  /**
   * Airline Airway (Tailored Data)
   */
  ER_A,
  /**
   * Control Airway
   */
  ER_C,
  /**
   * Direct Route
   */
  ER_D,
  /**
   * Helicopter Airway
   */
  ER_H,
  /**
   * Officially Designated Airway (except Helicopter or RNAV)
   */
  ER_O,
  /**
   * RNAV Airway
   */
  ER_R,
  /**
   * Undesignated ATS Route
   */
  ER_S,
  /**
   * North American routes for North Atlantic Traffic Common Portion
   */
  ET_C,
  /**
   * Preferential Routes
   */
  ET_D,
  /**
   * Pacific Oceanic Transition Routes (PACOTS)
   */
  ET_J,
  /**
   * TACAN Routes - Australia
   */
  ET_M,
  /**
   * North American routes for North Atlantic Traffic Non-Common Portion
   */
  ET_N,
  /**
   * Preferred/Preferential Overflight Routes
   */
  ET_0,
  /**
   * Preferred Routes
   */
  ET_P,
  /**
   * Traffic Orientation System Routes (TOS)
   */
  ET_S,
  /**
   * Tower Enroute Control Routes (TEC)
   */
  ET_T,
  /**
   * Engine Out SID
   */
  PD_0,
  /**
   * SID Runway Transition
   */
  PD_1,
  /**
   * SID or SID Common Route
   */
  PD_2,
  /**
   * SID Enroute Transition
   */
  PD_3,
  /**
   * RNAV SID Runway Transition
   */
  PD_4,
  /**
   * RNAV SID or SID Common Route
   */
  PD_5,
  /**
   * RNAV SID Enroute Transition
   */
  PD_6,
  /**
   * FMS SID Runway Transition
   */
  PD_F,
  /**
   * FMS SID or SID Common Route
   */
  PD_M,
  /**
   * FMS SID Enroute Transition
   */
  PD_S,
  /**
   * Vector SID Runway Transition
   */
  PD_T,
  /**
   * Vector SID Enroute Transition
   */
  PD_V,
  /**
   * STAR Enroute Transition
   */
  PE_1,
  /**
   * STAR or STAR Common Route
   */
  PE_2,
  /**
   * STAR Runway Transition
   */
  PE_3,
  /**
   * RNAV STAR Enroute Transition
   */
  PE_4,
  /**
   * RNAV STAR or STAR Common Route
   */
  PE_5,
  /**
   * RNAV STAR Runway Transition
   */
  PE_6,
  /**
   * Profile Descent Enroute Transition
   */
  PE_7,
  /**
   * Profile Descent Common Route
   */
  PE_8,
  /**
   * Profile Descent Runway Transition
   */
  PE_9,
  /**
   * FMS STAR Enroute Transition
   */
  PE_F,
  /**
   * FMS STAR or STAR Common Route
   */
  PE_M,
  /**
   * FMS STAR Runway Transition
   */
  PE_S,
  /**
   * Approach Transition
   */
  PF_A,
  /**
   * Localizer/Backcourse Approach
   */
  PF_B,
  /**
   * VORDME Approach
   */
  PF_D,
  /**
   * Flight Management System (FMS) Approach
   */
  PF_F,
  /**
   * Instrument Guidance System (IGS) Approach
   */
  PF_G,
  /**
   * Instrument Landing System (ILS) Approach
   */
  PF_I,
  /**
   * GNSS Landing System (GLS) Approach
   */
  PF_J,
  /**
   * Localizer Only (LOC) Approach
   */
  PF_L,
  /**
   * Microwave Landing System (MLS) Approach
   */
  PF_M,
  /**
   * Non-Directional Beacon (NDB) Approach
   */
  PF_N,
  /**
   * GPS Approach
   */
  PF_P,
  /**
   * NDB + DME Approach
   */
  PF_Q,
  /**
   * RNAV Approach
   */
  PF_R,
  /**
   * VOR Approach using VORDME/VORTAC
   */
  PF_S,
  /**
   * TACAN Approach
   */
  PF_T,
  /**
   * Simplified Directional Facility (SDF) Approach
   */
  PF_U,
  /**
   * VOR Approach
   */
  PF_V,
  /**
   * MLS Type A Approach
   */
  PF_W,
  /**
   * Localizer Directional Aid (LDA) Approach
   */
  PF_X,
  /**
   * MLS Type B/C Approach
   */
  PF_Y,
  /**
   * Missed Approach
   */
  PF_Z,
  /**
   * Used in RNP CIFP/LIDO procedures reflective of later ARINC specs
   */
  PF_H,

  // V20+ Fields
  /**
   * RNP SID runway transition
   */
  PD_R,
  /**
   * RNP SID common route
   */
  PD_N,
  /**
   * RNP SID enroute transition
   */
  PD_P,
  /**
   * RNP STAR enroute transition
   */
  PE_R,
  /**
   * RNP STAR common route
   */
  PE_N,
  /**
   * RNP STAR runway transition
   */
  PE_P;

  String arincRouteCharacter(){
    return name().split("_")[1];
  }

  static ArincRouteType from(ArincProcedureLeg arincProcedureLeg) {
    requireNonNull(arincProcedureLeg);

    return ArincRouteType.valueOf(
        arincProcedureLeg.sectionCode().name()
            .concat(arincProcedureLeg.subSectionCode().orElseThrow(IllegalStateException::new))
            .concat("_")
            .concat(arincProcedureLeg.routeType())
    );
  }
}

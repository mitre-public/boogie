package org.mitre.boogie.xml.v23_5.convert;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.v23_5.generated.MagneticVariation;
import org.mitre.boogie.xml.v23_5.generated.Procedure;
import org.mitre.boogie.xml.v23_5.generated.ProcedureDesignAircraftCategories;
import org.mitre.boogie.xml.v23_5.generated.ProcedureDesignAircraftTypes;

final class ArincProcedureConverter implements Function<Procedure, Optional<ArincProcedure>> {
  static final ArincProcedureConverter INSTANCE = new ArincProcedureConverter();

  private static final ArincProcedureValidator VALIDATOR = ArincProcedureValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;

  private ArincProcedureConverter() {
  }

  @Override
  public Optional<ArincProcedure> apply(Procedure procedure) {
    return Optional.ofNullable(procedure)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincProcedure convert(Procedure procedure) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(procedure);

    Optional<MagneticVariation> magVar = Optional.ofNullable(procedure.getProcedureDesignMagVar());
    Optional<ProcedureDesignAircraftCategories> cats = Optional.ofNullable(procedure.getProcedureDesignAircraftCategories());
    Optional<ProcedureDesignAircraftTypes> types = Optional.ofNullable(procedure.getProcedureDesignAircraftTypes());

    ArincProcedure.Builder builder = ArincProcedure.builder()
        .baseInfo(baseInfo)
        .identifier(procedure.getIdentifier())
        .procedureType(procedure.getClass().getSimpleName())
        .recordType(Optional.ofNullable(procedure.getRecordType()).map(Enum::name).orElse(null))
        .isRnav(procedure.isIsRnav())
        .isHelicopterOnlyProcedure(procedure.isIsHelicopterOnlyProcedure())
        .isMilitary(procedure.isIsMilitary())
        .isSpecial(procedure.isIsSpecial())
        .procedureDesignMagVarEwt(magVar.map(MagneticVariation::getMagneticVariationEWT).map(Enum::name).orElse(null))
        .procedureDesignMagVarValue(magVar.map(MagneticVariation::getMagneticVariationValue).orElse(null))
        .longIdent(procedure.getLongIdent())
        .procedureName(procedure.getProcedureName())
        .referenceId(procedure.getReferenceId())
        // ProcedureDesignAircraftCategories
        .isCategoryA(cats.map(ProcedureDesignAircraftCategories::isIsCategoryA).orElse(null))
        .isCategoryB(cats.map(ProcedureDesignAircraftCategories::isIsCategoryB).orElse(null))
        .isCategoryC(cats.map(ProcedureDesignAircraftCategories::isIsCategoryC).orElse(null))
        .isCategoryD(cats.map(ProcedureDesignAircraftCategories::isIsCategoryD).orElse(null))
        .isCategoryE(cats.map(ProcedureDesignAircraftCategories::isIsCategoryE).orElse(null))
        .isCategoryHelicopter(cats.map(ProcedureDesignAircraftCategories::isIsCategoryHelicopter).orElse(null))
        // ProcedureDesignAircraftTypes
        .isTypeJet(types.map(ProcedureDesignAircraftTypes::isIsTypeJet).orElse(null))
        .isTypeTurbojet(types.map(ProcedureDesignAircraftTypes::isIsTypeTurbojet).orElse(null))
        .isTypeTurboprop(types.map(ProcedureDesignAircraftTypes::isIsTypeTurboprop).orElse(null))
        .isTypeProp(types.map(ProcedureDesignAircraftTypes::isIsTypeProp).orElse(null))
        .isTypePiston(types.map(ProcedureDesignAircraftTypes::isIsTypePiston).orElse(null))
        .isTypeNonJets(types.map(ProcedureDesignAircraftTypes::isIsTypeNonJets).orElse(null))
        .isTypeNotLimited(types.map(ProcedureDesignAircraftTypes::isIsTypeNotLimited).orElse(null))
        .isTypeNonTurbojets(types.map(ProcedureDesignAircraftTypes::isIsTypeNonTurbojets).orElse(null));

    ProcedureSubtypeFieldsConverter.convert(procedure).applyTo(builder);

    return builder.build();
  }
}

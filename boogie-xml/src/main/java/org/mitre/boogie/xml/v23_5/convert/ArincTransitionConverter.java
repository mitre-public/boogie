package org.mitre.boogie.xml.v23_5.convert;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.model.ArincTransition;
import org.mitre.boogie.xml.model.fields.ArincBaseInfo;
import org.mitre.boogie.xml.v23_5.generated.Constraint;
import org.mitre.boogie.xml.v23_5.generated.ProcedureDesignAircraftCategories;
import org.mitre.boogie.xml.v23_5.generated.ProcedureDesignAircraftTypes;
import org.mitre.boogie.xml.v23_5.generated.ProcedureRoute;

final class ArincTransitionConverter implements Function<ProcedureRoute, Optional<ArincTransition>> {
  static final ArincTransitionConverter INSTANCE = new ArincTransitionConverter();

  private static final ArincTransitionValidator VALIDATOR = ArincTransitionValidator.INSTANCE;
  private static final ArincBaseConverter BASE_CONVERTER = ArincBaseConverter.INSTANCE;
  private static final ArincProcedureLegConverter LEG_CONVERTER = ArincProcedureLegConverter.INSTANCE;

  private ArincTransitionConverter() {
  }

  @Override
  public Optional<ArincTransition> apply(ProcedureRoute route) {
    return Optional.ofNullable(route)
        .filter(VALIDATOR)
        .map(this::convert);
  }

  private ArincTransition convert(ProcedureRoute route) {
    ArincBaseInfo baseInfo = BASE_CONVERTER.apply(route);

    List<ArincProcedureLeg> legs = route.getProcedureLeg().stream()
        .map(LEG_CONVERTER)
        .flatMap(Optional::stream)
        .collect(Collectors.toList());

    Optional<Constraint> transAlt = Optional.ofNullable(route.getTransitionAltitudeOrLevel());
    Optional<ProcedureDesignAircraftCategories> cats = Optional.ofNullable(route.getProcedureDesignAircraftCategory());
    Optional<ProcedureDesignAircraftTypes> types = Optional.ofNullable(route.getProcedureDesignAircraftType());

    ArincTransition.Builder builder = ArincTransition.builder()
        .baseInfo(baseInfo)
        .identifier(route.getIdentifier())
        .transitionType(route.getClass().getSimpleName())
        .legs(legs)
        // ProcedureRoute fields
        .transitionAltitudeOrLevel(transAlt.map(Constraint::getAltitude).orElse(null))
        .transitionAltitudeOrLevelIsFlightLevel(transAlt.map(Constraint::isIsFlightLevel).orElse(null))
        .msaRef(Optional.ofNullable(route.getMsaRef()).map(Object::toString).orElse(null))
        .isAtcAssignedOnly(route.isIsAtcAssignedOnly())
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

    ProcedureRouteFieldsConverter.convert(route).applyTo(builder);

    return builder.build();
  }
}

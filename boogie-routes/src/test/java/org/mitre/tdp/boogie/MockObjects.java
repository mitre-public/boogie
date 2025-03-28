package org.mitre.tdp.boogie;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.util.Declinations;


/**
 * Class containing helper code for generating mock objects.
 */
public final class MockObjects {

  public static Fix fix(String name, double lat, double lon) {
    return Fix.builder()
        .fixIdentifier(name)
        .latLong(LatLong.of(lat, lon))
        .magneticVariation(MagneticVariation.ofDegrees(Declinations.approx(lat, lon)))
        .build();
  }

  public static Leg leg(String name, double lat, double lon, PathTerminator type) {
    return leg(name, lat, lon, type, 0, null);
  }

  public static Leg leg(String name, double lat, double lon, PathTerminator type, int sequence, Double crs) {
    return Leg.builder(type, sequence).associatedFix(fix(name, lat, lon)).outboundMagneticCourse(crs).build();
  }

  public static Leg TF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.TF);
  }

  public static Leg TF(String name, double lat, double lon, int sequence) {
    return leg(name, lat, lon, PathTerminator.TF, sequence, null);
  }

  public static Leg IF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.IF);
  }

  public static Leg IF(String name, double lat, double lon, int sequence) {
    return leg(name, lat, lon, PathTerminator.IF, sequence, null);
  }

  public static Leg DF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.DF);
  }

  public static Leg DF(String name, double lat, double lon, int sequence) {
    return leg(name, lat, lon, PathTerminator.DF, sequence, null);
  }

  public static Leg CF(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.CF, 0, 100.0);
  }

  public static Leg FM(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.FM);
  }

  public static Leg HM(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.HM);
  }

  public static Leg HM(String name, double lat, double lon, int sequence) {
    return leg(name, lat, lon, PathTerminator.HM, sequence, null);
  }

  public static Leg FD(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.FD);
  }

  public static Leg FA(String name, double lat, double lon) {
    return leg(name, lat, lon, PathTerminator.FA);
  }

  public static Leg CA() {
    return nonConcreteLeg(PathTerminator.CA);
  }

  public static Leg VA() {
    return nonConcreteLeg(PathTerminator.VA);
  }

  public static Leg VI() {
    return nonConcreteLeg(PathTerminator.VI);
  }

  public static Leg VM() {
    return nonConcreteLeg(PathTerminator.VM);
  }

  public static Leg nonConcreteLeg(PathTerminator type) {
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(type);
    return leg;
  }

  public static Transition transition(String pname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    return transition(null, pname, "FOO", ttype, ptype, legs);
  }

  public static Transition transition(String pname, String aname, TransitionType ttype, ProcedureType ptype, List<Leg> legs) {
    return transition(null, pname, aname, ttype, ptype, legs);
  }

  public static Transition transition(String tname, String pname, String aname, TransitionType ttype, ProcedureType ptype, List<? extends Leg> legs) {
    return CompatTransition.builder()
        .transitionIdentifier(tname)
        .procedureIdentifier(pname)
        .airportIdentifier(aname)
        .transitionType(ttype)
        .procedureType(ptype)
        .legs(legs)
        .build();
  }

  public static Airport airport(String name, double lat, double lon) {
    return Airport.builder().airportIdentifier(name).latLong(LatLong.of(lat, lon)).build();
  }

  public static Airway airway(String name, List<? extends Leg> legs) {
    return Airway.builder().airwayIdentifier(name).legs(legs).build();
  }


  /**
   * Taken from legacy {@code ProcedureFactory} class and used extensively in unit tests.
   */
  public static Procedure newProcedure(Collection<Transition> transitions) {
    return newProcedureWithEquipage(transitions, RequiredNavigationEquipage.UNKNOWN);
  }

  /**
   * Taken from legacy {@code ProcedureFactory} class and used extensively in unit tests.
   */
  public static Procedure newProcedureWithEquipage(Collection<Transition> transitions, RequiredNavigationEquipage requiredNavigationEquipage) {
    CompatTransition representative = (CompatTransition) transitions.iterator().next();
    return Procedure.builder()
        .procedureIdentifier(representative.procedureIdentifier())
        .airportIdentifier(representative.airportIdentifier())
        .procedureType(representative.procedureType())
        .requiredNavigationEquipage(requiredNavigationEquipage)
        .transitions(transitions)
        .build();
  }

  /**
   * Taken from legacy {@code ProcedureFactory} class and used extensively in unit tests.
   */
  public static Collection<Procedure> newProcedures(Collection<Transition> transitions) {
    return transitions.stream().collect(Collectors.groupingBy(MockObjects::createGroupKey)).values()
        .stream().map(MockObjects::newProcedure).collect(Collectors.toList());
  }

  private static String createGroupKey(Transition transition) {
    return Optional.of(transition)
        .filter(t -> t instanceof CompatTransition)
        .map(CompatTransition.class::cast)
        .map(t -> t.procedureIdentifier().concat(t.airportIdentifier()).concat(t.procedureType().name()))
        .orElseThrow(() -> new IllegalArgumentException("Should be a CompatTransition if going through this - otherwise please assemble the procedure yourself with Procedure.builder()"));
  }

  /**
   * Left for compatability in our older unit tests - a bunch of them use features of the {@link Transition}s that exist at the
   * procedure-level now to group them behind the scenes into procedures...
   */
  private static final class CompatTransition implements Transition {

    private final Transition delegate;

    private final String procedureIdentifier;

    private final String airportIdentifier;

    private final ProcedureType procedureType;

    private CompatTransition(Builder builder) {
      this.delegate = builder.delegate.build();
      this.procedureIdentifier = builder.procedureIdentifier;
      this.airportIdentifier = builder.airportIdentifier;
      this.procedureType = builder.procedureType;
    }

    static Builder builder() {
      return new Builder();
    }

    public String procedureIdentifier() {
      return procedureIdentifier;
    }

    public String airportIdentifier() {
      return airportIdentifier;
    }

    public ProcedureType procedureType() {
      return procedureType;
    }

    @Override
    public Optional<String> transitionIdentifier() {
      return delegate.transitionIdentifier();
    }

    @Override
    public TransitionType transitionType() {
      return delegate.transitionType();
    }

    @Override
    public Set<CategoryOrType> categoryOrTypes() {
      return delegate.categoryOrTypes();
    }

    @Override
    public List<? extends Leg> legs() {
      return delegate.legs();
    }

    @Override
    public void accept(Visitor visitor) {
      delegate.accept(visitor);
    }

    private static final class Builder {

      private final Transition.Standard.Builder delegate = Transition.builder();

      private String procedureIdentifier;

      private String airportIdentifier;

      private ProcedureType procedureType;

      private Builder() {
      }

      public Builder transitionIdentifier(String transitionIdentifier) {
        this.delegate.transitionIdentifier(transitionIdentifier);
        return this;
      }

      public Builder transitionType(TransitionType transitionType) {
        this.delegate.transitionType(transitionType);
        return this;
      }

      /**
       * Override the current set of configured legs to be the provided list.
       */
      public Builder legs(List<? extends Leg> legs) {
        this.delegate.legs(legs);
        return this;
      }

      public Builder procedureIdentifier(String procedureIdentifier) {
        this.procedureIdentifier = procedureIdentifier;
        return this;
      }

      public Builder airportIdentifier(String airportIdentifier) {
        this.airportIdentifier = airportIdentifier;
        return this;
      }

      public Builder procedureType(ProcedureType procedureType) {
        this.procedureType = procedureType;
        return this;
      }

      public CompatTransition build() {
        return new CompatTransition(this);

      }
    }
  }
}

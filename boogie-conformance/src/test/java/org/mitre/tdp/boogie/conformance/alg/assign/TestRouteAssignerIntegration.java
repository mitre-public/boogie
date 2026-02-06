package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.dot.DOTExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.maps.MapBoxApi;
import org.mitre.caasd.commons.maps.MapBuilder;
import org.mitre.caasd.commons.maps.MapFeature;
import org.mitre.caasd.commons.maps.MapFeatures;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.OneshotRecordParser;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.HashCombinationStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.HybridHasher;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.PathTerminatorBasedLegHasher;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.RouteHasher;
import org.mitre.tdp.boogie.conformance.alg.assign.link.AllPhasesOfFlightLinker;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.score.StandardLegFeatureExtractor;
import org.mitre.tdp.boogie.conformance.alg.assign.score.StandardLegFeatureScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.StandardScoringStrategy;
import org.mitre.tdp.boogie.conformance.alg.assign.transition.StandardTransitionScorer;
import org.mitre.tdp.boogie.model.ProcedureFactory;
import org.mitre.tdp.boogie.model.ProcedureGraph;

@Tag("ASSIGNMENT")
@Tag("INTEGRATION")
public class TestRouteAssignerIntegration {
  static OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records;
  static OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure, Airspace, Heliport> records2;
  static Collection<TrackPoint> points;
  static File approachA = new File(System.getProperty("user.dir").concat("/src/test/resources/KGUC_H24-N.dat"));
  static File approachB = new File(System.getProperty("user.dir").concat("/src/test/resources/KGUC_H24.dat"));
  static File trackA =  new File(System.getProperty("user.dir").concat("/src/test/resources/one_flew.csv"));

  @BeforeEach
  public void setup() throws FileNotFoundException {
    records = OneshotRecordParser.standard(ArincVersion.V19).assembleFrom(new FileInputStream(approachA));
    records2 = OneshotRecordParser.standard(ArincVersion.V19).assembleFrom(new FileInputStream(approachB));
    points = TrackParser.parse(trackA.getAbsolutePath());
  }

  @Tag("ASSIGNMENT")
  @Tag("INTEGRATION")
  @Test
  void test() {
    Procedure special = records.procedures().stream().findFirst().orElseThrow();
    Procedure rawAppB = records2.procedures().stream().findFirst().orElseThrow();

    Route enroute0 = plannedRoute0();
    Route enroute = plannedRoute1();

    Collection<Route> apprachA = ProcedureRoutesExtractor.INSTANCE.apply(special);
    Collection<Route> approachB = ProcedureRoutesExtractor.INSTANCE.apply(rawAppB);
    Collection<Route> enroutes = List.of(enroute, enroute0);
    Collection<Route> deps = departureAirport();
    Collection<Route> arrs = arrivalAirport();

    Collection<Route> allRoutes = Stream.of(deps, apprachA, approachB, enroutes, arrs)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
    List<Route> approaches = Stream.of(apprachA, approachB)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    RouteAssigner assigner = newAssigner(deps, enroutes, approaches, arrs);

    Map<ConformablePoint, FlyableLeg> assignments = assigner.assignments(points, allRoutes);
    //chops it to terminal area if u use that instant filter
    //assigner.assignments(points.stream().filter(p -> p.time().isAfter(Instant.ofEpochSecond(1686248315000L))).toList(), allRoutes);
    Map<FlyableLeg, List<LatLong>> byLeg = assignments.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .collect(Collectors.groupingBy(
            Map.Entry::getValue,
            Collectors.mapping(i -> i.getKey().latLong(), Collectors.toList())
        ));

    Route flownTransitionA = apprachA.stream().filter(i -> i.legs().get(0).associatedFix().get().fixIdentifier().equals("HBU")).findFirst().orElseThrow();

    List<FlyableLeg> assignedAirport = byLeg.keySet().stream().filter(i -> i.routes().stream().anyMatch(r -> Airport.class.isAssignableFrom(r.source().getClass()))).toList();
    List<FlyableLeg> assignedEnroute0 = byLeg.keySet().stream().filter(i -> i.routes().stream().findFirst().get().equals(enroute0)).toList();
    List<FlyableLeg> assignedEnroute = byLeg.keySet().stream().filter(i -> i.routes().stream().findFirst().get().equals(enroute)).toList();
    List<FlyableLeg> assignedApproach = byLeg.keySet().stream()
        .filter(i -> i.routes().contains(flownTransitionA))
        .toList();
    List<FlyableLeg> assignedApproachB = byLeg.keySet().stream().filter(i -> i.routes().stream().anyMatch(approachB::contains)).toList();

    Graph<FlyableLeg, DefaultWeightedEdge> graph = assigner.transitionGraph(allRoutes).graph();
    DOTExporter<FlyableLeg, DefaultWeightedEdge> exporter = new DOTExporter<>(this::signature);


    List<MapFeature> byLegFeatures = byLeg.entrySet().stream()
        .map(e -> {
          List<LatLong> latLongs = e.getValue();
          Color color = generateRandomColor();
          MapFeature currentLeg = MapFeatures.text(
              e.getKey().current().associatedFix().get().fixIdentifier() + e.getKey().current().pathTerminator(),
              e.getKey().current().associatedFix().get().latLong(),
              color
              );
          MapFeature path = MapFeatures.path(latLongs, color, 2f);
          return List.of(currentLeg, path);
        })
        .flatMap(Collection::stream)
        .toList();

    /*System.out.println(asDotGraph(exporter, graph));
    MapBuilder.newMapBuilder()
        .tileSource(new MapBoxApi(MapBoxApi.Style.LIGHT))
        //.width(Distance.ofNauticalMiles(40))
        .width(Distance.ofNauticalMiles(250))
        .addFeatures(byLegFeatures)
        //.center(LatLong.of(38.4401, -106.8233))
        .center(LatLong.of(39.11363444047107, -106.26381461591687))
        .toFile(new File("bigMap.jpg"));
    MapBuilder.newMapBuilder()
        .tileSource(new MapBoxApi(MapBoxApi.Style.LIGHT))
        .width(Distance.ofNauticalMiles(40))
        .addFeatures(byLegFeatures)
        .center(LatLong.of(38.4401, -106.8233))
        .toFile(new File("map.jpg"));*/

    assertAll(
        () -> assertEquals(13, byLeg.size()),
        () -> assertEquals(0, assignedApproachB.size()),
        () -> assertEquals(10, assignedApproach.size()),
        () -> assertEquals(0, assignedEnroute.size()),
        () -> assertEquals(2, assignedEnroute0.size()),
        () -> assertEquals(1, assignedAirport.size())
    );
  }

  private RouteAssigner newAssigner(Collection<Route> deps, Collection<Route> enroute, Collection<Route> approach, Collection<Route> arrArpt) {
    LinkingStrategy allPhases = new AllPhasesOfFlightLinker(
        deps,
        List.of(),
        List.of(),
        enroute,
        List.of(),
        List.of(),
        List.of(),
        approach,
        arrArpt);
    HybridHasher hybridHasher = HybridHasher.from(List.of(RouteHasher.newInstance(), PathTerminatorBasedLegHasher.newInstance()));
    CombinationStrategy combinationStrategy = new HashCombinationStrategy(hybridHasher);
    StandardLegFeatureScorer standardLegFeatureScorer = new StandardLegFeatureScorer();
    StandardLegFeatureExtractor standardLegFeatureExtractor = new StandardLegFeatureExtractor();
    StandardScoringStrategy scoringStrategy = StandardScoringStrategy.from(standardLegFeatureExtractor, standardLegFeatureScorer);
    StandardTransitionScorer standardTransitionScorer = new StandardTransitionScorer();

    return new RouteAssigner(
        allPhases,
        combinationStrategy,
        scoringStrategy,
        standardTransitionScorer
    );
  }
  public static Color generateRandomColor() {
    Random rand = new Random();
    int r = rand.nextInt(256); // 0 to 255 inclusive
    int g = rand.nextInt(256);
    int b = rand.nextInt(256);

    return new Color(r, g, b);
  }

  public static Collection<Route> arrivalAirport() {
    LatLong arrLat = LatLong.of(38.534, -106.933);
    Fix arr = Fix.builder().fixIdentifier("ARR_ARPT").latLong(arrLat).build();
    Leg l0 = Leg.ifBuilder(arr, 0).build();
    return List.of(Route.newRoute(List.of(l0), arrLat));
  }

  public static Collection<Route> departureAirport() {
    LatLong depArpt = LatLong.of(39.861666666666665, -104.67316666666667);
    Fix dep = Fix.builder().fixIdentifier("DEP_ARPT").latLong(depArpt).build();
    Leg l0 = Leg.ifBuilder(dep, 0).build();
    Airport leftHere = Airport.builder()
        .latLong(depArpt)
        .magneticVariation(MagneticVariation.ZERO)
        .runways(List.of())
        .airportIdentifier("DEP_ARPT")
        .build();

    LatLong depArptSame = LatLong.of(40.0, -40.0);
    Fix depSame = Fix.builder().fixIdentifier("SOME_OTHER").latLong(depArptSame).build();
    Leg l01 = Leg.ifBuilder(depSame, 0).build();
    Airport other = Airport.builder()
        .latLong(depArptSame)
        .magneticVariation(MagneticVariation.ZERO)
        .runways(List.of())
        .airportIdentifier("SOME_OTHER")
        .build();

    return List.of(Route.newRoute(List.of(l0), leftHere), Route.newRoute(List.of(l01), other));
  }

  public static Route plannedRoute0() {
    LatLong tailored = LatLong.of(39.11363444047107, -106.26381461591687);
    Fix f1 = Fix.builder().fixIdentifier("MRFIDK").latLong(tailored).build();
    LatLong hbu = LatLong.of(38.45211666666667, -107.03971388888888);
    Fix f2 = Fix.builder().fixIdentifier("HBU").latLong(hbu).build();
    LatLong tomac = LatLong.of(38.34666111111111, -106.77130833333334);
    Fix f3 = Fix.builder().fixIdentifier("TOMAC").latLong(tomac).build();
    LatLong dufle = LatLong.of(38.43278055555555, -106.65873888888889);
    Fix f4 = Fix.builder().fixIdentifier("DUFLE").latLong(dufle).build();
    Leg l1 = Leg.dfBuilder(f1, 10).build();
    Leg l2 = Leg.dfBuilder(f2, 20).build();
    Leg l3 = Leg.dfBuilder(f3, 30).build();
    Leg l4 = Leg.dfBuilder(f4, 40).build();
    List<Leg> legs = Arrays.asList(l1, l2, l3, l4);
    return Route.newRoute(legs, "FLIGHT_PLAN_0");
  }

  public static Route plannedRoute1() {
    LatLong tailored = LatLong.of(38.628310052628024, -106.97503535163497);
    Fix f1 = Fix.builder().fixIdentifier("HBU1234").latLong(tailored).build();
    LatLong hbu = LatLong.of(38.45211666666667, -107.03971388888888);
    Fix f2 = Fix.builder().fixIdentifier("HBU").latLong(hbu).build();
    LatLong tomac = LatLong.of(38.34666111111111, -106.77130833333334);
    Fix f3 = Fix.builder().fixIdentifier("TOMAC").latLong(tomac).build();
    LatLong dufle = LatLong.of(38.43278055555555, -106.65873888888889);
    Fix f4 = Fix.builder().fixIdentifier("DUFLE").latLong(dufle).build();
    Leg l1 = Leg.dfBuilder(f1, 10).build();
    Leg l2 = Leg.dfBuilder(f2, 20).build();
    Leg l3 = Leg.dfBuilder(f3, 30).build();
    Leg l4 = Leg.dfBuilder(f4, 40).build();
    List<Leg> legs = Arrays.asList(l1, l2, l3, l4);
    return Route.newRoute(legs, "FLIGHT_PLAN");
  }

  /**
   * Returns the structure of the graph in DOT notation. This can be rendered in a variety of viewers (e.g. webgraphviz).
   * @return the dot graph for making graphics of the procedure.
   */
  public String asDotGraph(DOTExporter<FlyableLeg, DefaultWeightedEdge> exporter, Graph<FlyableLeg, DefaultWeightedEdge> graph) {
    try (StringWriter writer = new StringWriter()) {
      exporter.exportGraph(graph, writer);
      return writer.toString();
    } catch (ExportException | IOException e) {
      throw new IllegalStateException("Error encountered exporting graph as DOT string.", e);
    }
  }

  static int number = 0;
  public String signature(FlyableLeg leg) {
    String sig = leg.current().pathTerminator() + "_" + leg.current().sequenceNumber() + "_" + leg.current().associatedFix().map(Fix::fixIdentifier).orElse("NONE");
    number++;
    return sig;
  }

}

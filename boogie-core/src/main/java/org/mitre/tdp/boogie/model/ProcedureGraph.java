package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.ExportException;
import org.jgrapht.nio.dot.DOTExporter;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * Represents a decorated {@link Procedure} record as a {@link SimpleDirectedGraph} allowing for the use of a variety of graph
 * algorithms on it based on JGrapht.
 * <br>
 * Most common uses are:
 * <br>
 * {@link DijkstraShortestPath} - to find the shortest path through the procedure between two legs
 * {@link ConnectivityInspector} - to determine whether the procedure is a single connected entity
 * <br>
 * {@link ProcedureGraph}s are build via {@link ProcedureFactory#newProcedureGraph(Procedure)}.
 */
public final class ProcedureGraph extends SimpleDirectedGraph<Leg, DefaultEdge> implements Procedure {

  private final transient Procedure procedure;

  private final transient DOTExporter<Leg, DefaultEdge> exporter;
  private final transient AllDirectedPaths<Leg, DefaultEdge> allDirectedPaths = new AllDirectedPaths<>(this);

  ProcedureGraph(Procedure procedure) {
    super(DefaultEdge.class);
    this.procedure = requireNonNull(procedure);
    this.exporter = new DOTExporter<>(this::enhancedSignature);
  }

  @Override
  public String procedureIdentifier() {
    return procedure.procedureIdentifier();
  }

  @Override
  public String airportIdentifier() {
    return procedure.airportIdentifier();
  }

  @Override
  public ProcedureType procedureType() {
    return procedure.procedureType();
  }

  @Override
  public RequiredNavigationEquipage requiredNavigationEquipage() {
    return procedure.requiredNavigationEquipage();
  }

  @Override
  public Collection<? extends Transition> transitions() {
    return procedure.transitions();
  }

  /**
   * Returns all paths through the procedure.
   * @param start from this starting leg.
   * @param end to this ending leg.
   * @return the possible ways through.
   */
  public List<List<Leg>> pathsBetween(Leg start, Leg end) {
    return allDirectedPaths.getAllPaths(start, end, false, 100).stream()
        .map(GraphPath::getVertexList)
        .toList();
  }

  /**
   * Returns a list of lists with all the ways through this procedure. Sid-Runway -> Sid-Enroute, Star-Enroute -> Star-Runway, and Approach -> Final -
   * @return the lists of all the paths
   */
  public List<List<Leg>> allPaths() {
    return Combinatorics.cartesianProduct(this.entryLegs((l)->true), this.exitLegs((l) -> true)).stream()
        .map(i -> this.pathsBetween(i.first(), i.second()))
        .flatMap(Collection::stream)
        .toList();
  }

  /**
   * Returns the structure of the graph in DOT notation. This can be rendered in a variety of viewers (e.g. webgraphviz).
   * @return the dot graph for making graphics of the procedure.
   */
  public String asDotGraph() {
    try (StringWriter writer = new StringWriter()) {
      exporter.exportGraph(this, writer);
      return writer.toString();
    } catch (ExportException | IOException e) {
      throw new IllegalStateException("Error encountered exporting graph as DOT string.", e);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProcedureGraph that = (ProcedureGraph) o;
    return Objects.equals(procedure, that.procedure);
  }

  @Override
  public int hashCode() {
    return Objects.hash(procedure);
  }

  @Override
  public String toString() {
    return procedure.toString();
  }

  private String legSignature(Leg leg) {
    return String.join("_", leg.pathTerminator().name(), leg.associatedFix().map(Fix::fixIdentifier).orElse(""));
  }

  private String enhancedSignature(Leg leg) {
    Optional<? extends Transition> transition = procedure.transitions().stream()
        .filter(i -> i.legs().contains(leg))
        .findFirst();
    String cat = transition
        .map(Transition::categoryOrTypes)
        .map(s -> s.stream().map(Enum::name).collect(Collectors.joining("")))
        .orElse("");
    String transitionIdent = transition
        .flatMap(Transition::transitionIdentifier)
        .orElse("");
    return String.join("_", legSignature(leg), cat, transitionIdent);
  }

  @Override
  public void accept(Visitor visitor) {
    procedure.accept(visitor);
  }
}

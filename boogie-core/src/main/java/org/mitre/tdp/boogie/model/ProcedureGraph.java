package org.mitre.tdp.boogie.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsUnmodifiableGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;

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
    this.exporter = new DOTExporter<>(this::legSignature);
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

  public List<List<Leg>> pathsBetween(Leg start, Leg end) {
    return allDirectedPaths.getAllPaths(start, end, false, 100)
        .stream().map(GraphPath::getVertexList).collect(Collectors.toList());
  }

  /**
   * Returns the structure of the graph in DOT notation. This can be rendered in a variety of viewers (e.g. webgraphviz).
   */
  public String asDotGraph() {
    try (StringWriter writer = new StringWriter()) {
      exporter.exportGraph(this, writer);
      return writer.toString();
    } catch (IOException e) {
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
    return leg.pathTerminator().name().concat(leg.associatedFix().map(Fix::fixIdentifier).orElse(""));
  }
}

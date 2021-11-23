package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Function;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.io.DOTExporter;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;

/**
 * Exporter which can be used to log information about the structure of the resolved graph.
 */
final class GraphExporter implements Function<SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge>, String> {

  static final GraphExporter INSTANCE = new GraphExporter();

  private GraphExporter() {
  }

  @Override
  public String apply(SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph) {
    requireNonNull(graph);
    DOTExporter<Leg, DefaultWeightedEdge> graphExporter = new DOTExporter<>(this::legSignature, null, e -> Double.toString(graph.getEdgeWeight(e)));

    try (StringWriter writer = new StringWriter()) {
      graphExporter.exportGraph(graph, writer);
      return writer.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException("Error while attempting to export graph.", e);
    }
  }

  /**
   * Generates a string signature for the provided leg which is valid in the DOT language:
   * <br>
   * An ID is one of the following:
   * <br>
   * Any string of alphabetic ([a-zA-Z\200-\377]) characters, underscores ('_') or digits([0-9]), not beginning with a digit;
   * a numeral [-]?(.[0-9]⁺ | [0-9]⁺(.[0-9]*)? );
   * any double-quoted string ("...") possibly containing escaped quotes (\")¹;
   * an HTML string (<...>).
   * <br>
   * Most fix identifiers will be valid (DOT) but lat/lon ones need to have the '/' replaced.
   */
  private String legSignature(Leg leg) {
    return String.format("F_%s__P_%s", leg.associatedFix().map(Fix::fixIdentifier).map(id -> id.replace("/", "")).orElse("NONE"), leg.pathTerminator().name());
  }
}

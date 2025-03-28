package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.nio.dot.DOTExporter;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;

/**
 * Exporter which can be used to log information about the structure of the resolved graph.
 */
final class GraphExporter implements Function<SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge>, String> {

  private static final VertexIdValidator VALIDATOR = new VertexIdValidator();

  static final GraphExporter INSTANCE = new GraphExporter();

  private GraphExporter() {
  }

  @Override
  public String apply(SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph) {
    requireNonNull(graph);
    DOTExporter<Leg, DefaultWeightedEdge> graphExporter = new DOTExporter<>(this::legSignature);

    try (StringWriter writer = new StringWriter()) {
      graphExporter.exportGraph(graph, writer);
      return writer.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException("Error while attempting to export graph.", e);
    }
  }

  /**
   * Generates a string signature for the provided leg which is valid in the DOT language:
   *
   * <p>Most fix identifiers will be valid (DOT) but lat/lon ones need to have the '/' or ',' replaced.
   */
  String legSignature(Leg leg) {
    return String.format("F_%s__P_%s",
        leg.associatedFix()
            .map(Fix::fixIdentifier)
            .map(id -> id.replace("/", ""))
            .map(id -> id.replace(",", ""))
            .filter(this::isValid)
            .orElse("UNKNOWN"),
        leg.pathTerminator().name()
    );
  }

  /**
   * Returns true of the given vertex ID should be considered valid for representation in a DOT graph.
   */
  boolean isValid(String vertexId) {
    return VALIDATOR.test(vertexId);
  }

  /**
   * Code taken from {@code DOTUtils.java} in JGraphT, used to check whether out generated graph vertex ID is valid.
   */
  private static final class VertexIdValidator implements Predicate<String> {

    private static final Pattern ALPHA_DIG = Pattern.compile("[a-zA-Z_][\\w]*");

    private static final Pattern DOUBLE_QUOTE = Pattern.compile("\".*\"");

    private static final Pattern DOT_NUMBER = Pattern.compile("[-]?([.][0-9]+|[0-9]+([.][0-9]*)?)");

    private static final Pattern HTML = Pattern.compile("<.*>");

    private VertexIdValidator() {
    }

    /**
     * Test if the ID candidate is a valid ID.
     *
     * @param idCandidate the ID candidate.
     * @return <code>true</code> if it is valid; <code>false</code> otherwise.
     */
    @Override
    public boolean test(String idCandidate) {
      return ALPHA_DIG.matcher(idCandidate).matches()
          || DOUBLE_QUOTE.matcher(idCandidate).matches()
          || DOT_NUMBER.matcher(idCandidate).matches() || HTML.matcher(idCandidate).matches();
    }
  }
}

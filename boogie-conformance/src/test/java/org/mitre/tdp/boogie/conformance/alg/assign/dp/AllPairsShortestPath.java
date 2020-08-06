package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import org.jblas.DoubleMatrix;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of the all-pairs shortest path algorithm leveraging the TDP {@link DynamicProgrammer}.
 */
public class AllPairsShortestPath<V extends Comparable<? super V>> {

    /**
     * The source graph we're using to define the set if candidate transitions and edge weights.
     */
    private final SimpleDirectedWeightedGraph<V, DefaultWeightedEdge> transitionGraph;
    /**
     * n x n matrix representing the best path scores for each of the vertices.
     */
    private final DoubleMatrix vertexMatrix;
    /**
     * The n-length ordered vertex list.
     *
     * The value at i,j in the vertex matrix should map to the shortest path between elements i and j respectively of this list.
     */
    private final List<V> vertices;

    public AllPairsShortestPath(
            SimpleDirectedWeightedGraph<V, DefaultWeightedEdge> transitionGraph) {
        this.transitionGraph = transitionGraph;
        this.vertices = transitionGraph.vertexSet().stream().sorted().collect(Collectors.toList());
        int width = transitionGraph.vertexSet().size();
        this.vertexMatrix = new DoubleMatrix(width, width);
    }

    /**
     * Builds the A^0 matrix based on the initial set of edge pairs present in the graph. Instead of using infinity as a placeholder
     * value when no edge exists substitute {@link Double#MAX_VALUE}.
     */
    public void initializeMatrix(SimpleDirectedWeightedGraph<V, DefaultWeightedEdge> graph) {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                if (i == j) {
                    vertexMatrix.put(i, j, 0);
                }
                if (graph.containsEdge(vertices.get(i), vertices.get(j))) {
                    vertexMatrix.put(i, j, graph.getEdgeWeight(graph.getEdge(vertices.get(i), vertices.get(j))));
                } else {
                    vertexMatrix.put(i, j, Double.MAX_VALUE);
                }
            }
        }
    }

    /**
     * Computes the ith matrix updating the {@link #vertexMatrix} with the computed values.
     */
    public void computeKthMatrix(int k) {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = 0; j < vertices.size(); j++) {
                vertexMatrix.put(i, j, computeNewMin(i, j, k));
            }
        }
    }

    /**
     * Performs the shortest path computation - iterating over the vertices in the vertex list and updating the vertexMatrix as
     * outlined in the video.
     */
    public void computeShortestPaths() {
        initializeMatrix(transitionGraph);
        IntStream.range(0, vertices.size()).forEach(this::computeKthMatrix);
    }


    public double computeNewMin(int a, int b, int c) {
        double directSum = vertexMatrix.get(a, b);
        double indirectSum = vertexMatrix.get(a, c) + vertexMatrix.get(c, b);
        if (directSum > indirectSum) {
            return indirectSum;
        }
        return directSum;
    }

    /**
     * Returns the shortest path length between the start and end vertices. Note post computation this is just the value at the
     * location in the matrix specified by the start and end vertices.
     */
    public Double shortestPathLength(V start, V end) {
        return vertexMatrix.get(vertices.indexOf(start), vertices.indexOf(end));
    }

    public DynamicProgrammer<V, ShortestPathState> dynamicProgrammer() {
        initializeMatrix(transitionGraph);

        List<ShortestPathState> shortestPathStates = new ArrayList<>();

        IntStream.range(0, vertices.size()).forEach(i ->
                IntStream.range(0, vertices.size()).forEach(j -> shortestPathStates.add(
                        new ShortestPathState(vertices.get(i), vertices.get(j)))));

        DynamicProgrammer<V, ShortestPathState> dp = new DynamicProgrammer<>(
                vertices,
                shortestPathStates,
                DynamicProgrammer.Optimization.MINIMIZE,
                DynamicProgrammer.ExecutionMode.DIFFERENTIAL);

        dp.optimizedStates();
        return dp;
    }

    public Double shortestPathLengthDp(V start, V end) {
        DynamicProgrammer<V, ShortestPathState> dynamicProgrammer = dynamicProgrammer();
        Map<ShortestPathState, DynamicProgrammer<V, ShortestPathState>.OptimizedState> optimizedStates = dynamicProgrammer.optimizedStates();
        for(Map.Entry<ShortestPathState, DynamicProgrammer<V, ShortestPathState>.OptimizedState> stateEntry: optimizedStates.entrySet()){
            if(stateEntry.getKey().source.equals(start) && stateEntry.getKey().target.equals(end)){
                return stateEntry.getValue().get(vertices.get(vertices.size()-1)).score();
            }
        }
        throw new RuntimeException();


    }

    public class ShortestPathState implements DynamicProgrammerState<V> {
        private V source;
        private V target;

        public ShortestPathState(V source, V target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public double getValue(V stage) {
            double result;
            if(source.equals(target)){ result = 0.0; }
            else {
                double direct = vertexMatrix.get(vertices.indexOf(source), vertices.indexOf(target));
                double indirect = vertexMatrix.get(vertices.indexOf(source), vertices.indexOf(stage)) + vertexMatrix.get(vertices.indexOf(stage), vertices.indexOf(target));
                if (indirect < direct) { result = indirect; }
                else { result = direct; }
            }
            vertexMatrix.put(vertices.indexOf(source), vertices.indexOf(target), result);
            return result;
        }

        @Override
        public List<? extends DynamicProgrammerTransition> getPossibleTransitions(V stage) {
            return Collections.singletonList(new ShortestPathTransition(this));
        }
    }

    public class ShortestPathTransition implements DynamicProgrammerTransition<V, ShortestPathState> {

        private final ShortestPathState state;

        public ShortestPathTransition(ShortestPathState state) {
            this.state = state;
        }

        @Override
        public ShortestPathState getTransition() {
            return state;
        }

        @Override
        public Double getTransitionProbability() {
            return 1.0;
        }
    }
}

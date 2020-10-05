package edu.brown.cs.student.PageRank;

/**
 * The interface Edge represents a generic edge to be used in Dijkstra's—it has accessors and
 * mutators to its weight, its source vertex, and its end vertex.
 *
 * @param <V> A generic vertex
 * @param <E> A generic edge
 */
public interface Edge<V extends Vertex<V, E>, E extends Edge<V, E>> {

  /**
   * Edge objects must have a source vertex.
   *
   * @return the source vertex
   */
  V getSource();

  /**
   * Edge objects must have a dest vertex.
   *
   * @return the dest vertex
   */
  V getDest();

}

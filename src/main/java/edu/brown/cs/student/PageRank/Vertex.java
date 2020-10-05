package edu.brown.cs.student.PageRank;

import java.util.Set;

/**
 * The interface Vertex represents a generic vertex to be used in Dijkstra's—it has accessors for
 * its outgoing edges and its unique identifier.
 *
 * @param <V> a generic Vertex.
 * @param <E> a generic Edge.
 */
public interface Vertex<V extends Vertex<V, E>, E extends Edge<V, E>> {

  /**
   * Accessor for incoming edges extending from the Vertex object.
   *
   * @return the object's incoming edges.
   */
  Set<E> getIncomingEdges();

  /**
   * Accessor for outgoing edges extending from the Vertex object.
   *
   * @return the object's outgoing edges.
   */
  Set<E> getOutgoingEdges();

  /**
   * Accessor for the unique identifier of the Vertex object.
   *
   * @return the ID of the vertex.
   */
  String getID();

}

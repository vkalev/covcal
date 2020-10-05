package edu.brown.cs.student.PageRank;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Page Rank class that makes ranking calculations given a graph.
 * @param <V> - type of Vertex
 * @param <E> - type of Edge
 */
public class PageRank<V extends Vertex, E extends Edge> {
  private Collection<V> vertices;
  private Map<V, Double> vertsToRanks;
  private Map<V, Double> vertsToRanksInter;
  private static final double DAMPING_FACTOR = 0.85;
  private static final int MAX_ITERATIONS = 100;
  private static final double ERROR = 0.01;

  private List<V> sinks;
  private Boolean valid;


  /**
   * The main method that does the calculation.
   * @param g - collection of Vertex instances
   * @return A Map of every Vertex to its corresponding rank
   */
  public Map<V, Double> calcPageRank(Collection<V> g) {
    //this instance variable is used to stop the main while loop if the pagerank values converge
    valid = true;

    vertices = g;
    sinks = new ArrayList<V>();
    vertsToRanks = new HashMap<V, Double>();

    //this is an "intermediate" map that is used in the calculations of pageRank
    vertsToRanksInter = new HashMap<V, Double>();

    //finds and creates an arraylist of sink vertices
    findSinks();
    //sets the initial pageRank of each vertex to 1/|numOfVertices|
    setFirstPageRank();

    //counts the number of iterations
    int iteration = 0;

    while (iteration < MAX_ITERATIONS && valid) {
      iteration++;
      handleSinks();
      //main calculations for the exchange of pageRank between vertices
      exchangePageRank();
      //checks if the pageRank of the vertices is beginning to converge
      checkConvergence();
      //copies the pageRank from the _vertToRanksInter map to the _vertToRanks map
      movePageRank();
    }
    return vertsToRanks;
  }


  /**
   * This method is used to check if the pageRank of the vertices are all converging
   * onto single values, if for each vertex its pageRank is converging, then the method
   * changes the _valid instance variable to false and the entire algorithm will terminate.
   */
  private void checkConvergence() {
    int counter = 0;
    /*
    for each vertex, check if the current pageRank and previous is different _error
    amount or less
     */
    for (V vertex: vertices) {
      if (Math.abs(vertsToRanks.get(vertex) - vertsToRanksInter.get(vertex)) <= ERROR) {
        counter++;
      }
    }
    /*
      if each vertex's  current pageRank and previous is different _error amount,
      then set _valid to false
     */
    if (counter == vertices.size()) {
      valid = false;
    }
  }

  /**
   * This method copies the pageRank from the _vertsToRanksInter map to the _vertsToRanks map
   * The method then resets the _vertsToRanksInter map by setting each pageRank back to 0.
   */
  private void movePageRank() {
    for (V vertex:vertices) {
      vertsToRanks.put(vertex, vertsToRanksInter.get(vertex));
      vertsToRanksInter.put(vertex, 0.0);
    }
  }

  /**
   * This is the main method where most of the pageRank algorithm calculations take place
   * between the vertices. This method takes care of the calculation for the exchange of
   * PageRank between all of the vertices and their incoming vertices.
   */
  private void exchangePageRank() {
    //for each vertex
    for (V vertex: vertices) {
      //create a list of all incoming edges (happens for each vertex)
      Set<E> incomingEdges = vertex.getIncomingEdges();
      //create a list of all vertices attached to incoming edges (happens for each vertex)
      List<V> incomingVerticies = new ArrayList<>();
      //move all of the vertices attached to incoming edges (happens for each vertex) into list
      for (E edge: incomingEdges) {
        incomingVerticies.add((V) edge.getSource());
      }
      //initial constant value that each vertex's pageRank starts with due to "evaporation".
      Double pageRank = (1 - DAMPING_FACTOR) / ((double) vertices.size());

      //for each incoming vertex
      for (int j = 0; j < incomingVerticies.size(); j++) {
        Set<E> outgoingEdgesU = incomingVerticies.get(j).getOutgoingEdges();
        Integer numOfOutU = outgoingEdgesU.size();
        pageRank = pageRank + (DAMPING_FACTOR)
                * (vertsToRanks.get(incomingVerticies.get(j)) / numOfOutU);
      }
      //save pageRank to intermediate _vertsToRanksInter map
      vertsToRanksInter.put(vertex, pageRank + vertsToRanksInter.get(vertex));
    }
  }

  /**
   * This method initializes the starting pageRank for each vertex to 1/|numOfVertices|.
   */
  private void setFirstPageRank() {
    Double startingPageRank = 1 / (double) (vertices.size());
    for (V vertex: vertices) {
      vertsToRanks.put(vertex, startingPageRank);
    }
  }

  /**
   * This method checks all vertices for sink and appends all sinks to
   * the _sinks arraylist.
   */
  private void findSinks() {
    //for each vertex
    for (V vertex:vertices) {
      //if vertex doesnt have any outgoing edges, then add it to the _sink list
      if (vertex.getOutgoingEdges().isEmpty()) {
        sinks.add(vertex);
      }
    }
  }

  /**
   * Method used to account for sink pages (those with no outgoing
   * edges).
   */
  private void handleSinks() {
    Double totalSinkRank = 0.0;
    //for each sink, sum together al of their pageRanks * dampingFactor
    for (int j = 0; j < sinks.size(); j++) {
      totalSinkRank = totalSinkRank + (DAMPING_FACTOR) * vertsToRanks.get(sinks.get(j));

    }
    //for each vertex, set pageRank to the totalSinkRank/|numOfVertices|
    for (V vertex:vertices) {
      Double updatedRank = (totalSinkRank / ((double) vertices.size()));
      vertsToRanksInter.put(vertex, updatedRank);
    }
  }
}

package edu.brown.cs.student.Organization;

import edu.brown.cs.student.PageRank.Edge;
import edu.brown.cs.student.PageRank.PageRank;
import edu.brown.cs.student.PageRank.Vertex;
import org.javatuples.Pair;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;

/**
 * This class models a rank-calculator which determines the ranking of each
 * employee for an organization.
 */
public class RankCalculator {
  private int numEdges;
  private OrganizationProxy proxy;
  private final double negHalf = -.5;
  private final double sixFifths = 1.2;

  /**
   * Constructor.
   * @param proxy - the proxy to the org.
   */
  public RankCalculator(OrganizationProxy proxy) {
    if (proxy == null) {
      throw new NullPointerException("Proxy is null value");
    }
    this.proxy = proxy;
    this.numEdges = 0;

  }

  /**
   * Returns the PageRank based ordering of the workers.
   * @param empIds - list of employee IDs
   * @return - ordered list from highest rank to lowest
   * @throws SQLException - throws any SQL query exceptions
   */
  public List<Worker> getOrdering(List<String> empIds) throws SQLException {
    Collection<Worker> workers = createWorkers(empIds);
    Map<Worker, Double> rankMap = new PageRank<Worker, ShiftCover>().calcPageRank(workers);
    double mean = calcMean(rankMap);
    double stdev = calcStdev(rankMap, mean);
    int totalEdges = 0;
    for (Worker w:rankMap.keySet()) {
      totalEdges += w.getOutgoingEdges().size();
    }
    for (Worker w:rankMap.keySet()) {
      double score;
      if (totalEdges == 0 || w.getIncomingEdges().size() == 0) {
        score = -1;
      } else {
        //normalize scores
        score = (rankMap.get(w) - mean) / stdev;
      }
          //if score is .5 stdev below or more = rank of -1
      if (score <= negHalf) {
        w.setRank(-1);
        //if score is below mean = rank of 0
      } else if (score  <= 0) {
        w.setRank(0);
        //if score is between 1 stdev above mean =  rank of 1
      } else if (score > 0 && score < 1) {
        w.setRank(1);
        //if score is between 1 and 1.75 stdev above mean = rank of 2
      } else if (score >= 1 && score <= sixFifths) {
        w.setRank(2);
      } else {
        //if score is above 1.75 stdev above mean = rank of 3
        w.setRank(3);
      }
    }
    List<Worker> orderedWorkerList = new ArrayList<>();
    orderedWorkerList.addAll(rankMap.keySet());
    orderedWorkerList.sort(new WorkerComparator(rankMap));
    return orderedWorkerList;
  }

  private double calcStdev(Map<Worker, Double> rankMap, double mean) {
    double stdev = 0;
    for (Worker w: rankMap.keySet()) {
      stdev += (rankMap.get(w) - mean) * (rankMap.get(w) - mean);
    }
    if (rankMap.size() == 0) {
      return 0;
    }
    return Math.sqrt(stdev / rankMap.size());
  }

  private double calcMean(Map<Worker, Double> rankMap) {
    double mean = 0;
    for (Worker w: rankMap.keySet()) {
      mean += rankMap.get(w);
    }
    if (rankMap.size() == 0) {
      return 0;
    }
    return (mean / rankMap.size());
  }


  /**
   * This is a comparator class that is used to order the list
   * based on their PageRank values.
   */
  private class WorkerComparator implements Comparator<Worker> {
    private Map<Worker, Double> rankMap;

    /**
     * Constructor.
     * @param rankMap - maps worker to it's rank value
     */
    WorkerComparator(Map<Worker, Double> rankMap) {
      this.rankMap = rankMap;
    }

    @Override
    public int compare(Worker o1, Worker o2) {
      return Double.compare(this.rankMap.get(o2), this.rankMap.get(o1));
    }
  }

  private Collection<Worker> createWorkers(List<String> allEmployeeIDs) throws SQLException {
    Map<String, Worker> workers = new HashMap();
    for (String workerID: allEmployeeIDs) {
      workers.put(workerID, new Worker(workerID));
    }
    for (Pair<String, String> coverageInst:this.proxy.getCoverageLedger()) {
      Worker recipient = workers.get(coverageInst.getValue0());
      Worker donor = workers.get(coverageInst.getValue1());
      new ShiftCover(recipient, donor);
    }
    return workers.values();
  }

  /**
   * Dummy class that represents edge for PageRank calculation.
   * In the context of CovCal it represents a shift being covered.
   */
  public class ShiftCover implements Edge<Worker, ShiftCover> {
    private Worker src;
    private Worker dest;

    /**
     * Constructor.
     * @param src - worker recipient getting covered
     * @param dest - worker donor or "coverer"
     */
    public ShiftCover(Worker src, Worker dest) {
      this.src = src;
      this.dest = dest;
      src.addOutgoingEdge(this);
      dest.addIncomingEdge(this);
      RankCalculator.this.numEdges++;
    }

    @Override
    public Worker getSource() {
      return src;
    }

    @Override
    public Worker getDest() {
      return dest;
    }
  }

  /**
   * Dummy worker class that represents a vertex for PageRank
   * calculation. In the context of CovCal it is just an employee.
   */
  public class Worker implements Vertex<Worker, ShiftCover> {

    private Set<ShiftCover> incomingEdges = new HashSet<>();
    private Set<ShiftCover> outgoingEdges = new HashSet<>();
    private String id;
    private int rank = 0;

    /**
     * Constructor.
     * @param id - Unique employee ID
     */
    public Worker(String id) {
      this.id = id;
    }

    /**
     * Sets the rank value for the worker.
     * @param rank - double: rank value
     */
    public void setRank(int rank) {
      this.rank = rank;
    }

    /**
     * Gets the rank value for the worker.
     * @return - double: rank value
     */
    public int getRank() {
      return this.rank;
    }
    @Override
    public Set<ShiftCover> getIncomingEdges() {
      return this.incomingEdges;
    }

    @Override
    public Set<ShiftCover> getOutgoingEdges() {
      return this.outgoingEdges;
    }

    @Override
    public String getID() {
      return this.id;
    }

    /**
     * This adds a shiftCover to the outgoing edge set.
     * @param shiftCover - instance of the ShiftCover
     */
    public void addOutgoingEdge(ShiftCover shiftCover) {
      this.outgoingEdges.add(shiftCover);
    }

    /**
     * This adds a shiftCover to the incoming edge set.
     * @param shiftCover - instance of the ShiftCover
     */
    public void addIncomingEdge(ShiftCover shiftCover) {
      this.incomingEdges.add(shiftCover);
    }
  }

}



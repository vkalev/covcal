package edu.brown.cs.student;

import edu.brown.cs.student.PageRank.Edge;
import edu.brown.cs.student.PageRank.PageRank;
import edu.brown.cs.student.PageRank.Vertex;
import org.junit.Test;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PageRankTest {

  private class Node implements Vertex<Node,Link> {
    private Set<Link> incomingEdges = new HashSet<>();
    private Set<Link> outgoingEdges = new HashSet<>();
    private String id;

    public Node(String id) {
      this.id = id;
    }

    @Override
    public Set<Link> getIncomingEdges() {
      return this.incomingEdges;
    }

    @Override
    public Set<Link> getOutgoingEdges() {
      return this.outgoingEdges;
    }

    @Override
    public String getID() {
      return this.id;
    }

    public void addIncomingEdge(Link link) {
      this.incomingEdges.add(link);
    }

    public void addOutgoingEdge(Link link) {
      this.outgoingEdges.add(link);
    }

  }

  private class Link implements Edge<Node,Link> {
    private Node source;
    private Node dest;

    public Link (Node src, Node dest) {
      this.source = src;
      this.dest = dest;
      src.addOutgoingEdge(this);
      dest.addIncomingEdge(this);
    }

    @Override
    public Node getSource() {
      return this.source;
    }

    @Override
    public Node getDest() {
      return this.dest;
    }

  }

  // This is your margin of error for testing
  double _epsilon = 0.03;

  /**
   * A simple test with four pages. Each page only has one
   * outgoing link to a different page, resulting in a square
   * shape or cycle when visualized. The pages' total ranks is 1.
   */
  @Test
  public void testFourEqualRanks() {
    Set<Node> nodes = new HashSet<>();
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");
    Node d = new Node("D");
    Link e0 = new Link(a,b);
    Link e1 = new Link(b,c);
    Link e2 = new Link(c,d);
    Link e3 = new Link(d,a);
    nodes.add(a);
    nodes.add(b);
    nodes.add(c);
    nodes.add(d);
    PageRank<Node,Link> pageRank = new PageRank<>();

    Map<Node, Double> output = pageRank.calcPageRank(nodes);

    assertEquals(output.size(), 4);
    double total = 0;
    for (double rank: output.values()) {
      total += rank;
    }

    double expectedRankA = 0.25;
    double expectedRankB = 0.25;
    double expectedRankC = 0.25;
    double expectedRankD = 0.25;

    assertEquals(total, 1, _epsilon);

    assertEquals(output.get(a), expectedRankA, _epsilon);
    assertEquals(output.get(b), expectedRankB, _epsilon);
    assertEquals(output.get(c), expectedRankC, _epsilon);
    assertEquals(output.get(d), expectedRankD, _epsilon);

  }

  /**
   * A simple test with three pages. Note that vertex A's
   * rank is not 0 even though it has no incoming edges,
   * demonstrating the effects of the damping factor and handling sinks.
   */
  @Test
  public void simpleTestOne() {
    Set<Node> nodes = new HashSet<>();
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");

    Link e0 = new Link(a,b);
    Link e1 = new Link(b,c);

    nodes.add(a);
    nodes.add(b);
    nodes.add(c);

    PageRank<Node,Link> pageRank = new PageRank<>();
    Map<Node, Double> output = pageRank.calcPageRank(nodes);

    assertEquals(output.size(), 3);
    double total = 0;
    for (double rank: output.values()) {
      total += rank;
    }

    double expectedRankA = 0.186;
    double expectedRankB = 0.342;
    double expectedRankC = 0.471;

    assertEquals(total, 1, _epsilon);
    assertEquals(output.get(a), expectedRankA, _epsilon);
    assertEquals(output.get(b), expectedRankB, _epsilon);
    assertEquals(output.get(c), expectedRankC, _epsilon);

  }

  /**
   * Tests the functionality of sink handling
   */
  @Test
  public void sinkTest(){
    Set<Node> nodes = new HashSet<>();
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");
    Link e0 = new Link(a,b);
    Link e1 = new Link(c,b);
    nodes.add(a);
    nodes.add(b);
    nodes.add(c);
    PageRank<Node,Link> pageRank = new PageRank<>();

    Map<Node, Double> output = pageRank.calcPageRank(nodes);

    double total = 0;
    for (double rank: output.values()) {
      total += rank;
    }
    assertEquals(total,1,_epsilon);
    assertEquals(output.get(a),output.get(c),_epsilon);
    assertTrue(output.get(b) > output.get(a));
    assertTrue(output.get(b) > output.get(a));

  }

  /**
   * Tests the functionality of a singleNode in the graph
   */
  @Test
  public void singleNodeTest(){
    Set<Node> nodes = new HashSet<>();
    Node a = new Node("A");
    nodes.add(a);
    PageRank<Node,Link> pageRank = new PageRank<>();

    Map<Node, Double> output = pageRank.calcPageRank(nodes);

    assertEquals(output.get(a),1,_epsilon);

  }

  /**
   * This method tests that the pageRank algorithm works for a disconnected graph
   */
  @Test
  public void disconnectedGraphTest(){
    Set<Node> nodes = new HashSet<>();
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");
    Node d = new Node("D");
    Node e = new Node("E");
    Link e0 = new Link(a,b);
    Link e1 = new Link(c,b);
    Link e2 = new Link(e,d);
    nodes.add(a);
    nodes.add(b);
    nodes.add(c);
    nodes.add(d);
    nodes.add(e);
    PageRank<Node,Link> pageRank = new PageRank<>();

    Map<Node, Double> output = pageRank.calcPageRank(nodes);

    double total = 0;
    for (double rank: output.values()) {
      total += rank;
    }
    assertEquals(total,1,_epsilon);
    assertTrue(output.get(b) > output.get(a));
    assertTrue(output.get(b) > output.get(c));
    assertTrue(output.get(d) > output.get(e));

  }

  /**
   * This tests the functionality of the pageRank algorithm on a graph with only nodes
   */
  @Test
  public void onlyNodesTest(){
    Set<Node> nodes = new HashSet<>();
    Node a = new Node("A");
    Node b = new Node("B");
    Node c = new Node("C");
    nodes.add(a);
    nodes.add(b);
    nodes.add(c);;
    PageRank<Node,Link> pageRank = new PageRank<>();

    Map<Node, Double> output = pageRank.calcPageRank(nodes);


    double total = 0;
    for (double rank: output.values()) {
      total += rank;
    }

    assertEquals(total,1,_epsilon);
    assertEquals(output.get(a),output.get(b),_epsilon);
    assertEquals(output.get(a),output.get(c),_epsilon);
    assertEquals(output.get(b),output.get(c),_epsilon);

  }

  @Test
  public void emptyGraph(){
    Set<Node> nodes = new HashSet<>();
    PageRank<Node,Link> pageRank = new PageRank<>();
    Map<Node, Double> output = pageRank.calcPageRank(nodes);
    assertEquals(output.size(),0);
  }

}

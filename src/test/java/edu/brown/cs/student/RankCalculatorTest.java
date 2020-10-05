package edu.brown.cs.student;

import edu.brown.cs.student.CovCal.GetQuery;
import edu.brown.cs.student.CovCal.Proxy;
import edu.brown.cs.student.Organization.Employee;
import edu.brown.cs.student.Organization.Organization;
import edu.brown.cs.student.Organization.RankCalculator;
import edu.brown.cs.student.Organization.ShiftRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;


public class RankCalculatorTest {
  private Proxy _proxy;
  private Map<Integer, Organization> _orgs;


  @Before
  public void setUpConn() {
    try {
      this._proxy = new Proxy("data/covcal_test_rankcalculator.db");
      this._orgs = this._proxy.getAllOrgs(true);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      this._proxy = null;
    }
  }

  @After
  public void tearDown() {

    this._orgs = null;

  }

  @Test
  public void correctOrder() {
    Organization org = this._orgs.get(23456);
    Set<String> employeeIDs = org.getEmployeeMap().keySet();
    RankCalculator rankCalculator = new RankCalculator(org.getProxy());
    List<String> employeeIDsList = new ArrayList<String>(employeeIDs);
    List<RankCalculator.Worker> orderedResult = new ArrayList<>();
    try {
      orderedResult = rankCalculator.getOrdering(employeeIDsList);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assert (orderedResult.size() == 4);
    assert (orderedResult.get(0).getID().equals("1"));
    assert (orderedResult.get(1).getID().equals("5"));
    assert (orderedResult.get(2).getID().equals("4"));
    assert (orderedResult.get(3).getID().equals("3"));
  }

  @Test
  public void correctRanks() {
    Organization org = this._orgs.get(23456);
    Set<String> employeeIDs = org.getEmployeeMap().keySet();
    RankCalculator rankCalculator = new RankCalculator(org.getProxy());
    List<String> employeeIDsList = new ArrayList<String>(employeeIDs);
    List<RankCalculator.Worker> orderedResult = new ArrayList<>();
    try {
      orderedResult = rankCalculator.getOrdering(employeeIDsList);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    assert (orderedResult.size() == 4);
    assert (orderedResult.get(0).getRank() == 2);
    assert (orderedResult.get(1).getRank() == 1);
    assert (orderedResult.get(2).getRank() == 0);
    assert (orderedResult.get(3).getRank() == -1);
  }

  @Test
  public void noLedgerData() {
    Organization org = this._orgs.get(12345);
    Set<String> employeeIDs = org.getEmployeeMap().keySet();
    RankCalculator rankCalculator = new RankCalculator(org.getProxy());
    List<String> employeeIDsList = new ArrayList<String>(employeeIDs);
    List<RankCalculator.Worker> orderedResult = new ArrayList<>();
    try {
      orderedResult = rankCalculator.getOrdering(employeeIDsList);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    for (RankCalculator.Worker w: orderedResult) {
      assert (w.getRank() == -1);
    }
  }

  @Test public void nullProxy() {
    assertThrows(NullPointerException.class, () -> {
      new RankCalculator(null);
    });
  }

}



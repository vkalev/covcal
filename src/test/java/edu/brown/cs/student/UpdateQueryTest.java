package edu.brown.cs.student;

import edu.brown.cs.student.CovCal.GetQuery;
import edu.brown.cs.student.CovCal.Proxy;
import edu.brown.cs.student.CovCal.UpdateQuery;
import edu.brown.cs.student.Organization.Employee;
import edu.brown.cs.student.Organization.Organization;
import edu.brown.cs.student.Organization.ShiftRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class UpdateQueryTest {
  private Proxy _proxy;
  private UpdateQuery _updateQuery;
  private GetQuery _getQuery;
  private Connection conn;

  @Before
  public void setUpConn() throws SQLException, ClassNotFoundException {
      _proxy = new Proxy("data/covcal_test_updateQuery.db");
      _updateQuery = _proxy.getUpdateQuery();
      _getQuery = _proxy.getGetQuery();
      conn = _proxy.getConn();
  }

  @After
  public void tearDown() throws SQLException {
    _proxy.closeConn();
    _getQuery = null;
    _updateQuery = null;
    conn = null;
    _proxy = null;
  }

  @Test
  public void newEmpResponseTest() throws SQLException {
    _updateQuery.addShiftRespNewEmp("1", new ArrayList<>(Arrays.asList(1, 2, 3)));
    // Check that there are three new shift responses with this
    PreparedStatement prep = conn.prepareStatement(
        "SELECT * FROM shift_responses WHERE emp_id = '1'");
    ResultSet rs = prep.executeQuery();
    int testInt = 1;
    while (rs.next()) {
      assertEquals(testInt, rs.getInt(3));
      assertEquals("1", rs.getString(2));
      testInt++;
    }
    rs.close();

    _updateQuery.deleteResponsesLeavingEmp(1, "1");
    rs = prep.executeQuery();
    assertFalse(rs.next());
    rs.close();
  }

  @Test
  public void newEmpRequestTest() throws SQLException {
    _updateQuery.registerNewShiftInShifts("2", 2, new Date(2021), 5, 6);
    PreparedStatement prep = conn.prepareStatement(
        "SELECT * FROM shifts WHERE createdBy = '2'");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      assertEquals(2, rs.getInt(2));
      assertEquals(new Date(2021), rs.getDate(3));
      assertEquals(5, rs.getInt(4));
      assertEquals(6, rs.getInt(5));
    }
    rs.close();

    _updateQuery.deleteRequestsLeavingEmp(2, "2");
    rs = prep.executeQuery();
    assertFalse(rs.next());
    rs.close();

    _updateQuery.registerNewShiftInShifts("2", 2, new Date(2021), 5, 6);
    // Delete the passed shift
    _updateQuery.deletePassedRequests(2);
    prep = conn.prepareStatement(
        "SELECT * FROM shifts");
    rs = prep.executeQuery();
    while (rs.next()) {
      assertEquals(rs.getString(9), "1");
    }

  }


  @Test
  public void changeOwnerTest() throws SQLException, InterruptedException {
    _updateQuery.changeGroupOwner(1, "2");
    assertTrue(_getQuery.isUserOwner(1, "2"));
    _updateQuery.changeGroupOwner(1, "1");
    assertTrue(_getQuery.isUserOwner(1, "1"));
  }

  @Test
  public void deleteOrgFromLedgerTest() throws SQLException {
    _updateQuery.addToLedger(1, "1");
    ResultSet rs = _getQuery.getCoverageLedger(1);
    rs.next();
    assertEquals(rs.getInt(1), 1);
    assertEquals(rs.getString(3), "1");

    _updateQuery.addToLedger(2, "1");
    _updateQuery.addToLedger(4, "1");

    _updateQuery.deleteOrgFromLedger(1);
    rs = _getQuery.getCoverageLedger(2);
    assertTrue(rs.next());
    assertEquals(rs.getInt(1), 2);
    rs = _getQuery.getCoverageLedger(1);
    assertFalse(rs.next());
    _updateQuery.deleteOrgFromLedger(2);
  }

  @Test
  public void deleteOrgResponseTest() throws SQLException {
    _updateQuery.addShiftRespNewEmp("1", new ArrayList<>(Arrays.asList(1, 2, 3, 4)));

    _updateQuery.updateResponseToRequest("1", 2, "testUpdate");
    PreparedStatement prep = conn.prepareStatement(
        "SELECT * FROM shift_responses WHERE shift_id = 2");
    ResultSet rs = prep.executeQuery();
    assertEquals(rs.getString(4), "testUpdate");

    _updateQuery.deleteOrgFromResponses(1);

    prep = conn.prepareStatement(
        "SELECT * FROM shift_responses");
    rs = prep.executeQuery();
    assertTrue(rs.next());
    assertEquals(rs.getInt(3), 4);
    assertFalse(rs.next());

    _updateQuery.deleteOrgFromResponses(2);
  }

  @Test
  public void deleteOrgRequestTest() throws SQLException {
    _updateQuery.registerNewShiftInShifts("2", 3, new Date(20210420), 5, 6);
    _updateQuery.registerNewShiftInShifts("2", 3, new Date(20210420), 5, 6);
    _updateQuery.deleteOrgFromRequests(3);

    PreparedStatement prep = conn.prepareStatement(
        "SELECT * FROM shifts");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      assertNotEquals(rs.getInt(2), 3);
    }
  }

  @Test
  public void createAndDeleteOrgTest() throws SQLException {
    _updateQuery.addOrganization(99, "test org", null, "1", null, "");
    Map<Integer, Organization> orgs = _getQuery.getAllOrgs(false);
    assertEquals(orgs.size(), 4);

    _updateQuery.deleteOrgFromOrganizations(99);
    orgs = _getQuery.getAllOrgs(false);
    assertEquals(orgs.size(), 3);
  }

  @Test
  public void addNewUserTest() throws SQLException {
    _updateQuery.addNewUser("99", "email", "test name", "");
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM users WHERE id = '99'");
    ResultSet rs = prep.executeQuery();
    assertTrue(rs.next());
    assertFalse(rs.next());

    prep = conn.prepareStatement("DELETE FROM users WHERE id = '99'");
    prep.executeUpdate();
  }

  @Test
  public void initializeResponsesTest() throws SQLException {
    _updateQuery.initializeResponses(
        new ArrayList<>(Arrays.asList("1", "2")), 2, "1", "decline");
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM shift_responses");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      assertEquals(rs.getString(4), "decline");
      assertEquals(rs.getInt(3), 2);
    }

    prep = conn.prepareStatement("DELETE FROM shift_responses");
    prep.executeUpdate();
  }

  @Test
  public void updateOrgTest() throws SQLException {
    _updateQuery.updateOrg(2, "test name", "test type", null, "desc");
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM organizations WHERE id = 2");
    ResultSet rs = prep.executeQuery();
    assertEquals(rs.getString(2), "test name");
    assertEquals(rs.getString(3), "test type");
    assertEquals(rs.getString(7), "desc");

    _updateQuery.updateOrg(2, "name2", "type2", null, "desc2");
    rs = prep.executeQuery();
    assertEquals(rs.getString(2), "name2");
    assertEquals(rs.getString(3), "type2");
    assertEquals(rs.getString(7), "desc2");
  }

  @Test
  public void updateUserTest() throws SQLException {
    _updateQuery.updateUserImage("2", "image1");
    _updateQuery.updateUserName("2", "name1", "desc1");
    String[] user = _getQuery.getUser("2");
    assertEquals(user[3], "image1");
    assertEquals(user[1], "name1");
    assertEquals(user[4], "desc1");

    _updateQuery.updateUserImage("2", "image2");
    _updateQuery.updateUserName("2", "name2", "desc2");
    user = _getQuery.getUser("2");
    assertEquals(user[3], "image2");
    assertEquals(user[1], "name2");
    assertEquals(user[4], "desc2");
  }

}










package edu.brown.cs.student.CovCal;

import edu.brown.cs.student.Organization.Employee;
import edu.brown.cs.student.Organization.Organization;
import edu.brown.cs.student.Organization.ShiftRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

/**
 * Class representing a Query that executes queries to get information from the
 * connected CovCal database.
 */
public class GetQuery {
  private Connection conn;
  private Proxy proxy;
  private static final int MAX_GROUP_ID = 99999;
  private static final int MIN_GROUP_ID = 10000;
  private static final int LAST_INDEX_ORG = 7;

  /**
   * Constructor for a GetQuery object that sets the connection to the CovCal
   * database and the Proxy associated with it.
   * @param conn - the connection to the CovCal database
   * @param proxy - the proxy associated with this GetQuery instance
   */
  public GetQuery(Connection conn, Proxy proxy) {
    this.conn = conn;
    this.proxy = proxy;
  }

  /**
   * Query to determine whether a user is part of an employee group or not.
   * @param orgID - ID of the employee group
   * @param empID - ID of the user
   * @return true if the user is currently in the employee group, false otherwise
   * @throws SQLException when error reading database
   */
  public boolean employeeExistsInOrg(int orgID, String empID) throws SQLException {
    PreparedStatement prep =
            conn.prepareStatement("SELECT * FROM employment WHERE orgId = ? AND empId = ?");
    prep.setInt(1, orgID);
    prep.setString(2, empID);
    ResultSet rs = prep.executeQuery();
    return rs.next();
  }

  /**
   * Query to determine whether an employee group exists or not.
   * @param orgID - ID of employee group
   * @return true if there exists a group with ID of orgID, false otherwise
   * @throws SQLException when error reading database
   */
  public boolean organizationExists(int orgID) throws SQLException {
    PreparedStatement prep =
            conn.prepareStatement("SELECT id FROM organizations WHERE id = ?");
    prep.setInt(1, orgID);
    ResultSet rs = prep.executeQuery();
    return rs.next();
  }

  /**
   * Query to get all of the IDs associated with shift requests for an employee
   * group.
   * @param orgID - ID of the employee group
   * @return all of the shift request IDs for an employee group represented as
   *         a list of Integers.
   * @throws SQLException when error reading database
   */
  public List<Integer> getShiftIDsForOrg(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT id FROM shifts WHERE orgId = ?");
    prep.setInt(1, orgID);
    List<Integer> lstIDs = new ArrayList<>();
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      lstIDs.add(rs.getInt(1));
    }
    return lstIDs;
  }

  /**
   * Query to get an Organization instance of a group specified by ID.
   * @param orgID - ID of the group requested
   * @return an Organization instance representing the group requested if found,
   *         null if not found
   * @throws SQLException when error reading database
   */
  public Organization getOrganization(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM organizations WHERE id = ?");
    prep.setInt(1, orgID);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return new Organization(rs.getInt(1), rs.getString(2), rs.getString(3),
              rs.getString(4), rs.getDate(5), rs.getString(6),
              rs.getString(LAST_INDEX_ORG), null, false);
    }
    return null;
  }

  /**
   * Query to determine whether a specified user is the current owner of a
   * specified employee group.
   * @param orgID - ID of the employee group
   * @param empID - ID of the user
   * @return true if the owner of the group with ID of orgID has an ID of empID,
   *         false otherwise
   * @throws SQLException when error reading database
   */
  public boolean isUserOwner(int orgID, String empID) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement(
            "SELECT owner FROM organizations WHERE id = ?");
    prep.setInt(1, orgID);
    ResultSet rs = prep.executeQuery();
    if (!rs.next()) {
      return false;
    }
    return rs.getString(1).equals(empID);
  }

  /**
   * Query to get the first employee in a specified employee group.
   * @param orgID - ID of the employee group
   * @return ID of the first employee in the employment table to be listed with
   *         the group with ID of orgID if an employee is currently in the group,
   *         null otherwise
   * @throws SQLException when error reading database
   */
  public String getNextEmployeeInOrg(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT empId FROM employment WHERE orgId = ?");
    prep.setInt(1, orgID);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return rs.getString(1);
    }
    return null;
  }

  /**
   * Query to determine a valid, unique group ID for a new group.
   * @return a unique, five-digit, random new group ID as an integer
   * @throws SQLException when error reading database
   */
  public int getNewGroupID() throws SQLException {
    int gID = new Random().nextInt(MAX_GROUP_ID - MIN_GROUP_ID + 1)
            + MIN_GROUP_ID;
    PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM organizations WHERE id = ?");
    prep.setInt(1, gID);
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      gID = new Random().nextInt(MAX_GROUP_ID - MIN_GROUP_ID + 1) + MIN_GROUP_ID;
      prep.setInt(1, gID);
      rs = prep.executeQuery();
    }
    return gID;
  }

  /**
   * Query to get the URL of the profile image for a specified user.
   * @param empID - ID of the user
   * @return the URL of the profile image of user with ID empID, empty String
   *         if user not found
   * @throws SQLException when error reading database
   */
  public String getUserImg(String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT image FROM users WHERE id = ?");
    prep.setString(1, empID);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return rs.getString(1);
    }
    return "";
  }

  /**
   * Query to determine whether a user exists with specified ID.
   * @param empID - ID of the user
   * @return true if there exists a user with ID of empID, false otherwise
   * @throws SQLException when error reading database
   */
  public boolean userExists(String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
    prep.setString(1, empID);
    ResultSet rs = prep.executeQuery();
    return rs.next();
  }

  /**
   * Query to get a user's general information (email, name, date joined,
   * image, and description).
   * @param empID - ID of the user
   * @return user's email, name, date joined, image, and description represented
   *         as an array of Strings if the user is found, null otherwise
   * @throws SQLException when error reading database
   */
  public String[] getUser(String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM users WHERE id = ?");
    prep.setString(1, empID);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return new String[]{rs.getString(2), rs.getString(3),
              rs.getString(4), rs.getString(5), rs.getString(6)};
    }
    return null;
  }

  /**
   * Query to get all the employee groups registered with CovCal from organizations table.
   * @param findEmployees - boolean determining whether the employees and their rankings
   *                      should be calculated when each Organization instance is being
   *                      created
   * @return a mapping of each employee group's ID as an integer to that employee group
   *         represented as an Organization instance
   * @throws SQLException when error reading database
   */
  public Map<Integer, Organization> getAllOrgs(boolean findEmployees) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT * FROM organizations;");
    ResultSet rs = prep.executeQuery();
    Map<Integer, Organization> orgList = new HashMap<>();
    while (rs.next()) {
      Organization org =
              new Organization(rs.getInt(1), rs.getString(2),
                      rs.getString(3), rs.getString(4),
                      rs.getDate(5), rs.getString(6),
                      rs.getString(LAST_INDEX_ORG), this.proxy, findEmployees);
      orgList.put(rs.getInt(1), org);
    }
    return orgList;
  }

  /**
   * Query to get all the employees in a specified employee group.
   * @param orgID - ID of the employee group
   * @return all the employees registered with the group that has ID of orgID
   *         as a list of Employee instances
   * @throws SQLException when error reading database
   */
  public List<Employee> getAllEmployeesFromOrg(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT employment.empId, employment.startDate, employment.positions, users.email,"
                    + " users.name, users.image FROM employment INNER JOIN users "
                    + "ON employment.empId = "
                    + "users.id WHERE employment.orgId = ?;");
    prep.setInt(1, orgID);
    ResultSet rs = prep.executeQuery();
    List<Employee> employeeList = new ArrayList<>();
    while (rs.next()) {
      Employee employee = new Employee(
              rs.getString(1), rs.getDate(2),
              rs.getString(3), rs.getString(4), rs.getString(5),
              rs.getString(6));

      employeeList.add(employee);
    }
    return employeeList;
  }

  /**
   * Query to get all the employees in the history of a specified employee group.
   * @param orgID - ID of the employee group
   * @return all the employees currently and previously registered with the group
   *         that has ID of orgID as a list of Employee instances
   * @throws SQLException when error reading database
   */
  public List<Employee> getAllEmployeesFromOrgHistory(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT employment_history.empId, employment_history.startDate, users.email,"
                    + " users.name, users.image FROM employment_history INNER JOIN users "
                    + "ON employment_history.empId = "
                    + "users.id WHERE employment_history.orgId = ?;");
    prep.setInt(1, orgID);
    ResultSet rs = prep.executeQuery();
    List<Employee> employeeList = new ArrayList<>();
    while (rs.next()) {
      Employee employee = new Employee(
              rs.getString(1), rs.getDate(2),
             null, rs.getString(3), rs.getString(4),
              rs.getString(5));

      employeeList.add(employee);
    }
    return employeeList;
  }

  /**
   * Query to get all the IDs of the employees in a specified employee group.
   * @param orgID - ID of the employee group
   * @return all the IDs of the employees registered with group of ID orgID
   *         represented as a list of Strings.
   * @throws SQLException when error reading database
   */
  public List<String> getAllEmpIDsFromOrg(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT empId FROM employment WHERE orgId = ?;");
    prep.setInt(1, orgID);
    ResultSet rs = prep.executeQuery();
    List<String> lstIDs = new ArrayList<>();
    while (rs.next()) {
      lstIDs.add(rs.getString(1));
    }
    return lstIDs;
  }

  /**
   * Query to get all the employee groups a specified user is registered with.
   * @param empID - ID of the employee
   * @param needEmployees - boolean determining whether the employees and their rankings
   *    *                 should be calculated when each Organization instance is being
   *    *                 created
   * @return all the groups the user with ID of empID is registered with represented as
   *         a list of Organization instances
   * @throws SQLException when error reading database
   */
  public List<Organization> getAllOrgsEmployeeIn(String empID,
                                                 boolean needEmployees) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT employment.orgId, organizations.name, organizations.type, organizations.owner,"
                    + "organizations.dateCreated, organizations.positions, "
                    + "organizations.description "
                    + "FROM employment INNER JOIN organizations ON organizations.id = "
                    + "employment.orgId WHERE employment.empId = ?;");
    prep.setString(1, empID);
    ResultSet rs = prep.executeQuery();
    List<Organization> orgList = new ArrayList<>();
    while (rs.next()) {
      Organization organization = new Organization(rs.getInt(1), rs.getString(2),
              rs.getString(3), rs.getString(4), rs.getDate(5),
              rs.getString(6), rs.getString(LAST_INDEX_ORG), this.proxy, needEmployees);
      orgList.add(organization);
    }
    return orgList;
  }

  /**
   * Query to get the output of a SELECT query to the ledger table that gets
   * ledger inputs for a specified employee group.
   * @param orgID - ID of the employee group
   * @return output of the SELECT query to the ledger table getting the
   *         ledger inputs for employee group with ID of orgID represented
   *         as a ResultSet
   * @throws SQLException when error reading database
   */
  public ResultSet getCoverageLedger(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM ledger WHERE ledger.orgId = '" + orgID + "';");
    return prep.executeQuery();
  }

  /**
   * Query to get the shift requests that a specified user should see from another
   * specified user for a specified employee group; requests that have not been
   * responded to by the user viewing the requests.
   * @param viewerID - ID of the user viewing the requests
   * @param askerID - ID of the user creating the requests
   * @param orgID - ID of the employee group
   * @return the shift requests that the user with ID of viewerID has not responded
   *         to that were also created by the user with ID of askerID for the group
   *         with ID orgID represented as a list of ShiftRequest instances
   * @throws SQLException when error reading database
   */
  public List<ShiftRequest> getRequestsBetweenTwo(String viewerID,
                                                  String askerID, Integer orgID)
          throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT shifts.id, shifts.orgId, shifts.date, shifts.start, shifts.end FROM shifts "
                    + "INNER JOIN shift_responses ON shift_responses.shift_id = "
                    + "shifts.id WHERE shift_responses.response = ? "
                    + "AND shift_responses.emp_id = ? "
                    + "AND shifts.orgId = ? AND shifts.createdBy = ? "
                    + "AND shifts.needCoverage = ?;");
    prep.setString(1, "pending");
    prep.setString(2, viewerID);
    prep.setInt(3, orgID);
    prep.setString(4, askerID);
    prep.setByte(5, (byte) 0);
    ResultSet rs = prep.executeQuery();
    String[] userInfo = getUser(askerID);
    List<ShiftRequest> lstReqs = new ArrayList<>();
    while (rs.next()) {
      lstReqs.add(new ShiftRequest(rs.getInt(1), rs.getInt(2), rs.getDate(3),
              rs.getInt(4), rs.getInt(5), askerID, userInfo[1], userInfo[3]));
    }
    return lstReqs;
  }

  /**
   * Query to determine whether a shift with specified information currently exists.
   * @param empID - ID of employee who created the request
   * @param orgID - ID of the employee group the shift is for
   * @param date - the date of the shift
   * @param start - the start time of the shift (in integer format)
   * @param end - the end time of the shift (in integer format)
   * @return true if there currently exists a shift in the shifts table with date of 'date',
   *         start time of 'start', end time of 'end', for group with ID 'orgID', and
   *         was created by user with ID 'empID'.
   * @throws SQLException when error reading database
   */
  public boolean shiftExists(String empID, int orgID,
                             java.sql.Date date, int start, int end) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT * FROM shifts WHERE orgId = ? AND date = ? AND start = ? "
                    + "AND end = ? AND createdBy = ?");
    prep.setInt(1, orgID);
    prep.setDate(2, date);
    prep.setInt(3, start);
    prep.setInt(4, end);
    prep.setString(5, empID);
    ResultSet rs = prep.executeQuery();
    return rs.next();
  }

  /**
   * Query to get a specified user's fun facts in CovCal.
   * @param empID - ID of the user
   * @return CovCal fun facts for user with ID empID represented as a list
   *         of Strings (# of groups registered with, # of shifts covered,
   *         # of shift cover requests created)
   * @throws SQLException when error reading database
   */
  public String[] getUserFunFacts(String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT "
                    + "(SELECT count(empId) FROM employment WHERE empId = ?) as groups, "
                    + "(SELECT count(donor) FROM ledger WHERE donor = ?) as coverFor, "
                    + "(SELECT count(recipient) FROM ledger WHERE recipient = ?) as coveredBy "
                    + "FROM duel;");
    prep.setString(1, empID);
    prep.setString(2, empID);
    prep.setString(3, empID);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return new String[]{rs.getString("groups"), rs.getString("coverFor"),
              rs.getString("coveredBy")};
    }
    return null;
  }

  /**
   * Query that returns a list of the non-expired requests a specified user has
   * accepted.
   * @param empID - ID of the user
   * @return the shift requests the user with ID empID has accepted that has not
   *         expired represented as a list of ShiftRequests instances
   * @throws SQLException when error reading database
   */
  public List<ShiftRequest> getAcceptedRequests(String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT shifts.id, shifts.orgId, shifts.date, shifts.start, shifts.end, "
                    + "shifts.createdBy FROM shifts INNER JOIN shift_responses ON "
                    + "shifts.id = shift_responses.shift_id WHERE shift_responses.emp_id = ? "
                    + "AND shift_responses.response = ?");
    prep.setString(1, empID);
    prep.setString(2, "accepted");
    List<ShiftRequest> lstReqs = new ArrayList<>();
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      String[] userInfo = getUser(rs.getString(6));
      lstReqs.add(new ShiftRequest(rs.getInt(1), rs.getInt(2), rs.getDate(3),
              rs.getInt(4), rs.getInt(5), rs.getString(6), userInfo[1], userInfo[3]));
    }
    return lstReqs;
  }

  /**
   * Query that returns a list of the non-expired requests a specified user has
   * sent out.
   * @param empID - ID of the user
   * @return the shift cover requests that the user with ID of empID has sent out
   *         that has not expired represented as a list of ShiftRequest instances
   * @throws SQLException when error reading database
   */
  public List<ShiftRequest> getUserRequests(String empID) throws SQLException {
    String[] userInfo = getUser(empID);
    PreparedStatement prep = conn.prepareStatement(
            "SELECT shifts.id, shifts.orgId, shifts.date, shifts.start, shifts.end, "
                    + "shifts.needCoverage, shifts.coverer "
                    + "FROM shifts WHERE shifts.createdBy = ?");
    prep.setString(1, empID);
    List<ShiftRequest> lstReqs = new ArrayList<>();
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      ShiftRequest req = new ShiftRequest(rs.getInt(1), rs.getInt(2), rs.getDate(3),
              rs.getInt(4), rs.getInt(5), empID, userInfo[1], userInfo[3]);
      if (rs.getByte(6) == (byte) 0) {
        req.setAcceptedInfo(false, null);
      } else {
        req.setAcceptedInfo(true, getUser(rs.getString(LAST_INDEX_ORG))[1]);
      }
      lstReqs.add(req);
    }
    return lstReqs;
  }

  /**
   * Query to get the ID of the creator of a shift request.
   * @param shiftID - ID of shift request
   * @return ID of the creator of the shift request, null if not found
   * @throws SQLException when error reading database
   */
  public String getAskerRequest(int shiftID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT createdBy FROM shifts WHERE id = ?");
    prep.setInt(1, shiftID);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return rs.getString(1);
    }
    return null;
  }
}

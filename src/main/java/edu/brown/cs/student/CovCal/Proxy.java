package edu.brown.cs.student.CovCal;

import edu.brown.cs.student.Organization.Employee;
import edu.brown.cs.student.Organization.Organization;
import edu.brown.cs.student.Organization.ShiftRequest;
import edu.brown.cs.student.Utilities.DateTimeUtil;
import edu.brown.cs.student.Utilities.ShiftRequestComparator;

import java.nio.file.Files;
import java.nio.file.Path;;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Class representing a proxy for the database with CovCal data.
 */
public class Proxy {
  private GetQuery getQuery;
  private UpdateQuery updateQuery;
  private Connection conn;
  private static final int ERROR_NO_2 = -2;
  private static final int ERROR_NO_3 = -3;
  private static final int ERROR_NO_4 = -4;

  /**
   * Constructor for a Proxy instance that establishes connection to database
   * and creates GetQuery and UpdateQuery instances.
   * @param dbPath - the file path to the database to connect to
   * @throws SQLException when error connecting to the database
   * @throws ClassNotFoundException when the class for sqlite is not found
   */
  public Proxy(String dbPath) throws SQLException, ClassNotFoundException {
    if (Files.exists(Path.of(dbPath))) {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + dbPath;
      conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      this.getQuery = new GetQuery(conn, this);
      this.updateQuery = new UpdateQuery(conn);
    }
  }

  /**
   * Method handling when a user login is initiated that updates the database if
   * there is a new user logging in.
   * @param empID - the id of the user logging in
   * @param email - the email of the user logging in
   * @param name - the name of the user logging in
   * @param img - the profile image of the user logging in
   * @return true if this is a new user, false otherwise
   */
  public boolean userLogin(String empID, String email, String name, String img) {
    try {
      if (this.getQuery.userExists(empID)) {
        return false;
      }
      this.updateQuery.addNewUser(empID, email, name, img);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  /**
   * Method orchestrating the necessary updates to the database when a user joins
   * an employee group.
   * @param orgIDStr - the employee group ID as a String
   * @param empID - the ID of the employee joining the employee group
   * @param positions - the list of positions the employee is joining the group with
   * @return the group ID on success, -1 if the group does not exist, -2 if the user
   *         is already in the group, -3 on an SQLException, -4 if orgIDStr is not
   *         a valid group ID
   */
  public int addEmployeeToOrg(String orgIDStr, String empID, List<String> positions) {
    try {
      int orgID = Integer.parseInt(orgIDStr);
      if (getQuery.employeeExistsInOrg(orgID, empID)) {
        return ERROR_NO_2;
      }
      if (!getQuery.organizationExists(orgID)) {
        return -1;
      }
      this.updateQuery.registerNewEmployee(orgID, empID, positions);
      List<Integer> lstShiftIDs = this.getQuery.getShiftIDsForOrg(orgID);
      this.updateQuery.addShiftRespNewEmp(empID, lstShiftIDs);
      return orgID;
    } catch (SQLException e) {
      return ERROR_NO_3;
    } catch (NumberFormatException e) {
      return ERROR_NO_4;
    }
  }

  /**
   * Method orchestrating the necessary updates in the database to remove an
   * employee from an employee group.
   * @param orgID - the employee group ID the user is being removed from
   * @param empID - the ID of the employee being removed
   * @return an instance of the Organization representing the employee group
   *         that the employee is being removed from, null on failure
   */
  public Organization removeEmployeeFromOrg(int orgID, String empID) {
    try {
      Organization org = this.getQuery.getOrganization(orgID);
      if (org == null) {
        return null;
      }
      this.updateQuery.unregisterEmployee(orgID, empID);
      if (this.getQuery.isUserOwner(orgID, empID)) {
        String nextEmpID = this.getQuery.getNextEmployeeInOrg(orgID);
        if (nextEmpID == null) {
          this.deleteOrg(orgID, empID);
        } else {
          this.updateQuery.changeGroupOwner(orgID, nextEmpID);
        }
      }
      this.updateQuery.deleteResponsesLeavingEmp(orgID, empID);
      this.updateQuery.deleteRequestsLeavingEmp(orgID, empID);
      this.updateQuery.deleteAcceptedRequestsLeaveEmp(orgID, empID);
      return org;
    } catch (Throwable e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method orchestrating the necessary updates to the database to delete an
   * employee group from CovCal.
   * @param orgID = the ID of the employee group being deleted
   * @param empID - the ID of the employee attempting to delete the group
   * @return on success the instance of the Organization that was deleted, an
   *         instance of an Organization with id -1 if the employee attempting
   *         to delete the group is not the owner, an Organization with id -2 if
   *         an SQLException occurred
   */
  public Organization deleteOrg(int orgID, String empID) {
    try {
      if (!this.getQuery.isUserOwner(orgID, empID)) {
        return new Organization(-1, null, null, null, null,
                null, null, null, false);
      }
      Organization org = this.getQuery.getOrganization(orgID);
      this.updateQuery.deleteOrgFromEmployment(orgID);
      this.updateQuery.deleteOrgFromLedger(orgID);
      this.updateQuery.deleteOrgFromResponses(orgID);
      this.updateQuery.deleteOrgFromRequests(orgID);
      this.updateQuery.deleteOrgFromEmpHistory(orgID);
      this.updateQuery.deleteOrgFromOrganizations(orgID);
      return org;
    } catch (SQLException e) {
      //e.printStackTrace();
      try {
        return new Organization(ERROR_NO_2, null, null, null, null,
                null, null, null, false);
      } catch (SQLException e1) {
        //e1.printStackTrace();
        return null;
      }
    }
  }

  /**
   * Method orchestrating necessary updates in the database to create a new
   * employee group.
   * @param name - name of the new employee group
   * @param type - type of the new employee group
   * @param owner - id of the user creating the group
   * @param positions - the positions available in the group being created
   * @param description - description of the new employee group
   * @return the ID of the new group being created on success, -1 otherwise
   */
  public int createOrganization(String name, String type, String owner, List<String> positions,
                                String description) {
    try {
      int newGroupID = this.getQuery.getNewGroupID();
      this.updateQuery.addOrganization(newGroupID, name, type, owner, positions, description);
      this.updateQuery.registerNewEmployee(newGroupID, owner, positions);
      return newGroupID;
    } catch (SQLException e) {
      //e.printStackTrace();
      return -1;
    }
  }

  /**
   * Method that returns the shift requests to display to a particular user based
   * on the groups they are in; requests are ordered correctly by user ranking.
   * @param empID - the employee who the requests will display to
   * @return a list of ShiftRequest instances representing the requests user with
   *         ID of empID should be able to see in the correct chronological or ranked
   *         order, null on failure
   */
  public List<ShiftRequest> getDisplayRequests(String empID) {
    try {
      List<ShiftRequest> lstRequests = new ArrayList<>();
      List<Organization> lstOrg = this.getQuery.getAllOrgsEmployeeIn(empID, true);
      for (Organization org : lstOrg) {
        this.updateQuery.deletePassedRequests(org.getId());
        Map<Integer, String> mapEmpRank = org.getRankMap();
        int mapSize = mapEmpRank.size();
        for (int i = 1; i <= mapSize; i++) {
          String otherEmpID = mapEmpRank.get(i);
          if (!empID.equals(otherEmpID)) {
            lstRequests.addAll(this.getQuery.getRequestsBetweenTwo(empID, otherEmpID, org.getId()));
          }
        }
      }
      return lstRequests;
    } catch (Throwable e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method that returns the shift requests to display to a particular user based
   * on the groups they are in; requests are ordered by time.
   * @param empID - the employee who the requests will display to
   * @return a list of ShiftRequest instances representing the requests user with
   *         ID of empID should be able to see in the correct chronological or ranked
   *         order, null on failure
   */
  public List<ShiftRequest> getDisplayRequestTimeOrdered(String empID) {
    try {
      List<ShiftRequest> lstRequests = new ArrayList<>();
      List<Organization> lstOrg = this.getQuery.getAllOrgsEmployeeIn(empID, true);
      for (Organization org : lstOrg) {
        this.updateQuery.deletePassedRequests(org.getId());
        List<String> empLst = this.getQuery.getAllEmpIDsFromOrg(org.getId());
        for (String otherEmpID : empLst) {
          if (!empID.equals(otherEmpID)) {
            lstRequests.addAll(this.getQuery.getRequestsBetweenTwo(empID, otherEmpID, org.getId()));
          }
        }
      }
      lstRequests.sort(new ShiftRequestComparator());
      return lstRequests;
    } catch (SQLException e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method orchestrating necessary updates to database on a new shift request
   * being created.
   * @param empID - the ID of the employee requesting the shift cover
   * @param orgID - the ID of the employee group the shift is for
   * @param date - the date that the shift is on
   * @param start - the start time of the shift as an integer (2345 = 11:45pm)
   * @param end - the end time of the shift as an integer (15 = 12:15am)
   * @return "success" on success, "Same shift already exists." if a shift with the
   *         same information has already been added, "Shift start time has already
   *         passed." if the shift start time has already passed, "System error. Please
   *         try again." on SQL exception
   */
  public String addNewShift(String empID, int orgID, java.sql.Date date, int start, int end) {
    try {
      if (DateTimeUtil.dateTimeHasPassed(date, start)) {
        return "Shift start time has already passed.";
      }
      if (start > end) {
        return "Shift start time after end time.";
      }
      if (start == end) {
        return "Start and end time are the same.";
      }
      if (this.getQuery.shiftExists(empID, orgID, date, start, end)) {
        return "Same shift already exists.";
      }
      int shiftID = this.updateQuery.registerNewShiftInShifts(empID, orgID, date, start, end);
      this.updateQuery.initializeResponses(this.getQuery.getAllEmpIDsFromOrg(orgID),
              shiftID, empID, "pending");
      return "success";
    } catch (SQLException e) {
      //e.printStackTrace();
      return "System error occurred. Please try again.";
    }
  }

  /**
   * Method orchestrating necessary updates to database when a shift request is
   * accepted.
   * @param empID - ID of the employee accepting the shift request
   * @param shiftID - ID of the shift being requested coverage for
   * @return true on success, false otherwise
   */
  public boolean acceptShift(String empID, int shiftID) {
    try {
      this.updateQuery.updateResponseToRequest(empID, shiftID, "accepted");
      this.updateQuery.updateCoverageInShifts(shiftID, (byte) 1, empID);
      //this.updateQuery.addToLedger(shiftID, empID);
      return true;
    } catch (SQLException e) {
      //e.printStackTrace();
      return false;
    }
  }

  /**
   * Method orchestrating necessary updates to database when a shift request is
   * declined.
   * @param empID - ID of the employee declining the shift request
   * @param shiftID - ID of the shift being requested coverage for
   * @return true on success, false otherwise
   */
  public boolean declineShift(String empID, int shiftID) {
    try {
      this.updateQuery.updateResponseToRequest(empID, shiftID, "declined");
      return true;
    } catch (SQLException e) {
      //e.printStackTrace();
      return false;
    }
  }

  /**
   * Method returning a specified user's fun facts.
   * @param empID - ID of the user whose fun facts are being requested
   * @return the fun facts of the user with ID empID represented as an array of
   *         Strings, null on failure
   */
  public String[] getUserFunFacts(String empID) {
    try {
      return this.getQuery.getUserFunFacts(empID);
    } catch (SQLException e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method orchestrating necessary updates to the database when employee group
   * information needs to be updated.
   * @param empID - ID of the user attempting to update group information
   * @param orgID - ID of the group being updated
   * @param name - updated name for group
   * @param type - updated type for group
   * @param positions - updated available positions for group
   * @param description - updated description for group
   * @return true on successful update, false otherwise
   */
  public boolean updateOrg(String empID, int orgID, String name, String type,
                           List<String> positions, String description) {
    try {
      if (empID != null && !this.getQuery.isUserOwner(orgID, empID)) {
        return false;
      }
      this.updateQuery.updateOrg(orgID, name, type, positions, description);
      return true;
    } catch (SQLException e) {
      //e.printStackTrace();
      return false;
    }
  }

  /**
   * Method orchestrating necessary updates to database when shift coverage
   * needs to be added to the ledger.
   * @param shiftID - ID of the shift being added to the ledger
   * @param empID - ID of the employee who covered shift being added
   */
  public void addToLedger(int shiftID, String empID) {
    try {
      this.updateQuery.addToLedger(shiftID, empID);
    } catch (SQLException e) {
      //e.printStackTrace();
    }
  }

  /**
   * Method returning the employees in a specified employee group.
   * @param orgID - the ID of the employee group
   * @return the employees in the specified group represented as a list of
   *         Employee instances, null on failure
   */
  public List<Employee> getEmployeesFromOrg(int orgID) {
    try {
      return this.getQuery.getAllEmployeesFromOrg(orgID);
    } catch (SQLException e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method returning the employees in the history of a specified employee group.
   * @param orgID - the ID of the employee group
   * @return the employees currently and previously in the specified group represented
   *         as a list of Employee instances, null on failure
   */
  public List<Employee> getEmployeesFromOrgHistory(int orgID) {
    try {
      return this.getQuery.getAllEmployeesFromOrgHistory(orgID);
    } catch (SQLException e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method returning the output of a query for ledger inputs for a given employee
   * group.
   * @param orgID - ID of the employee group to get ledger inputs for
   * @return query output represented as a ResultSet of all ledger inputs for
   *         employee group with ID of orgID, null on failure
   */
  public ResultSet getCoverageLedger(int orgID) {
    try {
      return this.getQuery.getCoverageLedger(orgID);
    } catch (SQLException e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method returning all the employee groups in CovCal.
   * @param findEmployees - boolean representing whether full initialization
   *                      of each Organization instance is needed (rankings
   *                      and employees defined if true)
   * @return a mapping of group ID to Organization instance of all the groups
   *         registered with CovCal, null on failure
   */
  public Map<Integer, Organization> getAllOrgs(boolean findEmployees) {
    try {
      return this.getQuery.getAllOrgs(findEmployees);
    } catch (SQLException e) {
      //e.printStackTrace();
      return null;
    }
  }

  /**
   * Method orchestrating necessary updates to the database to update a user's
   * profile image.
   * @param empID - ID of the user having profile image update
   * @param newImg - URL of the new profile image for the user
   * @return true on success, false otherwise
   */
  public boolean updateUserImg(String empID, String newImg) {
    try {
      this.updateQuery.updateUserImage(empID, newImg);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Method orchestrating necessary updates to the database to update a user's
   * name and description.
   * @param empID - ID of the user having information updated
   * @param name - updated name of user
   * @param desc - updated description of user
   * @return true on success, false otherwise
   */
  public boolean updateUserName(String empID, String name, String desc) {
    try {
      this.updateQuery.updateUserName(empID, name, desc);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Method returning a user's information from the database.
   * @param empID - ID of the user whose information is requested
   * @return a user's general information represented as an array of Strings
   */
  public String[] getUser(String empID) {
    try {
      return this.getQuery.getUser(empID);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Method returning all the employee groups a specified user is a part of currently.
   * @param empID - ID of the user to get groups from
   * @return the groups user with ID empID is currently a part of represented as a
   *         list of Organization instances, null on failure
   */
  public List<Organization> getOrgsEmployeeIn(String empID) {
    try {
      return this.getQuery.getAllOrgsEmployeeIn(empID, false);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Method returning an Organization instance requested.
   * @param orgID - ID of the group being requested
   * @return instance of Organization with ID of orgID from database if found,
   *         null otherwise
   */
  public Organization getOrganization(int orgID) {
    try {
      return this.getQuery.getOrganization(orgID);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Getter for the GetQuery instance associated with this Proxy instance.
   * @return the field getQuery
   */
  public GetQuery getGetQuery() {
    return this.getQuery;
  }

  /**
   * Getter for the UpdateQuery instance associated with this Proxy instance.
   * @return the field updateQuery
   */
  public UpdateQuery getUpdateQuery() {
    return this.updateQuery;
  }

  /**
   * Method that handles necessary updates to release an accepted request.
   * @param empID - ID of employee releasing the accepted request
   * @param shiftID - ID of shift request being released
   * @return true if successful, false otherwise
   */
  public boolean releaseAcceptedRequest(String empID, int shiftID) {
    try {
      this.updateQuery.updateResponseToRequest(empID, shiftID, "declined");
      this.updateQuery.updateCoverageInShifts(shiftID, (byte) 0, null);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Method that returns a list of the non-expired requests a specified user has
   * accepted.
   * @param empID - ID of the user
   * @return the shift requests the user with ID of empID has accepted that has not
   *         expired represented as a list of ShiftRequest instances, null on an
   *         error
   */
  public List<ShiftRequest> getAcceptedRequests(String empID) {
    try {
      List<ShiftRequest> lstRequests = this.getQuery.getAcceptedRequests(empID);
      lstRequests.sort(new ShiftRequestComparator());
      return lstRequests;
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Method that returns a list of the non-expired requests a specified user has
   * sent out.
   * @param empID - ID of the user
   * @return the shift cover requests that the user with ID of empID has sent out
   *         that has not expired represented as a list of ShiftRequest instances,
   *         null on an error
   */
  public List<ShiftRequest> getUserRequests(String empID) {
    try {
      List<ShiftRequest> lstRequests = this.getQuery.getUserRequests(empID);
      lstRequests.sort(new ShiftRequestComparator());
      return lstRequests;
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Method that cancels a shift request and all the responses to it.
   * @param shiftID - ID of request being cancelled
   * @param empID - user ID of person who created the request
   * @return true if successful, false otherwise
   */
  public boolean cancelRequest(int shiftID, String empID) {
    try {
      if (!empID.equals(this.getQuery.getAskerRequest(shiftID))) {
        return false;
      }
      this.updateQuery.deleteShiftFromResponses(shiftID);
      this.updateQuery.deleteShiftFromShifts(shiftID);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  /**
   * Gives the connection for testing purposes.
   * @return the connection
   */
  public Connection getConn() {
    return conn;
  }

  /**
   * Method for closing the connection for this database.
   * @throws SQLException when error closing connection
   */
  public void closeConn() throws SQLException {
    conn.close();
  }
}

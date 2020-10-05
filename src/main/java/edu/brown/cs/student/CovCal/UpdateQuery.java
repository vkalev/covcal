package edu.brown.cs.student.CovCal;

import edu.brown.cs.student.Utilities.CovCalUtil;
import edu.brown.cs.student.Utilities.DateTimeUtil;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


/**
 * Class representing a Query that executes queries to update the connected CovCal
 * database.
 */
public class UpdateQuery {
  private Connection conn;
  private static final int LAST_INDEX_ORG = 7;

  /**
   * Sets connection to database.
   * @param conn - the connection to the CovCal database
   */
  public UpdateQuery(Connection conn) {
    this.conn = conn;
  }

  /**
   * Query to register a new employee to an employee group by adding row to employment table.
   * @param orgID - ID of employee group to add employee to
   * @param empID - ID of employee to register to employee group
   * @param positions - positions to add employee to group with
   * @throws SQLException when error updating database
   */
  public void registerNewEmployee(int orgID, String empID,
                                  List<String> positions) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "INSERT INTO employment(orgId, empId, startDate, positions) "
                    + "VALUES (?, ?, ?, ?);");
    prep.setInt(1, orgID);
    prep.setString(2, empID);
    // This gets the current date and converts it to the correct format
    String startDate = DateTimeUtil.newFormattedDate();
    prep.setString(3, startDate);
    // This is the list of positions converted to the correct format
    String pList = CovCalUtil.positionGenerator(positions);
    prep.setString(4, pList);
    prep.executeUpdate();
    prep = conn.prepareStatement(
            "INSERT INTO employment_history(orgId, empId, startDate) VALUES (?, ?, ?)");
    prep.setInt(1, orgID);
    prep.setString(2, empID);
    prep.setString(3, startDate);
    prep.executeUpdate();
  }

  /**
   * Query to add 'pending' responses to shift_responses table for specified employee
   * given a list of shift IDs.
   * @param empID - ID of user to add responses for
   * @param lstShiftIDs - list of shift IDs to add responses for
   * @throws SQLException when error updating database
   */
  public void addShiftRespNewEmp(String empID, List<Integer> lstShiftIDs) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "INSERT INTO shift_responses(emp_id, shift_id, response) "
                    + "VALUES (?, ?, ?);");
    prep.setString(1, empID);
    prep.setString(3, "pending");
    for (int shiftID : lstShiftIDs) {
      prep.setInt(2, shiftID);
      prep.executeUpdate();
    }
  }

  /**
   * Query to delete row from employment table that represents a user's employment
   * for a specified employee group.
   * @param orgID - ID of the employee group to remove employee from
   * @param empID - ID of the employee to remove
   * @throws SQLException when error updating database
   */
  public void unregisterEmployee(int orgID, String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM employment WHERE employment.orgId = ? AND employment.empId = ?");
    prep.setInt(1, orgID);
    prep.setString(2, empID);
    prep.executeUpdate();
  }

  /**
   * Query to update the owner of a specified employee group in organizations table.
   * @param orgId - ID of the group to update
   * @param newOwnerId - the ID of the new owner of the specified group
   * @throws SQLException when error updating database
   */
  public void changeGroupOwner(int orgId, String newOwnerId) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "UPDATE organizations SET owner = ? WHERE id = ?");
    prep.setString(1, newOwnerId);
    prep.setInt(2, orgId);
    prep.executeUpdate();
  }

  /**
   * Query to delete all responses from shift_responses table from specified employee
   * for a specified employee group.
   * @param orgID - ID of group to delete responses for
   * @param empID - ID of user of responses to delete
   * @throws SQLException when error updating database
   */
  public void deleteResponsesLeavingEmp(int orgID, String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM shift_responses WHERE emp_id = ? AND shift_id IN "
                    + "(SELECT id FROM shifts WHERE orgId = ?)");
    prep.setString(1, empID);
    prep.setInt(2, orgID);
    prep.executeUpdate();
  }

  /**
   * Query to delete all shift cover requests from shifts table made by specified
   * employee for a specified employee group; responses to the shifts deleted are
   * also removed from shift_responses table.
   * @param orgID - ID of employee group to remove shift cover requests from
   * @param empID - ID of user who made requests to delete
   * @throws SQLException when error updating database
   */
  public void deleteRequestsLeavingEmp(int orgID, String empID) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement(
            "DELETE FROM shift_responses WHERE shift_id IN (SELECT id "
                    + "FROM shifts WHERE createdBy = ? AND orgId = ?)");
    prep.setString(1, empID);
    prep.setInt(2, orgID);
    prep.executeUpdate();
    prep = conn.prepareStatement(
            "DELETE FROM shifts WHERE orgId = ? AND createdBy = ?");
    prep.setInt(1, orgID);
    prep.setString(2, empID);
    prep.executeUpdate();
  }

  /**
   * Query to delete all employments from employment table for a specified
   * employee group.
   * @param orgID - ID of employee group to delete employments from
   * @throws SQLException when error updating database
   */
  public void deleteOrgFromEmployment(int orgID) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement(
            "DELETE FROM employment WHERE orgId = ?");
    prep.setInt(1, orgID);
    prep.executeUpdate();
  }

  /**
   * Query to delete all employments from employment_history table for a specified
   * employee group.
   * @param orgID - ID of employee group to delete employments from
   * @throws SQLException when error updating database
   */
  public void deleteOrgFromEmpHistory(int orgID) throws SQLException {
    PreparedStatement prep;
    prep = conn.prepareStatement(
            "DELETE FROM employment_history WHERE orgId = ?");
    prep.setInt(1, orgID);
    prep.executeUpdate();
  }

  /**
   * Query to delete all ledger inputs from ledger table for a specified
   * employee group.
   * @param orgID - ID of employee group to delete employments from
   * @throws SQLException when error updating database
   */
  public void deleteOrgFromLedger(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM ledger WHERE orgId = ?");
    prep.setInt(1, orgID);
    prep.executeUpdate();
  }

  /**
   * Query to delete all responses from shift_responses table for a specified
   * employee group.
   * @param orgID - ID of employee group to delete responses from
   * @throws SQLException when error updating database
   */
  public void deleteOrgFromResponses(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM shift_responses WHERE shift_id IN "
                    + "(SELECT id FROM shifts WHERE orgId = ?)");
    prep.setInt(1, orgID);
    prep.executeUpdate();
  }

  /**
   * Query to delete all requests from shifts table for a specified
   * employee group.
   * @param orgID - ID of employee group to delete requests from
   * @throws SQLException when error updating database
   */
  public void deleteOrgFromRequests(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM shifts WHERE orgId = ?");
    prep.setInt(1, orgID);
    prep.executeUpdate();
  }

  /**
   * Query to delete employee group from organizations table.
   * @param orgID - ID of employee group to remove
   * @throws SQLException when error updating database
   */
  public void deleteOrgFromOrganizations(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM organizations WHERE id = ?");
    prep.setInt(1, orgID);
    prep.executeUpdate();
  }

  /**
   * Query to add employee group to organizations table.
   * @param groupID - ID of the new group
   * @param name - name of the new group
   * @param type - type of the new group
   * @param owner - owner of the new group
   * @param positions - positions of the new group
   * @param description - description of the new group
   * @throws SQLException when error updating database
   */
  public void addOrganization(int groupID, String name, String type, String owner,
                              List<String> positions, String description) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "INSERT INTO organizations(id, name, type, owner, dateCreated, "
                    + "positions, description) VALUES (?, ?, ?, ?, ?, ?, ?);");
    prep.setInt(1, groupID);
    prep.setString(2, name);
    prep.setString(3, type);
    prep.setString(4, owner);
    // This gets the current date and converts it to the correct format
    prep.setString(5, DateTimeUtil.newFormattedDate());
    // This is the list of positions converted to the correct format
    String pList = CovCalUtil.positionGenerator(positions);
    prep.setString(6, pList);
    prep.setString(LAST_INDEX_ORG, description);
    prep.executeUpdate();
  }

  /**
   * Adds a user when they login the first time.
   * @param empID - ID of new user (gotten using Google API)
   * @param email - email of the new user
   * @param name - name of the new user
   * @param img = user image for Google account
   * @throws SQLException when error updating database
   */
  public void addNewUser(String empID, String email, String name,
                         String img) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "INSERT INTO users(id, email, name, dateJoined, image, description) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");
    prep.setString(1, empID);
    prep.setString(2, email);
    prep.setString(3, name);
    // This gets the current date and converts it to the correct format
    prep.setString(4, DateTimeUtil.newFormattedDate());
    prep.setString(5, img);
    prep.setString(6, "");
    prep.executeUpdate();
  }

  /**
   * Query to update response from a specified user in shift_responses table
   * to a specified request.
   * @param empID - ID of user whose response is being updated
   * @param shiftID - ID of shift that the response being updated is to
   * @param newResponse - new response
   * @throws SQLException when error updating database
   */
  public void updateResponseToRequest(String empID, int shiftID,
                                      String newResponse) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "UPDATE shift_responses SET response = ? WHERE emp_id = ? AND shift_id = ?");
    prep.setString(1, newResponse);
    prep.setString(2, empID);
    prep.setInt(3, shiftID);
    prep.executeUpdate();
  }

  /**
   * Query to update the coverage status of a shift in the shifts table.
   * @param shiftID - ID of shift whose coverage status is being updated
   * @param needCover - byte representing if shift needs coverage
   * @param empCovering - ID of the employee covering the shift if it does not
   *                    need coverage
   * @throws SQLException when error updating database
   */
  public void updateCoverageInShifts(int shiftID, byte needCover,
                                     String empCovering) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "UPDATE shifts SET needCoverage = ?, coverer = ? WHERE id = ?");
    prep.setByte(1, needCover);
    prep.setString(2, empCovering);
    prep.setInt(3, shiftID);
    prep.executeUpdate();
  }

  /**
   * Adds a new ledger to the table.
   * @param shiftID id of shift
   * @param empID id of donor employee
   * @throws SQLException when error updating database
   */
  public void addToLedger(int shiftID, String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "SELECT orgId, createdBy FROM shifts WHERE id = ?");
    prep.setInt(1, shiftID);
    ResultSet rs = prep.executeQuery();
    prep = conn.prepareStatement(
            "INSERT INTO ledger VALUES (?, ?, ?, ?)");
    prep.setInt(1, rs.getInt(1));
    prep.setString(2, rs.getString(2));
    prep.setString(3, empID);
    // This gets the current date and converts it to the correct format
    prep.setString(4, DateTimeUtil.newFormattedDate());
    prep.executeUpdate();
  }

  /**
   * Query to delete all requests (in shifts table) and responses to those shifts
   * (in shift_responses table) whose 'end' times are in the past, for a specified
   * employee group.
   * @param orgID - ID of employee group whose expired requests and respective responses
   *              should be deleted
   * @throws SQLException when error updating database
   */
  public void deletePassedRequests(int orgID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement("SELECT id, date, end, needCoverage, "
            + "coverer FROM shifts WHERE orgId = ?");
    prep.setInt(1, orgID);
    PreparedStatement prep2 = conn.prepareStatement("DELETE FROM shift_responses "
            + "WHERE shift_id = ?");
    PreparedStatement prep3 = conn.prepareStatement("DELETE FROM shifts WHERE id = ?");
    ResultSet rs = prep.executeQuery();
    while (rs.next()) {
      if (DateTimeUtil.dateTimeHasPassed(rs.getDate(2), rs.getInt(3))) {
        if (rs.getByte(4) == (byte) 1) {
          addToLedger(rs.getInt(1), rs.getString(5));
        }
        int idShiftPassed = rs.getInt(1);
        prep2.setInt(1, idShiftPassed);
        prep2.executeUpdate();
        prep3.setInt(1, idShiftPassed);
        prep3.executeUpdate();
      }
    }
  }

  /**
   * Query to insert new shift request in shifts table and returns the new request's
   * id.
   * @param empID - ID of employee creating the request
   * @param orgID - ID of employee group the request is made for
   * @param date - the date of the shift
   * @param start - the start time of the shift (integer form)
   * @param end - the start time of the shift (integer form)
   * @return id of the new shift registered on success, -1 otherwise
   * @throws SQLException when error updating database
   */
  public int registerNewShiftInShifts(String empID, int orgID, java.sql.Date date,
                                      int start, int end) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "INSERT INTO shifts (orgId, date, start, end, assigned, createdBy) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");
    prep.setInt(1, orgID);
    prep.setDate(2, date);
    prep.setInt(3, start);
    prep.setInt(4, end);
    prep.setInt(5, 0);
    prep.setString(6, empID);
    prep.executeUpdate();
    prep = conn.prepareStatement(
            "SELECT id FROM shifts WHERE orgId = ? AND date = ? AND start = ? "
                    + "AND end = ? AND createdBy = ?");
    prep.setInt(1, orgID);
    prep.setDate(2, date);
    prep.setInt(3, start);
    prep.setInt(4, end);
    prep.setString(5, empID);
    ResultSet rs = prep.executeQuery();
    if (rs.next()) {
      return rs.getInt(1);
    }
    return -1;
  }

  /**
   * Query to add shift responses for multiple employees for a specified shift to
   * shift_responses table.
   * @param lstEmpIDs - all the employee IDs to add responses for
   * @param shiftID - ID of shift that responses added are for
   * @param creatorID - ID of creator of shift request being responded to
   * @param response - the new response to update shift_responses table with
   * @throws SQLException when error updating database
   */
  public void initializeResponses(List<String> lstEmpIDs, int shiftID, String creatorID,
                                  String response) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "INSERT INTO shift_responses (emp_id, shift_id, response) VALUES (?, ?, ?)");
    for (String id : lstEmpIDs) {
      if (!id.equals(creatorID)) {
        prep.setString(1, id);
        prep.setInt(2, shiftID);
        prep.setString(3, response);
        prep.executeUpdate();
      }
    }
  }

  /**
   * Query to update a specified employee group's information in the organizations table.
   * @param orgId - ID of the group whose information is to be updated
   * @param name - updated name of the group
   * @param type - updated type of the group
   * @param positions - updated positions of the group
   * @param description - updated description of the group
   * @throws SQLException when error updating database
   */
  public void updateOrg(int orgId, String name, String type, List<String> positions,
                        String description) throws SQLException {
    String pList = CovCalUtil.positionGenerator(positions);
    // add it to the database
    PreparedStatement prep = conn.prepareStatement(
            "UPDATE organizations SET name = ?, type = ?, positions = ?, description = ? "
                    + "WHERE id = ?");
    prep.setString(1, name);
    prep.setString(2, type);
    prep.setString(3, pList);
    prep.setString(4, description);
    prep.setInt(5, orgId);
    prep.executeUpdate();
  }

  /**
   * Query to update the user's profile picture in users table.
   * @param empId - ID of the user whose picture is to be updated
   * @param image - URL of the new profile picture
   * @throws SQLException when error updating database
   */
  public void updateUserImage(String empId, String image) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "UPDATE users SET image = ? WHERE id = ?");
    prep.setString(1, image);
    prep.setString(2, empId);
    prep.executeUpdate();
  }

  /**
   * Query to update the user's name and description in users table.
   * @param empId - ID of the user whose picture is to be updated
   * @param name - updated name of the user
   * @param desc - updated description of the user
   * @throws SQLException when error updating database
   */
  public void updateUserName(String empId, String name, String desc) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "UPDATE users SET name = ?, description = ? WHERE id = ?");
    prep.setString(1, name);
    prep.setString(2, desc);
    prep.setString(3, empId);
    prep.executeUpdate();
  }

  /**
   * Query to delete all responses from shift_responses table for a specified
   * shift request.
   * @param shiftID - ID of shift request to delete responses from
   * @throws SQLException when error updating database
   */
  public void deleteShiftFromResponses(int shiftID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM shift_responses WHERE shift_id = ?");
    prep.setInt(1, shiftID);
    prep.executeUpdate();
  }

  /**
   * Query to delete shift request from shifts table.
   * @param shiftID - ID of shift request to delete
   * @throws SQLException when error updating database
   */
  public void deleteShiftFromShifts(int shiftID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "DELETE FROM shifts WHERE id = ?");
    prep.setInt(1, shiftID);
    prep.executeUpdate();
  }

  /**
   * Query to change all shifts a leaving employee has currently accepted
   * to need coverage.
   * @param orgID - ID of employee group the user is leaving
   * @param empID - ID of the user
   * @throws SQLException when error reading database
   */
  public void deleteAcceptedRequestsLeaveEmp(int orgID, String empID) throws SQLException {
    PreparedStatement prep = conn.prepareStatement(
            "UPDATE shifts SET needCoverage = ?, coverer = ? WHERE coverer = ? AND orgId = ?");
    prep.setByte(1, (byte) 0);
    prep.setString(2, null);
    prep.setString(3, empID);
    prep.setInt(4, orgID);
    prep.executeUpdate();
  }
}

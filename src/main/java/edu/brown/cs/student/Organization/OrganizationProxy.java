package edu.brown.cs.student.Organization;

import edu.brown.cs.student.CovCal.Proxy;
import org.javatuples.Pair;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a proxy for an organization.
 */
public class OrganizationProxy {
  private final int orgID;
  private Proxy proxy;

  /**
   * Constructor for OrganizationProxy that sets the CovCal proxy and the ID of
   * the organization.
   * @param orgID - ID of the organization
   * @param proxy - CovCal proxy
   */
  public OrganizationProxy(int orgID, Proxy proxy) {
    this.orgID = orgID;
    this.proxy = proxy;
  }

  // Queries for adding/deleting from the database:

  /**
   * Adds an employee to the database.
   * @param employeeId employee id
   * @param positions positions of the employee
   * @throws SQLException exception
   */
  public void addEmployee(String employeeId, List<String> positions) throws SQLException {
    proxy.addEmployeeToOrg(Integer.toString(this.orgID), employeeId, positions);
  }

  /**
   * Removes an employee.
   * @param empId empId
   * @throws SQLException exception
   */
  public void removeEmployee(String empId) throws SQLException {
    proxy.removeEmployeeFromOrg(this.orgID, empId);
  }

  /**
   * Updates the organization data.
   * @param name name
   * @param type employee or employer run
   * @param positions position list
   * @param description description
   * @throws SQLException exception
   */
  public void updateOrg(String name, String type, List<String> positions, String description)
      throws SQLException {
    proxy.updateOrg(null, orgID, name, type, positions, description);
  }

  // Queries for getting data:

  /**
   * Gets all the employees.
   * @return the employees
   * @throws SQLException exception
   */
  public List<Employee> getEmployees() throws SQLException {
    return proxy.getEmployeesFromOrg(this.orgID);
  }

  /**
   * Gets the org's ledger.
   * @return the ledger
   * @throws SQLException exception
   */
  public List<Pair<String, String>> getCoverageLedger() throws SQLException {
    ResultSet rs = proxy.getCoverageLedger(this.orgID);
    List<Pair<String, String>> ledgerList = new ArrayList<>();
    while (rs.next()) {

      Pair<String, String> coverage = new Pair(rs.getString(2), rs.getString(3));
      ledgerList.add(coverage);
    }
    return ledgerList;
  }

  /**
   * Getter for the organization's ID.
   * @return the orgID field
   */
  public int getOrgID() {
    return this.orgID;
  }

  /**
   * Getter for the organizations Proxy.
   * @return - proxy
   */
  public Proxy getProxy() {
    return this.proxy;
  }
}

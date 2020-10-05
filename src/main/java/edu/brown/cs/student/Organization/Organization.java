package edu.brown.cs.student.Organization;

import edu.brown.cs.student.CovCal.Proxy;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class representing an Organization (which is a group of employees in CovCal).
 */
public class Organization {
  private static final int RANK_UPDATE_CONST = 3;
  private OrganizationProxy orgProxy;
  private Proxy proxy;
  private int id;
  private String name;
  private String type;
  private String owner;
  private Date date;
  private String positions;
  private String description;
  private List<Employee> employees;
  private Map<String, Employee> idToEmployee;
  // This is the current order of rank
  private List<RankCalculator.Worker> rankList;
  // This is a counter that keeps track of when to update the rankList
  private Integer rankCounter;

  /**
   * Constructor for an Organization in CovCal.
   * @param id - ID of the organization
   * @param name - name of the organization
   * @param type - type of organization
   * @param owner - ID of the owner of the organization
   * @param dateCreated - date the organization was created
   * @param positions - the positions available for the organization
   * @param description - description of the organization
   * @param p - the proxy for the CovCal program instance this organization is part of
   * @param getEmployees - boolean representing whether the employees need to be
   *                     gotten and ranks calculated on creation
   * @throws SQLException when error querying for employee information
   */
  public Organization(int id, String name, String type, String owner, Date dateCreated,
                      String positions, String description, Proxy p,
                      boolean getEmployees) throws SQLException {
    this.idToEmployee = new HashMap<>();
    this.id = id;
    this.name = name;
    this.type = type;
    this.owner = owner;
    this.date = dateCreated;
    this.positions = positions;
    this.description = description;

    if (getEmployees) {
      this.proxy = p;
      this.orgProxy = new OrganizationProxy(id, p);
      this.employees = this.orgProxy.getEmployees();
      rankCounter = RANK_UPDATE_CONST - 1;
      getRank();
    }
  }

  /**
   * This is how we get the rank ordering of all the employees at the company.
   * Call this each time an employee tries to ask for their shift covered.
   * @return returns the ordered list
   * @throws SQLException exception
   */
  public List<RankCalculator.Worker> getRank() throws SQLException {
    // If we have hit the database recently, we don't update the rankList yet
    if (rankCounter < RANK_UPDATE_CONST) {
      rankCounter++;
    } else {
      rankCounter = 0;
      // Here, we get the ids of the employees
      List<String> employeeIDs = new ArrayList<>();
      List<Employee> allEmployeesHistory = this.proxy.getEmployeesFromOrgHistory(this.id);
      for (Employee e:allEmployeesHistory) {
        employeeIDs.add(e.getId());
      }
      RankCalculator rankCalculator = new RankCalculator(orgProxy);
      rankList = rankCalculator.getOrdering(employeeIDs);
    }
    return rankList;
  }

  /**
   * For adding an employee.
   * @param empId employee
   * @param posLst positions
   * @throws SQLException exception
   */
  public void addEmployee(String empId, List<String> posLst) throws SQLException {
    // First, add it to the database
    orgProxy.addEmployee(empId, posLst);
    // Now, update the employee list
    employees = orgProxy.getEmployees();
    //used to update map that is used for testing
    getEmployeeMap();
  }

  /**
   * Updates the employee map with potential new employees.
   * Used for testing.
   * @return map of employee IDs to Employee instances of potential new employees
   */
  public Map<String, Employee> getEmployeeMap() {
    this.idToEmployee = new HashMap<>();
    for (Employee e: employees) {
      this.idToEmployee.put(e.getId(), e);
    }
    return this.idToEmployee;
  }

  /**
   * Removing an employee.
   * @param empId employee
   * @throws SQLException exception
   */
  public void removeEmployee(String empId) throws SQLException {
    // Remove it from the database
    orgProxy.removeEmployee(empId);
    // Update the employee list
    for (int i = 0; i < employees.size(); i++) {
      if (employees.get(i).getId().equals(empId)) {
        employees.remove(i);
        break;
      }
    }
  }

  /**
   * This method creates a json with all of the data that is used for the frontend
   * leaderboard.
   * @return - map of strings to maps.
   * @throws SQLException - sql exception.
   */
  public Map<String, Map<String, Object>> getJSONLedgerData() throws SQLException {
    Map<String, Map<String, Object>> ledger = new LinkedHashMap<>();
    for (RankCalculator.Worker w:getRank()) {
      for (Employee e: employees) {
        if (e.getId().equals(w.getID())) {
          Map<String, Object> workerInfo = Map.of(
              "requests", w.getOutgoingEdges().size(),
              "coverages", w.getIncomingEdges().size(),
              "id", w.getID(),
              "rank", w.getRank(),
              "name", e.getName());

          ledger.put(e.getId(), workerInfo);
        }
      }
    }
    return ledger;
  }

  /**
   * Method to get the rank map of rank values for workers.
   * @return - rank map from order ranking to worker ID.
   * @throws SQLException - sql exception
   */
  public Map<Integer, String> getRankMap() throws SQLException {
    //get statement is used for testing to update map
    getEmployeeMap();
    Map<Integer, String> rankMap = new LinkedHashMap<>();
    List<RankCalculator.Worker> rankLst = getRank();
    for (int i = 0; i < rankLst.size(); i++) {
      rankMap.put(i + 1, rankLst.get(i).getID());
    }
    return rankMap;
  }

  /**
   * Get's organization's ID.
   * @return - orgID
   */
  public int getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Organization) {
      Organization org = (Organization) o;
      return this.id == org.id && this.date.equals(org.date) && this.name.equals(org.name)
              && this.owner.equals(org.owner) && this.description.equals(org.description)
              && this.type.equals(org.type);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.id;
  }

  /**
   * Method used to access org proxy.
   * Only used for testing.
   * @return - org proxy
   */
  public OrganizationProxy getProxy() {
    return this.orgProxy;
  }

  /**
   * Getter for organization description.
   * @return - description
   */
  public String getDescription() {
    return this.description;
  }

}

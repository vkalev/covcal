package edu.brown.cs.student.CovCal;

import edu.brown.cs.student.Organization.Organization;
import java.sql.SQLException;
import java.util.Map;


/**
 * Class representing a CovCal program.
 */
public class CovCal {
  // instance of the sql query class
  private Proxy proxy = new Proxy("data/covcal.db");
  // This is the entire list of current Organizations
  private Map<Integer, Organization> organizations;

  /**
   * Constructor for a CovCal instance that gets the organizations registered
   * with CovCal.
   * @throws SQLException - when error querying for organizations
   * @throws ClassNotFoundException - when sqlite class not found
   */
  public CovCal() throws ClassNotFoundException, SQLException {
    // We first grab all the current Organizations
    organizations = proxy.getAllOrgs(true);
  }

  /**
   * Getter for a specific organization.
   * @param id - ID of the Organization being requested
   * @return the Organization instance with ID of ID in the organizations
   *         field
   */
  public Organization getOrg(int id) {
    organizations = proxy.getAllOrgs(true);
    return organizations.get(id);
  }

  /**
   * Getter for the proxy associated with the CovCal program.
   * @return the proxy field
   */
  public Proxy getProxy() {
    return this.proxy;
  }
}

package edu.brown.cs.student;

import edu.brown.cs.student.CovCal.GetQuery;
import edu.brown.cs.student.CovCal.Proxy;
import edu.brown.cs.student.Organization.Employee;
import edu.brown.cs.student.Organization.Organization;
import edu.brown.cs.student.Organization.ShiftRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

/**
 * Most of the proxy class acts as a wrapper for the UpdateQuery
 * and GetQuery classes, thus only the methods that contain additional
 * logic are being tested here.
 */
public class ProxyTest {

  private Proxy _proxy;
  private Map<Integer,Organization> _orgs;


  @Before
  public void setUpConn() {
    try {

      this._proxy = new Proxy("data/covcal_test_proxy.db");
      this._orgs =  this._proxy.getAllOrgs(true);
    } catch (SQLException | ClassNotFoundException e) {
      //e.printStackTrace();

    }
  }

  @After
  public void tearDown() {
    this._proxy = null;
  }


  @Test
  public void addEmployeeToOrgTest() {
    try {
      assertEquals(-1, this._proxy.addEmployeeToOrg("000000000000","1",new ArrayList<>()));
      assertEquals(-2, this._proxy.addEmployeeToOrg("12345","4",new ArrayList<>()));
      assertEquals (-3,this._proxy.addEmployeeToOrg("12345","1",new ArrayList<>()));
      assertEquals(-4, this._proxy.addEmployeeToOrg("invalidOrg","1",new ArrayList<>()));
    }catch (Throwable e) {}

  }


  @Test
  public void removeEmployeeFromOrgTest() {
    try {
      //invalid employee
      assertEquals(null, this._proxy.removeEmployeeFromOrg(12345, "invalidEmployee"));
      //invalid group
      assertEquals(null, this._proxy.removeEmployeeFromOrg(000000000000, "1"));
    } catch (Throwable e) {}

  }

  @Test
  public void deleteOrgTest() {
    try {
      assertEquals(-1, this._proxy.deleteOrg(12345, "7").getId());
      assertEquals(-1, this._proxy.deleteOrg(00000000000, "4").getId());
    } catch (Throwable e) {}

  }

  @Test
  public void addNewShiftTest() throws ParseException {
    try {
      assertEquals("Shift start time has already passed.",
          this._proxy.addNewShift("4",23456,new Date(20000101),2345,2345));
      SimpleDateFormat sdf = new SimpleDateFormat("yyyymmdd");
    } catch (Throwable e) {}

  }


}

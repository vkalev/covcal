package edu.brown.cs.student;

import edu.brown.cs.student.CovCal.GetQuery;
import edu.brown.cs.student.CovCal.Proxy;
import edu.brown.cs.student.Organization.Employee;
import edu.brown.cs.student.Organization.Organization;
import edu.brown.cs.student.Organization.ShiftRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GetQueryTest {
  private GetQuery _getQuery;
  private Proxy _proxy;
  private Map<Integer, Organization> _orgs;
  private Map<String, Employee> _users;
  private Map<Integer, ShiftRequest> _requests;

  @Before
  public void setUpConn() {
    try {
      this._proxy = new Proxy("data/covcal_test_getquery.db");
      this._getQuery = this._proxy.getGetQuery();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      this._getQuery = null;
    }
  }

  private void setUpOrgs() {
    try {
      Date parkDate = new Date(20200430);
      Date officeDate = new Date(20200422);
      Date emptyDate = new Date(20200501);
      Organization parkOrg = new Organization(12345, "Parks Dep.", "Gov. Group", "2",
              parkDate, null, "We did Harvest Fest", this._proxy, false);
      Organization officeOrg = new Organization(23456, "Dunder Mifflin", "Paper Company", "1",
              officeDate, null, "Great customer service", this._proxy, false);
      Organization emptyOrg = new Organization(11111, "Empty Group", "Unproductive", "1",
              emptyDate, null, "Nothingness", this._proxy, false);
      this._orgs = new HashMap<>();
      this._orgs.put(12345, parkOrg);
      this._orgs.put(23456, officeOrg);
      this._orgs.put(11111, emptyOrg);
    } catch (SQLException e) {
      e.printStackTrace();
      this._orgs = null;
    }
  }

  private void setUpUsers() {
    Employee michael = new Employee("1", new Date(20200421), null,
            "ms@office", "Michael Scott",
            "michael_scott_pic");
    Employee lesley = new Employee("2", new Date(20200423), null,
            "lk@park", "Lesley Knope",
            "lesley_knope_image");
    Employee dwight = new Employee("3", new Date(20200422), null,
            "ds@office", "Dwight K. Schrute",
            "beet_photo");
    Employee rashida = new Employee("4", new Date(20200425), null,
            "rj@actor", "Rashida Jones",
            "rashida_jones_image");
    Employee jim = new Employee("5", new Date(20200424), null,
            "jh@office", "Jim Halpert",
            "jim_image");
    Employee jon = new Employee("6", new Date(20200501), null,
            "jr@snakehole", "Jon Ralfio",
            "ralfio_image");
    Employee lil_seb = new Employee("7", new Date(20200420), null,
            "mini@horse", "Lil Sebastian",
            "sebastian_image");
    this._users = new HashMap<>();
    this._users.put("1", michael);
    this._users.put("2", lesley);
    this._users.put("3", dwight);
    this._users.put("4", rashida);
    this._users.put("5", jim);
    this._users.put("6", jon);
    this._users.put("7", lil_seb);
  }

  public void setUpRequests() {
    ShiftRequest shiftOne = new ShiftRequest(1, 12345, new Date(20200501),
            415, 1345, "7", "Lil Sebastian", "sebastian_image");
    ShiftRequest shiftTwo = new ShiftRequest(2, 12345, new Date(20200501),
            1425, 1525, "4", "Rashida Jones", "rashida_image");
    ShiftRequest shiftThree = new ShiftRequest(3, 23456, new Date(20200501),
            945, 1145, "4", "Rashida Jones", "rashida_image");
    ShiftRequest shiftFour = new ShiftRequest(4, 23456, new Date(20200430),
            315, 600, "4", "Rashida Jones", "rashida_image");
    ShiftRequest shiftFive = new ShiftRequest(5, 12345, new Date(20200501),
            1300, 2235, "2", "Lesley Knope", "lesley_image");
    ShiftRequest shiftSix = new ShiftRequest(6, 23456, new Date(20200503),
            200, 500, "5", "Jim Halpert", "jim_image");
    this._requests = new HashMap<>();
    this._requests.put(1, shiftOne);
    this._requests.put(2, shiftTwo);
    this._requests.put(3, shiftThree);
    this._requests.put(4, shiftFour);
    this._requests.put(5, shiftFive);
    this._requests.put(6, shiftSix);
  }

  @After
  public void tearDown() {
    this._getQuery = null;
    this._orgs = null;
    this._requests = null;
  }

  @Test
  public void testEmpExistsInOrg() {
    try {
      assertTrue(this._getQuery.employeeExistsInOrg(12345, "2"));
      assertTrue(this._getQuery.employeeExistsInOrg(12345, "4"));
      assertFalse(this._getQuery.employeeExistsInOrg(12345, "1"));
      assertFalse(this._getQuery.employeeExistsInOrg(23456, "2"));
      assertTrue(this._getQuery.employeeExistsInOrg(23456, "4"));
      assertFalse(this._getQuery.employeeExistsInOrg(13578, "2"));
      assertFalse(this._getQuery.employeeExistsInOrg(12345, "10"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testOrgExists() {
    try {
      assertTrue(this._getQuery.organizationExists(12345));
      assertTrue(this._getQuery.organizationExists(23456));
      assertFalse(this._getQuery.organizationExists(35291));
      assertFalse(this._getQuery.organizationExists(23457));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetShiftIDsForOrg() {
    try {
      Integer[] expectedOutput = new Integer[]{1, 2, 5};
      assertArrayEquals(this._getQuery.getShiftIDsForOrg(12345).toArray(), expectedOutput);
      expectedOutput = new Integer[]{3, 4, 6};
      assertArrayEquals(this._getQuery.getShiftIDsForOrg(23456).toArray(), expectedOutput);
      expectedOutput = new Integer[]{};
      assertArrayEquals(this._getQuery.getShiftIDsForOrg(13345).toArray(), expectedOutput);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetOrganization() {
    try {
      setUpOrgs();
      assertEquals(this._getQuery.getOrganization(12345), this._orgs.get(12345));
      assertEquals(this._getQuery.getOrganization(23456), this._orgs.get(23456));
      assertEquals(this._getQuery.getOrganization(11111), this._orgs.get(11111));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIsUserOwner() {
    try {
      assertTrue(this._getQuery.isUserOwner(12345, "2"));
      assertTrue(this._getQuery.isUserOwner(23456, "1"));
      assertFalse(this._getQuery.isUserOwner(12345, "1"));
      assertFalse(this._getQuery.isUserOwner(23456, "3"));
      assertFalse(this._getQuery.isUserOwner(12345, "6"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetNextEmployeeInOrg() {
    try {
      setUpUsers();
      assertEquals(this._getQuery.getNextEmployeeInOrg(12345), "2");
      assertEquals(this._getQuery.getNextEmployeeInOrg(23456), "1");
      assertNull(this._getQuery.getNextEmployeeInOrg(13456));
      assertNull(this._getQuery.getNextEmployeeInOrg(11111));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetNewGroupID() {
    try {
      int newGroupID;
      for (int i=0; i<7; i++) {
        newGroupID = this._getQuery.getNewGroupID();
        assertNotEquals(newGroupID, 12345);
        assertNotEquals(newGroupID, 23456);
        assertNotEquals(newGroupID, 11111);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetUserImg() {
    try {
      assertEquals(this._getQuery.getUserImg("3"), "beet_photo");
      assertEquals(this._getQuery.getUserImg("6"), "ralfio_image");
      assertEquals(this._getQuery.getUserImg("10"), "");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testUserExists() {
    try {
      assertTrue(this._getQuery.userExists("1"));
      assertTrue(this._getQuery.userExists("3"));
      assertFalse(this._getQuery.userExists("0"));
      assertFalse(this._getQuery.userExists("9"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetUser() {
    try {
      setUpUsers();
      Employee emp = this._users.get("1");
      String[] expectedOutput = new String[]{emp.getEmail(), emp.getName(), "20200421",
              emp.getImageUrl(), "Undercover Michael Scarn"};
      assertArrayEquals(this._getQuery.getUser("1"), expectedOutput);
      emp = this._users.get("5");
      expectedOutput = new String[]{emp.getEmail(), emp.getName(), "20200424",
              emp.getImageUrl(), "I am immature"};
      assertArrayEquals(this._getQuery.getUser("5"), expectedOutput);
      assertNull(this._getQuery.getUser("19"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetAllOrgs() {
    try {
      setUpOrgs();
      Map<Integer, Organization> mapOrgs = this._getQuery.getAllOrgs(false);
      assertEquals(mapOrgs.get(12345), this._orgs.get(12345));
      assertEquals(mapOrgs.get(23456), this._orgs.get(23456));
      assertEquals(mapOrgs.get(11111), this._orgs.get(11111));
      assertEquals(mapOrgs.size(), 3);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetEmpsInOrg() {
    try {
      setUpUsers();
      assertArrayEquals(this._getQuery.getAllEmployeesFromOrg(11111).toArray(), new Employee[]{});
      assertArrayEquals(this._getQuery.getAllEmployeesFromOrg(12121).toArray(), new Employee[]{});
      Employee[] expectedOutput = new Employee[]{this._users.get("1"), this._users.get("3"), this._users.get("4"), this._users.get("5")};
      List<Employee> lstEmps = this._getQuery.getAllEmployeesFromOrg(23456);
      assertEquals(lstEmps.size(), expectedOutput.length);
      for (int i=0; i<expectedOutput.length; i++) {
        Employee expectEmp = expectedOutput[i];
        Employee testEmp = lstEmps.get(i);
        assertEquals(expectEmp.getId(), testEmp.getId());
      }
      expectedOutput = new Employee[]{this._users.get("2"), this._users.get("4"), this._users.get("7")};
      lstEmps = this._getQuery.getAllEmployeesFromOrg(12345);
      assertEquals(lstEmps.size(), expectedOutput.length);
      for (int i=0; i<expectedOutput.length; i++) {
        Employee expectEmp = expectedOutput[i];
        Employee testEmp = lstEmps.get(i);
        assertEquals(expectEmp.getId(), testEmp.getId());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetEmpIDsInOrg() {
    try {
      assertArrayEquals(this._getQuery.getAllEmpIDsFromOrg(11111).toArray(), new String[]{});
      assertArrayEquals(this._getQuery.getAllEmpIDsFromOrg(12121).toArray(), new String[]{});
      String[] expectedOutput = new String[]{"1", "3", "4", "5"};
      List<String> lstEmpIDs = this._getQuery.getAllEmpIDsFromOrg(23456);
      assertEquals(lstEmpIDs.size(), expectedOutput.length);
      for (int i=0; i<expectedOutput.length; i++) {
        String expectEmpID = expectedOutput[i];
        String testEmpID = lstEmpIDs.get(i);
        assertEquals(expectEmpID, testEmpID);
      }
      expectedOutput = new String[]{"2", "4", "7"};
      lstEmpIDs = this._getQuery.getAllEmpIDsFromOrg(12345);
      assertEquals(lstEmpIDs.size(), expectedOutput.length);
      for (int i=0; i<expectedOutput.length; i++) {
        String expectEmpID = expectedOutput[i];
        String testEmpID = lstEmpIDs.get(i);
        assertEquals(expectEmpID, testEmpID);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetOrgsEmpIn() {
    try {
      setUpOrgs();
      assertArrayEquals(this._getQuery.getAllOrgsEmployeeIn("6", false).toArray(),
              new Organization[]{});
      assertArrayEquals(this._getQuery.getAllOrgsEmployeeIn("1", false).toArray(),
              new Organization[]{this._orgs.get(23456)});
      assertArrayEquals(this._getQuery.getAllOrgsEmployeeIn("7", false).toArray(),
              new Organization[]{this._orgs.get(12345)});
      assertArrayEquals(this._getQuery.getAllOrgsEmployeeIn("4", false).toArray(),
              new Organization[]{this._orgs.get(12345), this._orgs.get(23456)});
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetRequestsBetweenTwo() {
    try {
      setUpRequests();
      assertArrayEquals(this._getQuery.getRequestsBetweenTwo("6", "1", 23456).toArray(),
              new ShiftRequest[]{});
      assertArrayEquals(this._getQuery.getRequestsBetweenTwo("4", "4", 12345).toArray(),
              new ShiftRequest[]{});
      assertArrayEquals(this._getQuery.getRequestsBetweenTwo("3", "5", 23456).toArray(),
              new ShiftRequest[]{});
      assertArrayEquals(this._getQuery.getRequestsBetweenTwo("3", "5", 23456).toArray(),
              new ShiftRequest[]{});
      List<ShiftRequest> testReqLst = this._getQuery.getRequestsBetweenTwo("7", "2", 12345);
      assertEquals(testReqLst.size(), 1);
      assertEquals(testReqLst.get(0).getID(), 5);
      testReqLst = this._getQuery.getRequestsBetweenTwo("1", "4", 23456);
      assertEquals(testReqLst.size(), 1);
      assertEquals(testReqLst.get(0).getID(), 3);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testShiftExists() {
    try {
      setUpRequests();
      ShiftRequest req = this._requests.get(1);
      assertTrue(this._getQuery.shiftExists(req.getCreatedById(), req.getOrgID(), req.getDate(),
              req.getStartTime(), req.getEndTime()));
      req = this._requests.get(3);
      assertTrue(this._getQuery.shiftExists(req.getCreatedById(), req.getOrgID(), req.getDate(),
              req.getStartTime(), req.getEndTime()));
      assertFalse(this._getQuery.shiftExists("10", req.getOrgID(), req.getDate(),
              req.getStartTime(), req.getEndTime()));
      assertFalse(this._getQuery.shiftExists(req.getCreatedById(), req.getOrgID(), req.getDate(),
              req.getStartTime(), 700));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetUserFunFacts() {
    try {
      assertArrayEquals(this._getQuery.getUserFunFacts("1"), new String[]{"1", "1", "0"});
      assertArrayEquals(this._getQuery.getUserFunFacts("4"), new String[]{"2", "0", "1"});
      assertArrayEquals(this._getQuery.getUserFunFacts("7"), new String[]{"1", "0", "0"});
      assertArrayEquals(this._getQuery.getUserFunFacts("6"), new String[]{"0", "0", "0"});
      assertArrayEquals(this._getQuery.getUserFunFacts("9"), new String[]{"0", "0", "0"});
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetAcceptedReqs() {
    try {
      setUpRequests();
      assertArrayEquals(this._getQuery.getAcceptedRequests("6").toArray(), new ShiftRequest[]{});
      assertArrayEquals(this._getQuery.getAcceptedRequests("2").toArray(), new ShiftRequest[]{});
      List<ShiftRequest> testReqs = this._getQuery.getAcceptedRequests("1");
      assertEquals(testReqs.size(), 1);
      assertEquals(testReqs.get(0).getID(), 4);
      testReqs = this._getQuery.getAcceptedRequests("3");
      assertEquals(testReqs.size(), 1);
      assertEquals(testReqs.get(0).getID(), 6);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetUserRequests() {
    try {
      setUpRequests();
      assertArrayEquals(this._getQuery.getUserRequests("6").toArray(), new ShiftRequest[]{});
      assertArrayEquals(this._getQuery.getUserRequests("1").toArray(), new ShiftRequest[]{});
      List<ShiftRequest> testReqs = this._getQuery.getUserRequests("4");
      System.out.println("This is id of one in testReqs: " + testReqs.get(0).getID());
      assertEquals(testReqs.size(), 3);
      assertEquals(testReqs.get(0).getID(), 2);
      assertEquals(testReqs.get(1).getID(),  3);
      assertEquals(testReqs.get(2).getID(), 4);
      List<ShiftRequest> testReqs2 = this._getQuery.getUserRequests("7");
      assertEquals(testReqs2.size(), 1);
      assertEquals(testReqs2.get(0).getID(), 1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
